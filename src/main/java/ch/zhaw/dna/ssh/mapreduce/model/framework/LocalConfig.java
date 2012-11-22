package ch.zhaw.dna.ssh.mapreduce.model.framework;

import java.lang.reflect.Constructor;

import ch.zhaw.dna.ssh.mapreduce.model.framework.impl.PooledMapRunnerFactory;
import ch.zhaw.dna.ssh.mapreduce.model.framework.impl.PooledReduceRunnerFactory;

import com.google.inject.AbstractModule;

public final class LocalConfig extends AbstractModule {

	@Override
	protected void configure() {
		bind(ReduceRunnerFactory.class).to(PooledReduceRunnerFactory.class);
		bind(MapRunnerFactory.class).to(PooledMapRunnerFactory.class);

		try {
			bind(Master.class).toConstructor(createMasterType());
		} catch (Exception e) {
			addError(e);
		}
	}

	private static Constructor<Master> createMasterType() throws Exception {
		return Master.class.getConstructor(MapRunnerFactory.class, ReduceRunnerFactory.class);
	}

}
