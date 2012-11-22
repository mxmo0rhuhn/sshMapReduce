package ch.zhaw.dna.ssh.mapreduce.model.framework.impl;

import ch.zhaw.dna.ssh.mapreduce.model.framework.CombinerTask;
import ch.zhaw.dna.ssh.mapreduce.model.framework.MapRunner;
import ch.zhaw.dna.ssh.mapreduce.model.framework.MapRunnerFactory;
import ch.zhaw.dna.ssh.mapreduce.model.framework.MapTask;

public class PooledMapRunnerFactory implements MapRunnerFactory {

	private CombinerTask combinerTask;
	private MapTask mapTask;

	/** {@inheritDoc} */
	@Override
	public void assignMapTask(MapTask task) {
		this.mapTask = task;
	}

	/** {@inheritDoc} */
	@Override
	public void assignCombineTask(CombinerTask task) {
		this.combinerTask = task;
	}

	/** {@inheritDoc} */
	@Override
	public MapRunner create() {
		MapRunner newMapRunner = new PooledMapRunner();
		newMapRunner.setMapTask(mapTask);
		if (combinerTask != null) {
			newMapRunner.setCombineTask(combinerTask);
		}
		return newMapRunner;
	}

}
