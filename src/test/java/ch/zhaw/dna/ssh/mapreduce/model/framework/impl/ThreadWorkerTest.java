package ch.zhaw.dna.ssh.mapreduce.model.framework.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.Sequence;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.concurrent.DeterministicExecutor;
import org.jmock.lib.concurrent.ExactCommandExecutor;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.zhaw.dna.ssh.mapreduce.model.framework.KeyValuePair;
import ch.zhaw.dna.ssh.mapreduce.model.framework.Pool;
import ch.zhaw.dna.ssh.mapreduce.model.framework.WorkerTask;

@RunWith(JMock.class)
public class ThreadWorkerTest {

	private Mockery context;

	private Pool pool;

	@Before
	public void initMockery() {
		this.context = new JUnit4Mockery();
		this.pool = this.context.mock(Pool.class);
	}

	@Test
	public void shouldGoBackToPool() {
		ExactCommandExecutor exec = new ExactCommandExecutor(1);
		final ThreadWorker worker = new ThreadWorker(pool, exec);
		final WorkerTask task = this.context.mock(WorkerTask.class);
		final Sequence seq = this.context.sequence("executionOrder");
		worker.execute(task);
		this.context.checking(new Expectations() {
			{
				oneOf(task).doWork(worker);
				inSequence(seq);
				oneOf(pool).workerIsFinished(worker);
			}
		});
		exec.waitForExpectedTasks(200, TimeUnit.MILLISECONDS);
	}

	@Test
	public void shouldReturnPreviouslyStoredValues() {
		DeterministicExecutor exec = new DeterministicExecutor();
		ThreadWorker worker = new ThreadWorker(pool, exec);
		worker.storeKeyValuePair("mrtuid", "key", "value");
		List<KeyValuePair> vals = worker.getStoredKeyValuePairs("mrtuid");
		assertTrue(vals.contains(new KeyValuePair("key", "value")));
		assertTrue(vals.size() == 1);
	}

	@Test
	public void shouldAssociateResultsForSameMapReduceTask() {
		DeterministicExecutor exec = new DeterministicExecutor();
		ThreadWorker worker = new ThreadWorker(pool, exec);
		worker.storeKeyValuePair("mrtuid", "key", "value");
		worker.storeKeyValuePair("mrtuid", "key2", "value");
		List<KeyValuePair> vals = worker.getStoredKeyValuePairs("mrtuid");
		assertTrue(vals.contains(new KeyValuePair("key", "value")));
		assertTrue(vals.contains(new KeyValuePair("key2", "value")));
		assertTrue(vals.size() == 2);
	}

	@Test
	public void shouldHandleMultipleMapReduceUIds() {
		DeterministicExecutor exec = new DeterministicExecutor();
		ThreadWorker worker = new ThreadWorker(pool, exec);
		worker.storeKeyValuePair("mrtuid1", "key", "value");
		worker.storeKeyValuePair("mrtuid2", "key2", "value2");
		List<KeyValuePair> vals1 = worker.getStoredKeyValuePairs("mrtuid1");
		List<KeyValuePair> vals2 = worker.getStoredKeyValuePairs("mrtuid2");
		assertTrue(vals1.contains(new KeyValuePair("key", "value")));
		assertTrue(vals2.contains(new KeyValuePair("key2", "value2")));
		assertTrue(vals1.size() == 1);
		assertTrue(vals2.size() == 1);
	}

	@Test
	public void shouldReplaceKeyValuePairs() {
		DeterministicExecutor exec = new DeterministicExecutor();
		ThreadWorker worker = new ThreadWorker(pool, exec);
		worker.storeKeyValuePair("mrtuid1", "key", "value");
		List<KeyValuePair> vals1 = worker.getStoredKeyValuePairs("mrtuid1");
		assertTrue(vals1.contains(new KeyValuePair("key", "value")));
		assertTrue(vals1.size() == 1);

		worker.replaceStoredKeyValuePairs("mrtuid1",
				Arrays.asList(new KeyValuePair[] { new KeyValuePair("key2", "value2") }));
		vals1 = worker.getStoredKeyValuePairs("mrtuid1");
		assertTrue(vals1.contains(new KeyValuePair("key2", "value2")));
		assertTrue(vals1.size() == 1);

		vals1 = worker.getStoredKeyValuePairs("mrtuid1");
		assertFalse(vals1.contains(new KeyValuePair("key", "value")));
	}
	
	@Test
	public void shouldSetValueIfReplaceIsInvokedForAnInexistentKey() {
		DeterministicExecutor exec = new DeterministicExecutor();
		ThreadWorker worker = new ThreadWorker(pool, exec);
		worker.replaceStoredKeyValuePairs("mrtuid1",
				Arrays.asList(new KeyValuePair[] { new KeyValuePair("key2", "value2") }));
		List<KeyValuePair> vals1 = worker.getStoredKeyValuePairs("mrtuid1");
		assertTrue(vals1.contains(new KeyValuePair("key2", "value2")));
		assertTrue(vals1.size() == 1);
	}

}
