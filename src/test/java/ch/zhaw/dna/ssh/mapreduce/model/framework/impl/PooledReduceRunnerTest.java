package ch.zhaw.dna.ssh.mapreduce.model.framework.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.zhaw.dna.ssh.mapreduce.model.framework.Master;
import ch.zhaw.dna.ssh.mapreduce.model.framework.Pool;
import ch.zhaw.dna.ssh.mapreduce.model.framework.WorkerTask.State;
import ch.zhaw.dna.ssh.mapreduce.model.framework.registry.Registry;

@RunWith(JMock.class)
public class PooledReduceRunnerTest {

	private Mockery context;

	private Pool p;

	private Master master;

	@Before
	public void initMockery() {
		this.context = new JUnit4Mockery();
		this.p = this.context.mock(Pool.class);
		this.master = Registry.getComponent(Master.class);
	}

	@Test
	public void shouldSaveResultsInGlobalStructure() {
		PooledReduceWorkerTask reduceRunner = new PooledReduceWorkerTask(p, master);
		reduceRunner.setKey("hello");
		reduceRunner.emit("3");
		assertTrue(master.getGlobalResultStructure().get("hello").contains("3"));
	}

	@Test
	public void shouldBeIdleAtStart() {
		PooledReduceWorkerTask reduceRunner = new PooledReduceWorkerTask(p, master);
		assertEquals(State.INITIATED, reduceRunner.getCurrentState());
	}

}
