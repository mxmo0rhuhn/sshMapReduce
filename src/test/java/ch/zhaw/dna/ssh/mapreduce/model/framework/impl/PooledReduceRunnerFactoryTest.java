package ch.zhaw.dna.ssh.mapreduce.model.framework.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;

import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.zhaw.dna.ssh.mapreduce.model.framework.MapReduceTask;
import ch.zhaw.dna.ssh.mapreduce.model.framework.MapTask;
import ch.zhaw.dna.ssh.mapreduce.model.framework.ReduceTask;

@RunWith(JMock.class)
public class PooledReduceRunnerFactoryTest {

	private Mockery context;

	@Before
	public void initMock() {
		this.context = new JUnit4Mockery();
	}
	
	@Test
	public void shouldCreateNewMapRunner() {
		PooledReduceRunnerFactory factory = new PooledReduceRunnerFactory();
		assertNotNull(factory.create("hello"));
	}
	
	@Test
	public void shouldCreateNewMapRunnerEveryTime() {
		PooledReduceRunnerFactory factory = new PooledReduceRunnerFactory();
		assertNotSame(factory.create("hello"), factory.create("hello"));
	}
	
	@Test
	public void shouldSetWord() {
		PooledReduceRunnerFactory factory = new PooledReduceRunnerFactory();
		assertEquals("hello", factory.create("hello").getKey()); 
	}

	@Test
	public void shouldSetCombinerTask() {
		PooledReduceRunnerFactory factory = new PooledReduceRunnerFactory();
		ReduceTask reduceTask = this.context.mock(ReduceTask.class);
		factory.assignReduceTask(reduceTask);
		assertEquals(reduceTask, factory.create("hello").getReduceTask()); 
	}
	
	@Test
	public void shouldSetMaster() {
		PooledReduceRunnerFactory factory = new PooledReduceRunnerFactory();
		MapReduceTask master = new MapReduceTask(this.context.mock(MapTask.class), this.context.mock(ReduceTask.class));
		factory.setMaster(master);
	}

}
