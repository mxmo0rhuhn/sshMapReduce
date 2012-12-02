package ch.zhaw.dna.ssh.mapreduce.model.framework.registry;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.zhaw.dna.ssh.mapreduce.model.framework.CombinerTask;
import ch.zhaw.dna.ssh.mapreduce.model.framework.MapRunner;
import ch.zhaw.dna.ssh.mapreduce.model.framework.MapTask;
import ch.zhaw.dna.ssh.mapreduce.model.framework.Master;
import ch.zhaw.dna.ssh.mapreduce.model.framework.Pool;
import ch.zhaw.dna.ssh.mapreduce.model.framework.ReduceRunner;
import ch.zhaw.dna.ssh.mapreduce.model.framework.ReduceTask;
import ch.zhaw.dna.ssh.mapreduce.model.framework.RunnerFactory;
import ch.zhaw.dna.ssh.mapreduce.model.framework.Worker;
import ch.zhaw.dna.ssh.mapreduce.model.framework.impl.LocalThreadPool;

@RunWith(JMock.class)
public class RegistryTest {

	private Mockery context;

	@Before
	public void initMock() {
		this.context = new JUnit4Mockery();
	}

	@Test
	public void shouldDefineBindingForMaster() {
		assertNotNull(Registry.getComponent(Master.class));
	}

	@Test
	public void poolShouldBeSingleton() {
		Pool p1 = Registry.getComponent(Pool.class);
		Pool p2 = Registry.getComponent(Pool.class);
		assertSame(p1, p2);
	}

	@Test
	public void poolShouldBeSingletonInDifferentThreads() throws InterruptedException {
		final Pool[] pools = new Pool[2];
		Thread t1 = new Thread(new Runnable() {
			@Override
			public void run() {
				pools[0] = Registry.getComponent(Pool.class);
			}
		});

		Thread t2 = new Thread(new Runnable() {
			@Override
			public void run() {
				pools[1] = Registry.getComponent(Pool.class);
			}
		});

		t1.start();
		t2.start();
		t1.join();
		t2.join();
		assertSame(pools[0], pools[1]);
		assertSame(pools[1], Registry.getComponent(Pool.class));
	}

	@Test
	public void shouldInvokeInitOnLocalThreadPool() {
		Pool p = Registry.getComponent(Pool.class);
		if (!(p instanceof LocalThreadPool)) {
			fail("Dieser Test macht nur fuer LocalThreadPool Sinn!");
		}
		assertTrue(p.isRunning());
		p.shutdownNow();
		assertFalse(p.isRunning());
	}

	@Test
	public void workerShouldBePrototype() {
		Worker w1 = Registry.getComponent(Worker.class);
		Worker w2 = Registry.getComponent(Worker.class);
		assertNotSame(w1, w2);
	}

	@Test
	public void shouldSetMapAndCombinerTaskToMapRunner() {
		RunnerFactory factory = Registry.getComponent(RunnerFactory.class);
		MapTask mapTask = this.context.mock(MapTask.class);
		CombinerTask combinerTask = this.context.mock(CombinerTask.class);
		MapRunner mapRunner = factory.createMapRunner(mapTask, combinerTask);
		assertSame(mapTask, mapRunner.getMapTask());
		assertSame(combinerTask, mapRunner.getCombinerTask());
	}

	@Test
	public void shouldCreatePrototypesForMapRunners() {
		RunnerFactory factory = Registry.getComponent(RunnerFactory.class);
		MapTask mapTask = this.context.mock(MapTask.class);
		CombinerTask combinerTask = this.context.mock(CombinerTask.class);
		assertNotSame(factory.createMapRunner(mapTask, combinerTask), factory.createMapRunner(mapTask, combinerTask));
	}

	@Test
	public void shouldSetReduceTaskToReduceRunner() {
		RunnerFactory factory = Registry.getComponent(RunnerFactory.class);
		ReduceTask reduceTask = this.context.mock(ReduceTask.class);
		ReduceRunner reduceRunner = factory.createReduceRunner(reduceTask);
		assertSame(reduceTask, reduceRunner.getReduceTask());
	}

	@Test
	public void shouldCreatePrototypesForReduceRunners() {
		RunnerFactory factory = Registry.getComponent(RunnerFactory.class);
		ReduceTask reduceTask = this.context.mock(ReduceTask.class);
		assertNotSame(factory.createReduceRunner(reduceTask), factory.createReduceRunner(reduceTask));
	}
}
