package ch.zhaw.dna.ssh.mapreduce.model.framework.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;
import java.util.List;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.concurrent.DeterministicExecutor;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.zhaw.dna.ssh.mapreduce.model.framework.CombinerTask;
import ch.zhaw.dna.ssh.mapreduce.model.framework.MapTask;
import ch.zhaw.dna.ssh.mapreduce.model.framework.PoolHelper;
import ch.zhaw.dna.ssh.mapreduce.model.framework.WorkerTask;
import ch.zhaw.dna.ssh.mapreduce.model.framework.WorkerTask.State;

@RunWith(JMock.class)
public class PooledMapRunnerTest {
	
	private Mockery context;
	
	@Before
	public void initMock() {
		this.context = new JUnit4Mockery();
	}

	@Test
	public void shouldReturnPreviouslyEmittedResult() {
		PooledMapRunner r = new PooledMapRunner();
		r.emitIntermediateMapResult("hello", "1");
		assertEquals("1", r.getIntermediate("hello").get(0));
	}

	@Test
	public void shouldAccumulateResults() {
		PooledMapRunner r = new PooledMapRunner();
		r.emitIntermediateMapResult("hello", "1");
		r.emitIntermediateMapResult("hello", "2");
		List<String> intermed = r.getIntermediate("hello");
		assertEquals(2, intermed.size());
		assertTrue(intermed.contains("1"));
		assertTrue(intermed.contains("2"));
	}

	@Test
	public void shouldNotAccumulateDifferentKeys() {
		PooledMapRunner r = new PooledMapRunner();
		r.emitIntermediateMapResult("hello", "1");
		r.emitIntermediateMapResult("help", "2");
		String one = r.getIntermediate("hello").get(0);
		String two = r.getIntermediate("help").get(0);
		assertEquals("1", one);
		assertEquals("2", two);
	}
	
	@Test
	public void shouldRemoveIntermediate() {
		PooledMapRunner r = new PooledMapRunner();
		r.emitIntermediateMapResult("hello", "1");
		String one = r.getIntermediate("hello").get(0);
		assertEquals("1", one);
		assertNull(r.getIntermediate("hello"));
	}

	@Test
	public void shouldReturnAllKeys() {
		PooledMapRunner r = new PooledMapRunner();
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
		PooledMapRunner r = new PooledMapRunner();
		r.emitIntermediateMapResult("hello", "1");
		r.emitIntermediateMapResult("hello", "2");
		r.emitIntermediateMapResult("hello", "3");
		List<String> snapshot = r.getKeysSnapshot();
		assertEquals(1, snapshot.size());
		assertEquals("hello", snapshot.get(0));
	}
	
	@Test
	@Ignore
	public void shouldInvokeMapTask() throws InterruptedException {
		final PooledMapRunner mapRunner = new PooledMapRunner();
		final MapTask mapTask = this.context.mock(MapTask.class);
		mapRunner.setMapTask(mapTask);
		final String input = "hello world";
		DeterministicExecutor exec = new DeterministicExecutor();
		ThreadWorker tw = new ThreadWorker(PoolHelper.getPool(), exec);
		PoolHelper.getPool().donateWorker(tw);
		
		this.context.checking(new Expectations() {{
			one(mapTask).map(with(same(mapRunner)), with(equal(input)));
		}});
		
		mapRunner.runMapTask(input);
		awaitFinish(mapRunner, 10000);
		exec.runUntilIdle();
	}
	
	private static void awaitFinish(WorkerTask worker, long timeout) {
		long start = System.currentTimeMillis();
		while (worker.getCurrentState() != State.COMPLETED && start + timeout > System.currentTimeMillis()) {
			Thread.yield();
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				Thread.interrupted();
				break;
			}
		}
	}
	
	@Test
	public void shouldRunCombineAfterSpecifiedNumberOfResults() {
		final PooledMapRunner mapRunner = new PooledMapRunner();
		mapRunner.setMaxWaitResults(1);
		final CombinerTask combineTask = this.context.mock(CombinerTask.class);
		mapRunner.setCombineTask(combineTask);
		
		this.context.checking(new Expectations() {{
			one(combineTask).combine(with(aNonNull(Iterator.class)));
		}});
		
		mapRunner.emitIntermediateMapResult("hello", "1");
	}
	
	@Test
	public void shouldRunCombineAfterSpecifiedNumberOfResults2() {
		final PooledMapRunner mapRunner = new PooledMapRunner();
		mapRunner.setMaxWaitResults(2);
		final CombinerTask combineTask = this.context.mock(CombinerTask.class);
		mapRunner.setCombineTask(combineTask);
		
		this.context.checking(new Expectations() {{
			one(combineTask).combine(with(aNonNull(Iterator.class)));
		}});
		
		mapRunner.emitIntermediateMapResult("hello", "1");
		mapRunner.emitIntermediateMapResult("hello", "1");
	}
	
	@Test
	public void shouldUpdateMaxWaitResultsOnTheFly() {
		PooledMapRunner mapRunner = new PooledMapRunner();
		mapRunner.setMaxWaitResults(3);
		assertEquals(3, mapRunner.getMaxWaitResults());
		mapRunner.setMaxWaitResults(4);
		assertEquals(4, mapRunner.getMaxWaitResults());
	}
	
	@Test
	public void shouldWorkWithoutCombinerTask() {
		PooledMapRunner mapRunner = new PooledMapRunner();
		mapRunner.setMaxWaitResults(1);
		mapRunner.emitIntermediateMapResult("hell", "1");
		mapRunner.emitIntermediateMapResult("hell0", "1");
	}
	
	
	@Test
	public void shouldBeIdleAtStart() {
		PooledMapRunner mapRunner = new PooledMapRunner();
		assertEquals(State.IDLE, mapRunner.getCurrentState());
	}
	
	@Test
	public void shouldBeRunningAfterSubmit() {
		final PooledMapRunner mapRunner = new PooledMapRunner();
		MapTask mapTask = this.context.mock(MapTask.class);
		mapRunner.setMapTask(mapTask);
		mapRunner.runMapTask("hello");
		assertEquals(State.INPROGRESS, mapRunner.getCurrentState());
	}
}