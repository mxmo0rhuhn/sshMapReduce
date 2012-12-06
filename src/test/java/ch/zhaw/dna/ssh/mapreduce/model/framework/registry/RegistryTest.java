package ch.zhaw.dna.ssh.mapreduce.model.framework.registry;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.zhaw.dna.ssh.mapreduce.model.framework.CombinerInstruction;
import ch.zhaw.dna.ssh.mapreduce.model.framework.MapInstruction;
import ch.zhaw.dna.ssh.mapreduce.model.framework.MapWorkerTask;
import ch.zhaw.dna.ssh.mapreduce.model.framework.Master;
import ch.zhaw.dna.ssh.mapreduce.model.framework.Pool;
import ch.zhaw.dna.ssh.mapreduce.model.framework.ReduceInstruction;
import ch.zhaw.dna.ssh.mapreduce.model.framework.ReduceWorkerTask;
import ch.zhaw.dna.ssh.mapreduce.model.framework.Worker;
import ch.zhaw.dna.ssh.mapreduce.model.framework.WorkerTaskFactory;
import ch.zhaw.dna.ssh.mapreduce.model.framework.impl.LocalThreadPool;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.matcher.Matchers;

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
		// hier wird explizit ein neuer injector kreiert, da der pool im kontext von guice ein singleton ist und wir
		// sonst nicht sicherstellen koennen, dass hier das erste mal eine instanz vom pool angefordert wird. dies ist
		// aber notwendig, da die init method nur beim ersten mal aufgerufen wird.
		Pool p = Guice.createInjector(new AbstractModule() {

			@Override
			protected void configure() {
				bind(Pool.class).to(LocalThreadPool.class);
				bindListener(Matchers.any(), new PostConstructFeature());
			}

		}).getInstance(Pool.class);
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
		WorkerTaskFactory factory = Registry.getComponent(WorkerTaskFactory.class);
		MapInstruction mapTask = this.context.mock(MapInstruction.class);
		CombinerInstruction combinerTask = this.context.mock(CombinerInstruction.class);
		MapWorkerTask mapRunner = factory.createMapWorkerTask(mapTask, combinerTask);
		assertSame(mapTask, mapRunner.getMapTask());
		assertSame(combinerTask, mapRunner.getCombinerTask());
	}
	
	@Test
	public void shouldCopeWithNullCombinerTask() {
		WorkerTaskFactory factory = Registry.getComponent(WorkerTaskFactory.class);
		MapInstruction mapTask = this.context.mock(MapInstruction.class);
		MapWorkerTask mapperTask = factory.createMapWorkerTask(mapTask, null);
		assertNotNull(mapperTask);
	}

	@Test
	public void shouldCreatePrototypesForMapRunners() {
		WorkerTaskFactory factory = Registry.getComponent(WorkerTaskFactory.class);
		MapInstruction mapTask = this.context.mock(MapInstruction.class);
		CombinerInstruction combinerTask = this.context.mock(CombinerInstruction.class);
		assertNotSame(factory.createMapWorkerTask(mapTask, combinerTask), factory.createMapWorkerTask(mapTask, combinerTask));
	}

	@Test
	public void shouldSetReduceTaskToReduceRunner() {
		WorkerTaskFactory factory = Registry.getComponent(WorkerTaskFactory.class);
		ReduceInstruction reduceTask = this.context.mock(ReduceInstruction.class);
		ReduceWorkerTask reduceRunner = factory.createReduceWorkerTask(reduceTask);
		assertSame(reduceTask, reduceRunner.getReduceTask());
	}

	@Test
	public void shouldCreatePrototypesForReduceRunners() {
		WorkerTaskFactory factory = Registry.getComponent(WorkerTaskFactory.class);
		ReduceInstruction reduceTask = this.context.mock(ReduceInstruction.class);
		assertNotSame(factory.createReduceWorkerTask(reduceTask), factory.createReduceWorkerTask(reduceTask));
	}
}