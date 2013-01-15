package ch.zhaw.dna.ssh.mapreduce.controller;

import org.junit.Test;

import static org.junit.Assert.*;

import ch.zhaw.mapreduce.Pool;
import ch.zhaw.mapreduce.registry.Registry;

public class ProjectLauncherTest {
	
	@Test
	public void shouldCreateSpecifiedNumberOfWorkers() {
		ProjectLauncher launcher = new ProjectLauncher();
		launcher.launch(5);
		assertEquals(5, Registry.getComponent(Pool.class).getCurrentPoolSize());
	}

}
