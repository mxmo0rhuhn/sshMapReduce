package ch.zhaw.dna.ssh.mapreduce.model.framework.registry;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.util.Map.Entry;
import java.util.Properties;

import ch.zhaw.dna.ssh.mapreduce.model.framework.MapRunnerFactory;
import ch.zhaw.dna.ssh.mapreduce.model.framework.Master;
import ch.zhaw.dna.ssh.mapreduce.model.framework.ReduceRunnerFactory;

import com.google.inject.AbstractModule;
import com.google.inject.Binder;
import com.google.inject.name.Names;

public class PropertiesModule extends AbstractModule {

	private static final String BINDINGS_FILE = "ch/zhaw/dna/ssh/mapreduce/model/framework/bindings.properties";

	@Override
	protected void configure() {
		initPropertyBindings();

		try {
			bind(Master.class).toConstructor(createMasterType());
		} catch (Exception e) {
			addError(e);
		}
	}

	private static Constructor<Master> createMasterType() throws Exception {
		return Master.class.getConstructor(MapRunnerFactory.class, ReduceRunnerFactory.class);
	}

	private void initPropertyBindings() {
		Properties props = loadProperties();
		makeNamedBindings(props);
		bindProviders(props);
	}

	private Properties loadProperties() {
		Properties props = new Properties();
		InputStream is = getClass().getClassLoader().getResourceAsStream(BINDINGS_FILE);

		if (is == null) {
			addError(new FileNotFoundException("Bindings file not found: " + BINDINGS_FILE));
		}
		try {
			props.load(is);
			return props;
		} catch (IOException io) {
			addError(io);
		}
		return null;
	}
	
	private void makeNamedBindings(Properties props) {
		Names.bindProperties(binder(), props);
	}

	private void bindProviders(Properties properties) {
		for (Entry<Object, Object> entry : properties.entrySet()) {
			String ifc = (String) entry.getKey();
			String impl = (String) entry.getValue();
			bindIfClass(ifc, impl);
		}
	}

	private void bindIfClass(String ifcName, String implName) {
		try {
			Class<?> ifc = Class.forName(ifcName);
			addBinding(ifc, implName);
		} catch (Exception e) {
			// keine klasse
		}
	}

	private <T> void addBinding(Class<T> ifc, String impl) {
		Binder binder = binder();
		try {
			@SuppressWarnings("unchecked")
			Class<T> implClass = (Class<T>) Class.forName(impl);
			binder.bind(ifc).toProvider(new LazyProvider<T>(implClass));
		} catch (ClassNotFoundException e) {
			binder.addError("Failed to load class %s", impl);
		}
	}
}
