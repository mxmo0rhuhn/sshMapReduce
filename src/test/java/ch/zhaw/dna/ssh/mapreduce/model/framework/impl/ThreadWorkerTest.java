package ch.zhaw.dna.ssh.mapreduce.model.framework.impl;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.Sequence;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.zhaw.dna.ssh.mapreduce.model.framework.Pool;
import ch.zhaw.dna.ssh.mapreduce.model.framework.WorkerTask;

@RunWith(JMock.class)
public class ThreadWorkerTest {

	private Mockery context;

	@Before
	public void initMockery() {
		this.context = new JUnit4Mockery();
	}

	/**
	 * make sure a threadWorker executes its work and goes back into the pool
	 */
	@Test
	public void shouldGoBackToPool() {
		final Pool p = this.context.mock(Pool.class);
		final WorkerTask task = this.context.mock(WorkerTask.class);
		final ExecutorService exec = Executors.newSingleThreadExecutor();
		final ThreadWorker worker = new ThreadWorker(p, exec);
		final Sequence seq = context.sequence("poolseq");

		this.context.checking(new Expectations() {
			{
				one(task).doWork();
				inSequence(seq);
				one(p).workerIsFinished(with(same(worker)));
			}
		});
		worker.execute(task);
	}
	
	@Test
	public void shouldExecuteTenTimes() {
		final Pool p = this.context.mock(Pool.class);
		final WorkerTask[] tasks = new WorkerTask[10];
		for (int i = 0; i < 10; i ++) {
			tasks[i] = this.context.mock(WorkerTask.class, "wt" + i);
		}
		final ExecutorService exec = Executors.newSingleThreadExecutor();
		final ThreadWorker worker = new ThreadWorker(p, exec);

		this.context.checking(new Expectations() {
			{
				exactly(10).of(any(WorkerTask.class)).method("doWork");
				exactly(10).of(p).workerIsFinished(with(same(worker)));
			}
		});
		for (int i = 0; i < 10; i ++) {
			worker.execute(tasks[i]);
		}
	}

}
