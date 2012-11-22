package ch.zhaw.dna.ssh.mapreduce.model.framework.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Iterator;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.concurrent.DeterministicExecutor;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.zhaw.dna.ssh.mapreduce.model.framework.MapRunner;
import ch.zhaw.dna.ssh.mapreduce.model.framework.Master;
import ch.zhaw.dna.ssh.mapreduce.model.framework.PoolHelper;
import ch.zhaw.dna.ssh.mapreduce.model.framework.ReduceTask;
import ch.zhaw.dna.ssh.mapreduce.model.framework.TestConfig;
import ch.zhaw.dna.ssh.mapreduce.model.framework.WorkerTask.State;

import com.google.inject.Guice;

@RunWith(JMock.class)
public class PooledReduceRunnerTest {

	private Mockery context;

	@Before
	public void initMockery() {
		this.context = new JUnit4Mockery();
	}

	@Test
	public void shouldSaveResultsInGlobalStructure() {
		PooledReduceRunner reduceRunner = new PooledReduceRunner();
		Master master = Guice.createInjector(new TestConfig()).getInstance(Master.class);
		reduceRunner.setKey("hello");
		reduceRunner.setMaster(master);
		reduceRunner.emit("3");
		assertTrue(master.getGlobalResultStructure().get("hello").contains("3"));
	}

	@Test
	public void shouldBeIdleAtStart() {
		PooledReduceRunner reduceRunner = new PooledReduceRunner();
		assertEquals(State.IDLE, reduceRunner.getCurrentState());
	}

	@Test
	public void shouldRunReduceTask() throws InterruptedException {
		final String input = "hello";
		final PooledReduceRunner reduceRunner = new PooledReduceRunner();
		final DeterministicExecutor exec = new DeterministicExecutor();
		ThreadWorker worker = new ThreadWorker(PoolHelper.getPool(), exec);
		final ReduceTask reduceTask = this.context.mock(ReduceTask.class);
		PoolHelper.getPool().donateWorker(worker);
		reduceRunner.setReduceTask(reduceTask);
		reduceRunner.setKey(input);
		
		MapRunner mapRunner = new PooledMapRunner();
		mapRunner.emitIntermediateMapResult(input, "1");

		this.context.checking(new Expectations() {
			{
				one(reduceTask).reduce(with(same(reduceRunner)), with(equal(input)), with(aNonNull(Iterator.class)));
			}
		});
		
		reduceRunner.runReduceTask(Arrays.asList(new MapRunner[]{mapRunner}));
		Thread.sleep(300);
		exec.runUntilIdle();
	}
}
