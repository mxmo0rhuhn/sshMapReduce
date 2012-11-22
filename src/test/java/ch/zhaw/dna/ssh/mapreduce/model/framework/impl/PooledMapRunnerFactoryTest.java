package ch.zhaw.dna.ssh.mapreduce.model.framework.impl;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.zhaw.dna.ssh.mapreduce.model.framework.CombinerTask;
import ch.zhaw.dna.ssh.mapreduce.model.framework.MapTask;

@RunWith(JMock.class)
public class PooledMapRunnerFactoryTest {

	private Mockery context;

	@Before
	public void initMock() {
		this.context = new JUnit4Mockery();
	}
	
	@Test
	public void shouldCreateNewMapRunner() {
		PooledMapRunnerFactory factory = new PooledMapRunnerFactory();
		assertNotNull(factory.getMapRunner());
	}
	
	@Test
	public void shouldCreateNewMapRunnerEveryTime() {
		PooledMapRunnerFactory factory = new PooledMapRunnerFactory();
		assertNotSame(factory.getMapRunner(), factory.getMapRunner());
	}

	@Test
	public void shouldSetCombinerTask() {
		final CombinerTask combiner = this.context.mock(CombinerTask.class);
		PooledMapRunnerFactory factory = new PooledMapRunnerFactory();
		factory.assignCombineTask(combiner);
		assertSame(combiner, factory.getMapRunner().getCombinerTask());
	}
	
	@Test
	public void shouldSetMapTask() {
		final MapTask mapTask = this.context.mock(MapTask.class);
		PooledMapRunnerFactory factory = new PooledMapRunnerFactory();
		factory.assignMapTask(mapTask);
		assertSame(mapTask, factory.getMapRunner().getMapTask());
	}

}
