package ch.zhaw.dna.ssh.mapreduce.model.framework.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.zhaw.dna.ssh.mapreduce.model.framework.CombinerInstruction;
import ch.zhaw.dna.ssh.mapreduce.model.framework.MapInstruction;
import ch.zhaw.dna.ssh.mapreduce.model.framework.Pool;
import ch.zhaw.dna.ssh.mapreduce.model.framework.WorkerTask.State;
import ch.zhaw.dna.ssh.mapreduce.model.framework.registry.Registry;

@RunWith(JMock.class)
public class PooledMapRunnerTest {

	private static final long SHORT = 200;

	private static final long MEDIUM = 3 * SHORT;

	private static final long LONG = 3 * MEDIUM;

	private Mockery context;

	private Pool p;

	@Before
	public void initMock() {
		this.context = new JUnit4Mockery();
		this.p = this.context.mock(Pool.class);
	}

	@Test
	public void shouldReturnPreviouslyEmittedResult() {
		PooledMapWorkerTask r = new PooledMapWorkerTask(p);
		r.emitIntermediateMapResult("hello", "1");
		assertEquals("1", r.getIntermediate("hello").get(0));
	}

	@Test
	public void shouldAccumulateResults() {
		PooledMapWorkerTask r = new PooledMapWorkerTask(p);
		r.emitIntermediateMapResult("hello", "1");
		r.emitIntermediateMapResult("hello", "2");
		List<String> intermed = r.getIntermediate("hello");
		assertEquals(2, intermed.size());
		assertTrue(intermed.contains("1"));
		assertTrue(intermed.contains("2"));
	}

	@Test
	public void shouldNotAccumulateDifferentKeys() {
		PooledMapWorkerTask r = new PooledMapWorkerTask(p);
		r.emitIntermediateMapResult("hello", "1");
		r.emitIntermediateMapResult("help", "2");
		String one = r.getIntermediate("hello").get(0);
		String two = r.getIntermediate("help").get(0);
		assertEquals("1", one);
		assertEquals("2", two);
	}

	@Test
	public void shouldRemoveIntermediate() {
		PooledMapWorkerTask r = new PooledMapWorkerTask(p);
		r.emitIntermediateMapResult("hello", "1");
		String one = r.getIntermediate("hello").get(0);
		assertEquals("1", one);
		assertNull(r.getIntermediate("hello"));
	}

	@Test
	public void shouldReturnAllKeys() {
		PooledMapWorkerTask r = new PooledMapWorkerTask(p);
		r.emitIntermediateMapResult("hello", "1");
		r.emitIntermediateMapResult("help", "1");
		r.emitIntermediateMapResult("hell", "1");
		r.emitIntermediateMapResult("hall", "1");
		List<String> snapshot = r.getKeysSnapshot();
		assertEquals(4, snapshot.size());
		assertTrue(snapshot.contains("hello"));
		assertTrue(snapshot.contains("help"));
		assertTrue(snapshot.contains("hell"));
		assertTrue(snapshot.contains("hall"));
	}

	@Test
	public void shouldReturnUniqueKeys() {
		PooledMapWorkerTask r = new PooledMapWorkerTask(p);
		r.emitIntermediateMapResult("hello", "1");
		r.emitIntermediateMapResult("hello", "2");
		r.emitIntermediateMapResult("hello", "3");
		List<String> snapshot = r.getKeysSnapshot();
		assertEquals(1, snapshot.size());
		assertEquals("hello", snapshot.get(0));
	}

	@Test
	public void shouldInvokeMapTask() throws InterruptedException {
		final Pool pool = Registry.getComponent(Pool.class);
		final PooledMapWorkerTask mapRunner = new PooledMapWorkerTask(pool);
		final MapInstruction mapTask = this.context.mock(MapInstruction.class);
		final String input = "hello world";
		final ExecutorService exec = Executors.newSingleThreadExecutor();
		final ThreadWorker tw = new ThreadWorker(pool, exec);

		mapRunner.setMapTask(mapTask);
		pool.donateWorker(tw);

		this.context.checking(new Expectations() {
			{
				one(mapTask).map(with(same(mapRunner)), with(equal(input)));
			}
		});

		mapRunner.runMapTask(input);
		Thread.yield();
		pool.shutdownNow();
	}

	@Test
	public void shouldRunCombineAfterSpecifiedNumberOfResults() {
		final PooledMapWorkerTask mapRunner = new PooledMapWorkerTask(p);
		mapRunner.setMaxWaitResults(1);
		final CombinerInstruction combineTask = this.context.mock(CombinerInstruction.class);
		mapRunner.setCombineTask(combineTask);

		this.context.checking(new Expectations() {
			{
				one(combineTask).combine(with(aNonNull(Iterator.class)));
			}
		});

		mapRunner.emitIntermediateMapResult("hello", "1");
	}

	@Test
	public void shouldRunCombineAfterSpecifiedNumberOfResults2() {
		final PooledMapWorkerTask mapRunner = new PooledMapWorkerTask(p);
		mapRunner.setMaxWaitResults(2);
		final CombinerInstruction combineTask = this.context.mock(CombinerInstruction.class);
		mapRunner.setCombineTask(combineTask);

		this.context.checking(new Expectations() {
			{
				one(combineTask).combine(with(aNonNull(Iterator.class)));
			}
		});

		mapRunner.emitIntermediateMapResult("hello", "1");
		mapRunner.emitIntermediateMapResult("hello", "1");
	}

	@Test
	public void shouldUpdateMaxWaitResultsOnTheFly() {
		final PooledMapWorkerTask mapRunner = new PooledMapWorkerTask(p);
		mapRunner.setMaxWaitResults(3);
		assertEquals(3, mapRunner.getMaxWaitResults());
		mapRunner.setMaxWaitResults(4);
		assertEquals(4, mapRunner.getMaxWaitResults());
	}

	@Test
	public void shouldWorkWithoutCombinerTask() {
		final PooledMapWorkerTask mapRunner = new PooledMapWorkerTask(p);
		mapRunner.setMaxWaitResults(1);
		mapRunner.emitIntermediateMapResult("hell", "1");
		mapRunner.emitIntermediateMapResult("hell0", "1");
	}

	@Test
	public void shouldBeIdleAtStart() {
		final PooledMapWorkerTask mapRunner = new PooledMapWorkerTask(p);
		assertEquals(State.INITIATED, mapRunner.getCurrentState());
	}

	@Test
	public void shouldBeRunningAfterSubmit() {
		final PooledMapWorkerTask mapRunner = new PooledMapWorkerTask(p);
		MapInstruction mapTask = this.context.mock(MapInstruction.class);
		mapRunner.setMapTask(mapTask);
		mapRunner.runMapTask("hello");
		assertEquals(State.INPROGRESS, mapRunner.getCurrentState());
	}
}
