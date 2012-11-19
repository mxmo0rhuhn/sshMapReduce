package ch.zhaw.dna.ssh.mapreduce.model.framework;

import static org.junit.Assert.assertEquals;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.concurrent.DeterministicExecutor;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.zhaw.dna.ssh.mapreduce.model.framework.impl.AsyncPool;
import ch.zhaw.dna.ssh.mapreduce.model.framework.impl.ThreadWorker;

@RunWith(JMock.class)
public class AsyncPoolTest {
	
	private Mockery context;

	@Before
	public void initMock() {
		this.context = new JUnit4Mockery();
	}

	@Test
	public void shouldHaveZeroInitialWorker() {
		AsyncPool p = new AsyncPool();
		p.init();
		assertEquals(0, p.getCurrentPoolSize());
		assertEquals(0, p.getFreeWorkers());
	}

	@Test
	public void shouldHaveOneWorker() {
		Worker w = this.context.mock(Worker.class);
		AsyncPool p = new AsyncPool();
		p.init();
		p.donateWorker(w);
		assertEquals(1, p.getCurrentPoolSize());
		assertEquals(1, p.getFreeWorkers());
	}

	@Test
	public void shouldHaveTwoWorker() {
		Worker w1 = this.context.mock(Worker.class, "w1");
		Worker w2 = this.context.mock(Worker.class, "w2");
		AsyncPool p = new AsyncPool();
		p.init();
		p.donateWorker(w1);
		p.donateWorker(w2);
		assertEquals(2, p.getCurrentPoolSize());
		assertEquals(2, p.getFreeWorkers());
	}

	@Test
	public void shouldExecuteWork() throws InterruptedException {
		final WorkerTask task = this.context.mock(WorkerTask.class);
		DeterministicExecutor exec = new DeterministicExecutor();
		AsyncPool p = new AsyncPool();
		p.init();
		final ThreadWorker worker = new ThreadWorker(p, exec);
		p.donateWorker(worker);

		this.context.checking(new Expectations() {
			{
				one(task).doWork();
			}
		});

		p.enqueueWork(task);
		Thread.sleep(200);
		exec.runUntilIdle();
	}

}
