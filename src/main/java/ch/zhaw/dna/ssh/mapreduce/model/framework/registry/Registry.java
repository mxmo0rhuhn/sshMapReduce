package ch.zhaw.dna.ssh.mapreduce.model.framework.registry;

import com.google.inject.Guice;
import com.google.inject.Injector;

public final class Registry {

	private static final Injector INJECTOR = Guice.createInjector(new PropertiesModule());

	private Registry() {

	}

	public static <T> T getComponent(Class<T> type) {
		return INJECTOR.getInstance(type);
	}
}