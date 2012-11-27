package ch.zhaw.dna.ssh.mapreduce.model.framework.registry;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import ch.zhaw.dna.ssh.mapreduce.model.framework.MapRunnerFactory;
import ch.zhaw.dna.ssh.mapreduce.model.framework.Master;

public class RegistryTest {
	
	@Test
	public void shouldDefineBindingsForMapRunnerFactory() {
		assertNotNull(Registry.getComponent(MapRunnerFactory.class));
	}

	@Test
	public void shouldDefineBindingsForReduceRunnerFactory() {
		assertNotNull(Registry.getComponent(MapRunnerFactory.class));
	}
	
	@Test
	public void shouldDefineBindingForMaster() {
		assertNotNull(Registry.getComponent(Master.class));
	}

}
