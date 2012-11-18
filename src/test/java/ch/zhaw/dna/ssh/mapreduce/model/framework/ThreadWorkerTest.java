package ch.zhaw.dna.ssh.mapreduce.model.framework;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.Sequence;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.concurrent.DeterministicExecutor;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.zhaw.dna.ssh.mapreduce.model.framework.impl.ThreadWorker;

@RunWith(JMock.class)
public class ThreadWorkerTest {

	private Mockery context;

	@Before
	public void initMockery() {
		this.context = new JUnit4Mockery();
	}

	@Test
	public void shouldGoBackToPool() {
		final Pool p = this.context.mock(Pool.class);
		final WorkerTask task = this.context.mock(WorkerTask.class);
		final DeterministicExecutor executor = new DeterministicExecutor();
		final ThreadWorker worker = new ThreadWorker(p, executor);
		final Sequence seq = context.sequence("poolseq");

		this.context.checking(new Expectations() {
			{
				one(task).doWork();
				inSequence(seq);
				one(p).workerIsFinished(with(same(worker)));
			}
		});
		worker.execute(task);
		executor.runUntilIdle();
	}

}
