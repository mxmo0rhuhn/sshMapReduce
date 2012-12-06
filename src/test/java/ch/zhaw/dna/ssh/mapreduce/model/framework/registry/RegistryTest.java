package ch.zhaw.dna.ssh.mapreduce.model.framework.registry;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

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
import com.google.inject.Provides;
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
			
			@Provides
			@PoolExecutor
			@SuppressWarnings("unused") // guice will
			public Executor poolExec() {
				return Executors.newSingleThreadExecutor();
			}

		}).getInstance(Pool.class);
		assertTrue(p.isRunning());
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
		MapWorkerTask mapWorkerTask = factory.createMapWorkerTask("uuid", mapTask, combinerTask);
		assertSame(mapTask, mapWorkerTask.getMapInstruction());
		assertSame(combinerTask, mapWorkerTask.getCombinerInstruction());
	}

	@Test
	public void shouldCopeWithNullCombinerTask() {
		WorkerTaskFactory factory = Registry.getComponent(WorkerTaskFactory.class);
		MapInstruction mapTask = this.context.mock(MapInstruction.class);
		MapWorkerTask mapperTask = factory.createMapWorkerTask("uuid", mapTask, null);
		assertNotNull(mapperTask);
	}

	@Test
	public void shouldCreatePrototypesForMapRunners() {
		WorkerTaskFactory factory = Registry.getComponent(WorkerTaskFactory.class);
		MapInstruction mapTask = this.context.mock(MapInstruction.class);
		CombinerInstruction combinerTask = this.context.mock(CombinerInstruction.class);
		assertNotSame(factory.createMapWorkerTask("uuid1", mapTask, combinerTask),
				factory.createMapWorkerTask("uuid2", mapTask, combinerTask));
	}

	@Test
	public void shouldSetReduceTaskToReduceRunner() {
		WorkerTaskFactory factory = Registry.getComponent(WorkerTaskFactory.class);
		ReduceInstruction reduceTask = this.context.mock(ReduceInstruction.class);
		ReduceWorkerTask reduceRunner = factory.createReduceWorkerTask("uuid", "key", reduceTask);
		assertSame(reduceTask, reduceRunner.getReduceTask());
	}

	@Test
	public void shouldCreatePrototypesForReduceRunners() {
		WorkerTaskFactory factory = Registry.getComponent(WorkerTaskFactory.class);
		ReduceInstruction reduceTask = this.context.mock(ReduceInstruction.class);
		assertNotSame(factory.createReduceWorkerTask("uuid1", "key1", reduceTask), factory.createReduceWorkerTask("uuid2", "key2", reduceTask));
	}
	
	@Test
	public void shouldSetUUIDToMapTask() {
		WorkerTaskFactory factory = Registry.getComponent(WorkerTaskFactory.class);
		MapWorkerTask mwt = factory.createMapWorkerTask("uuid", this.context.mock(MapInstruction.class), null);
		assertEquals("uuid", mwt.getMapReduceTaskUUID());
	}
	
	@Test
	public void shouldSetUUIDToReduceTask() {
		WorkerTaskFactory factory = Registry.getComponent(WorkerTaskFactory.class);
		ReduceWorkerTask mwt = factory.createReduceWorkerTask("uuid", "key", this.context.mock(ReduceInstruction.class));
		assertEquals("uuid", mwt.getMapReduceTaskUUID());
	}
	
	@Test
	public void shouldSetKeyToReduceTask() {
		WorkerTaskFactory factory = Registry.getComponent(WorkerTaskFactory.class);
		ReduceWorkerTask mwt = factory.createReduceWorkerTask("uuid", "key", this.context.mock(ReduceInstruction.class));
		assertEquals("key", mwt.getKey());
	}

	@Test
	public void shouldProvideDifferentUUIDEveryTime() {
		Master m1 = Registry.getComponent(Master.class);
		Master m2 = Registry.getComponent(Master.class);
		assertFalse(m1.getMapReduceTaskUUID().equals(m2.getMapReduceTaskUUID()));
	}
}
