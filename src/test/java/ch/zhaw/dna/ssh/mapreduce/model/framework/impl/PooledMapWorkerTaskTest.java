package ch.zhaw.dna.ssh.mapreduce.model.framework.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.concurrent.ExactCommandExecutor;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.zhaw.dna.ssh.mapreduce.model.framework.CombinerInstruction;
import ch.zhaw.dna.ssh.mapreduce.model.framework.KeyValuePair;
import ch.zhaw.dna.ssh.mapreduce.model.framework.MapEmitter;
import ch.zhaw.dna.ssh.mapreduce.model.framework.MapInstruction;
import ch.zhaw.dna.ssh.mapreduce.model.framework.Pool;
import ch.zhaw.dna.ssh.mapreduce.model.framework.WorkerTask.State;

@RunWith(JMock.class)
public class PooledMapWorkerTaskTest {

	private Mockery context;

	private Pool p;

	private MapInstruction mapInstr;

	private CombinerInstruction combInstr;
	
	private String inputUUID;
	
	private String input;

	@Before
	public void initMock() {
		this.context = new JUnit4Mockery();
		this.p = this.context.mock(Pool.class);
		this.mapInstr = this.context.mock(MapInstruction.class);
		this.combInstr = this.context.mock(CombinerInstruction.class);
		this.inputUUID = "inputUUID";
		this.input = "hello";
	}

	@Test
	public void shouldSetMapReduceTaskUUID() {
		PooledMapWorkerTask task = new PooledMapWorkerTask(p, "uuid", mapInstr, combInstr, inputUUID, input);
		assertEquals("uuid", task.getMapReduceTaskUUID());
	}

	@Test
	public void shouldSetMapInstruction() {
		PooledMapWorkerTask task = new PooledMapWorkerTask(p, "uuid", mapInstr, combInstr, inputUUID, input);
		assertSame(mapInstr, task.getMapInstruction());
	}

	@Test
	public void shouldSetCombinerInstruction() {
		PooledMapWorkerTask task = new PooledMapWorkerTask(p, "uuid", mapInstr, combInstr, inputUUID, input);
		assertSame(combInstr, task.getCombinerInstruction());
	}

	@Test
	public void shouldCopeWithNullCombiner() {
		PooledMapWorkerTask task = new PooledMapWorkerTask(p, "uuid", mapInstr, null, inputUUID, input);
		assertNull(task.getCombinerInstruction());
	}

	@Test
	public void shouldEmitUnderMapReduceTaskUUID() {
		Executor poolExec = Executors.newSingleThreadExecutor();
		LocalThreadPool pool = new LocalThreadPool(poolExec);
		pool.init();
		final PooledMapWorkerTask task = new PooledMapWorkerTask(pool, "mrtUuid", new MapInstruction() {
			@Override
			public void map(MapEmitter emitter, String toDo) {
				for (String part : toDo.split(" ")) {
					emitter.emitIntermediateMapResult(part, "1");
				}
			}
		}, null, inputUUID, input);
		ExactCommandExecutor threadExec = new ExactCommandExecutor(1);
		ThreadWorker worker = new ThreadWorker(pool, threadExec);
		pool.donateWorker(worker);
		task.runMapInstruction();
		assertTrue(threadExec.waitForExpectedTasks(100, TimeUnit.MILLISECONDS));
		List<KeyValuePair> vals = worker.getStoredKeyValuePairs("mrtUuid");
		assertTrue(vals.contains(new KeyValuePair("hello", "1")));
		assertEquals(1, vals.size());
	}

	@Test
	public void shouldSetInputUUID() {
		final PooledMapWorkerTask task = new PooledMapWorkerTask(p, "mrtUuid", mapInstr, combInstr, inputUUID, input);
		this.context.checking(new Expectations() {
			{
				oneOf(p).enqueueWork(task);
			}
		});
		task.runMapInstruction();
		assertEquals("inputUUID", task.getCurrentInputUID());
	}

	@Test
	public void shouldSetStateToFailedOnException() {
		Executor poolExec = Executors.newSingleThreadExecutor();
		ExactCommandExecutor taskExec = new ExactCommandExecutor(1);
		final LocalThreadPool pool = new LocalThreadPool(poolExec);
		pool.init();
		ThreadWorker worker = new ThreadWorker(pool, taskExec);
		pool.donateWorker(worker);
		final PooledMapWorkerTask task = new PooledMapWorkerTask(pool, "mrtUuid", new MapInstruction() {

			@Override
			public void map(MapEmitter emitter, String toDo) {
				throw new NullPointerException();
			}
		}, combInstr, inputUUID, input);
		task.runMapInstruction();
		assertTrue(taskExec.waitForExpectedTasks(100, TimeUnit.MILLISECONDS));
		assertEquals(State.FAILED, task.getCurrentState());
		assertNull(task.getWorker());
	}

	@Test
	public void shouldSetStateToCompletedOnSuccess() {
		Executor poolExec = Executors.newSingleThreadExecutor();
		ExactCommandExecutor taskExec = new ExactCommandExecutor(1);
		final LocalThreadPool pool = new LocalThreadPool(poolExec);
		pool.init();
		ThreadWorker worker = new ThreadWorker(pool, taskExec);
		pool.donateWorker(worker);
		final PooledMapWorkerTask task = new PooledMapWorkerTask(pool, "mrtUuid", new MapInstruction() {

			@Override
			public void map(MapEmitter emitter, String toDo) {
				for (String part : toDo.split(" ")) {
					emitter.emitIntermediateMapResult(part, "1");
				}
			}
		}, combInstr, inputUUID, input);
		this.context.checking(new Expectations() {
			{
				oneOf(combInstr).combine(with(aNonNull(Iterator.class)));
			}
		});
		task.runMapInstruction();
		assertTrue(taskExec.waitForExpectedTasks(100, TimeUnit.MILLISECONDS));
		assertEquals(State.COMPLETED, task.getCurrentState());
		assertSame(worker, task.getWorker());
	}

	@Test
	public void shouldSetStateToInitiatedInitially() {
		PooledMapWorkerTask task = new PooledMapWorkerTask(p, "mrtUuid", mapInstr, combInstr, inputUUID, input);
		assertEquals(State.INITIATED, task.getCurrentState());
	}

	@Test
	public void shouldBeInProgressWhileRunning() throws InterruptedException, BrokenBarrierException {
		Executor poolExec = Executors.newSingleThreadExecutor();
		final CyclicBarrier barrier = new CyclicBarrier(2);
		Executor taskExec = Executors.newSingleThreadExecutor();
		final LocalThreadPool pool = new LocalThreadPool(poolExec);
		pool.init();
		ThreadWorker worker = new ThreadWorker(pool, taskExec);
		pool.donateWorker(worker);
		final PooledMapWorkerTask task = new PooledMapWorkerTask(pool, "mrtUuid", new MapInstruction() {

			@Override
			public void map(MapEmitter emitter, String toDo) {
				try {
					barrier.await();
				} catch (Exception e) {
					throw new IllegalStateException(e);
				}
			}
		}, combInstr, inputUUID, input);
		task.runMapInstruction();
		Thread.yield();
		Thread.sleep(200);
		assertEquals(State.INPROGRESS, task.getCurrentState());
		try {
			barrier.await(100, TimeUnit.MILLISECONDS);
		} catch (TimeoutException te) {
			fail("should return immediately");
		}
	}

	@Test
	public void shouldBeAbleToRerunTests() {
		Executor poolExec = Executors.newSingleThreadExecutor();
		ExactCommandExecutor threadExec1 = new ExactCommandExecutor(1);
		ExactCommandExecutor threadExec2 = new ExactCommandExecutor(1);
		final LocalThreadPool pool = new LocalThreadPool(poolExec);
		final AtomicInteger cnt = new AtomicInteger();
		pool.init();
		ThreadWorker worker1 = new ThreadWorker(pool, threadExec1);
		pool.donateWorker(worker1);
		ThreadWorker worker2 = new ThreadWorker(pool, threadExec2);
		pool.donateWorker(worker2);
		final PooledMapWorkerTask task = new PooledMapWorkerTask(pool, "mrtUuid", new MapInstruction() {

			@Override
			public void map(MapEmitter emitter, String toDo) {
				if (cnt.get() == 0) {
					cnt.incrementAndGet();
					throw new NullPointerException();
				} else if (cnt.get() == 1) {
					// successful
				} else {
					throw new NullPointerException();
				}
			}
		}, combInstr, inputUUID, input);
		task.runMapInstruction();
		assertTrue(threadExec1.waitForExpectedTasks(100, TimeUnit.MILLISECONDS));
		assertEquals(State.FAILED, task.getCurrentState());
		assertNull(task.getWorker());
		task.runMapInstruction();
		assertTrue(threadExec2.waitForExpectedTasks(100, TimeUnit.MILLISECONDS));
		assertEquals(State.COMPLETED, task.getCurrentState());
		assertSame(worker2, task.getWorker());
	}

	@Test
	public void shouldBeEnqueuedAfterSubmissionToPool() {
		final PooledMapWorkerTask task = new PooledMapWorkerTask(p, "mrtuid", mapInstr, combInstr, inputUUID, input);
		this.context.checking(new Expectations() {
			{
				oneOf(p).enqueueWork(task);
			}
		});
		task.runMapInstruction();
		assertEquals(State.ENQUEUED, task.getCurrentState());
	}
}
