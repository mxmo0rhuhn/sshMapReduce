package ch.zhaw.dna.ssh.mapreduce.model.framework.registry;

import com.google.inject.Provider;

public class LazyProvider<T> implements Provider<T> {

	private final Class<T> impl;

	public LazyProvider(Class<T> implClass) {
		this.impl = implClass;
	}

	@Override
	public T get() {
		try {
			return this.impl.newInstance();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
