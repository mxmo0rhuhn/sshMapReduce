package ch.zhaw.dna.ssh.mapreduce.model.framework.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.concurrent.ExactCommandExecutor;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.zhaw.dna.ssh.mapreduce.model.framework.Worker;
import ch.zhaw.dna.ssh.mapreduce.model.framework.WorkerTask;

@RunWith(JMock.class)
public class LocalThreadPoolTest {

	private Mockery context;

	private Executor executor;

	@Before
	public void initMock() {
		this.context = new JUnit4Mockery();
		this.executor = Executors.newSingleThreadExecutor();
	}

	@Test
	public void shouldHaveZeroInitialWorker() {
		LocalThreadPool p = new LocalThreadPool(executor);
		p.init();
		assertEquals(0, p.getCurrentPoolSize());
		assertEquals(0, p.getFreeWorkers());
	}

	@Test
	public void shouldHaveOneWorker() {
		Worker w = this.context.mock(Worker.class);
		LocalThreadPool p = new LocalThreadPool(executor);
		p.init();
		p.donateWorker(w);
		assertEquals(1, p.getCurrentPoolSize());
		assertEquals(1, p.getFreeWorkers());
	}

	@Test
	public void shouldHaveTwoWorker() {
		Worker w1 = this.context.mock(Worker.class, "w1");
		Worker w2 = this.context.mock(Worker.class, "w2");
		LocalThreadPool p = new LocalThreadPool(executor);
		p.init();
		p.donateWorker(w1);
		p.donateWorker(w2);
		assertEquals(2, p.getCurrentPoolSize());
		assertEquals(2, p.getFreeWorkers());
	}

	@Test(expected = IllegalStateException.class)
	public void shouldNotBeAbleToInitTwice() {
		LocalThreadPool p = new LocalThreadPool(this.executor);
		try {
			p.init(); // first time must work
		} catch (IllegalStateException ise) {
			fail(ise.getMessage());
		}
		p.init();
	}

	@Test
	public void shouldExecuteWork() throws InterruptedException {
		final WorkerTask task = this.context.mock(WorkerTask.class);
		final Executor poolExec = Executors.newSingleThreadExecutor();
		final ExactCommandExecutor threadExec = new ExactCommandExecutor(1);
		LocalThreadPool p = new LocalThreadPool(poolExec);
		p.init();
		final ThreadWorker worker = new ThreadWorker(p, threadExec);
		p.donateWorker(worker);

		this.context.checking(new Expectations() {
			{
				one(task).doWork(worker);
			}
		});

		p.enqueueWork(task);
		assertTrue(threadExec.waitForExpectedTasks(300, TimeUnit.MILLISECONDS));
	}

	@Test
	public void shouldBeAvailableAgain() {
		final WorkerTask workerTask = this.context.mock(WorkerTask.class);
		final LocalThreadPool pool = new LocalThreadPool(this.executor);
		pool.init();
		final ExactCommandExecutor threadExec = new ExactCommandExecutor(1);
		final ThreadWorker worker = new ThreadWorker(pool, threadExec);
		pool.donateWorker(worker);
		this.context.checking(new Expectations() {
			{
				oneOf(workerTask).doWork(worker);
			}
		});
		pool.enqueueWork(workerTask);
		assertTrue(threadExec.waitForExpectedTasks(300, TimeUnit.MILLISECONDS));
		assertEquals(1, pool.getFreeWorkers());
	}

	@Test
	public void shouldStopUponInterruption() throws InterruptedException {
		final Thread[] ts = new Thread[1];
		Executor exec = Executors.newSingleThreadExecutor(new ThreadFactory() {
			@Override
			public Thread newThread(Runnable command) {
				ts[0] = new Thread(command);
				return ts[0];
			}
		});
		LocalThreadPool pool = new LocalThreadPool(exec);
		pool.init();
		assertTrue(pool.isRunning());
		Thread.sleep(200);
		ts[0].interrupt();
		Thread.yield();
		Thread.sleep(200);
		assertFalse(pool.isRunning());
	}

}