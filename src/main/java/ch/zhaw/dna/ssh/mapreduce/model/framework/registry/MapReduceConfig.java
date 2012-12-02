package ch.zhaw.dna.ssh.mapreduce.model.framework.registry;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ch.zhaw.dna.ssh.mapreduce.model.framework.MapRunner;
import ch.zhaw.dna.ssh.mapreduce.model.framework.Master;
import ch.zhaw.dna.ssh.mapreduce.model.framework.Pool;
import ch.zhaw.dna.ssh.mapreduce.model.framework.ReduceRunner;
import ch.zhaw.dna.ssh.mapreduce.model.framework.RunnerFactory;
import ch.zhaw.dna.ssh.mapreduce.model.framework.Worker;
import ch.zhaw.dna.ssh.mapreduce.model.framework.impl.LocalThreadPool;
import ch.zhaw.dna.ssh.mapreduce.model.framework.impl.PooledMapRunner;
import ch.zhaw.dna.ssh.mapreduce.model.framework.impl.PooledReduceRunner;
import ch.zhaw.dna.ssh.mapreduce.model.framework.impl.ThreadWorker;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.matcher.Matchers;

/**
 * In dieser Klasse befinden sich die Bindings für Guice - also welche Implementationen für welche Interfaces verwendet
 * werden sollen.
 * 
 * @author Reto
 * 
 */
class MapReduceConfig extends AbstractModule {

	/**
	 * Binded verschiedene Interfaces zu den zugehörigen Implementationen.
	 */
	@Override
	protected void configure() {

		// AssistedInject Magic: Mit diesem FactoryModuleBuilder wird ein Binding für die RunnerFactory erstellt ohne,
		// dass wir eine tatsächliche Implementation bereitstellen.
		install(new FactoryModuleBuilder().implement(MapRunner.class, PooledMapRunner.class)
				.implement(ReduceRunner.class, PooledReduceRunner.class).build(RunnerFactory.class));

		bind(Worker.class).to(ThreadWorker.class);
		bind(Pool.class).to(LocalThreadPool.class);

		// Master soll einfach von Guice verwaltet werden. Ohne Interface
		bind(Master.class);

		// see PostConstructFeature
		bindListener(Matchers.any(), new PostConstructFeature());
	}

	/**
	 * Provided eine Implementation für das Interface ExecutorService, welches mit der Annotation SingleThreaded
	 * annotiert ist.
	 * 
	 */
	@Provides
	@SingleThreaded
	public ExecutorService createSingle() {
		return Executors.newSingleThreadExecutor();
	}
}
