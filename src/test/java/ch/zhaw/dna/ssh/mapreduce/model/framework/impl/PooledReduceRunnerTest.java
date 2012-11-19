package ch.zhaw.dna.ssh.mapreduce.model.framework.impl;

import ch.zhaw.dna.ssh.mapreduce.model.framework.MapReduceTask;

public class PooledReduceRunnerTest {

	@Test
	public void shouldSaveResultsInGlobalStructure() {
		PooledReduceRunner reduceRunner = new PooledReduceRunner();
		MapReduceTask master = new MapReduceTask(null, null);
		reduceRunner.setKey("hello");
		reduceRunner.setMaster(master);
		reduceRunner.emit("3");
		assertEquals(1, master.)
	}
	
}
