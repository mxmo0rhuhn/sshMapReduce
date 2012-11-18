package ch.zhaw.dna.ssh.mapreduce.model.framework.impl;

import ch.zhaw.dna.ssh.mapreduce.model.framework.CombinerTask;
import ch.zhaw.dna.ssh.mapreduce.model.framework.MapRunner;
import ch.zhaw.dna.ssh.mapreduce.model.framework.MapRunnerFactory;
import ch.zhaw.dna.ssh.mapreduce.model.framework.MapTask;

public class PooledMapRunnerFactory implements MapRunnerFactory {

	private CombinerTask combinerTask;
	private MapTask mapTask;

	@Override
	public void assignMapTask(MapTask task) {
		this.mapTask = task;
	}

	@Override
	public void assignCombineTask(CombinerTask task) {
		this.combinerTask = task;
	}

	@Override
	public MapRunner getMapRunner() {
		if (combinerTask != null) {
			return new PooledMapRunner(mapTask, combinerTask);
		} else {
			return new PooledMapRunner(mapTask);
		}
	}

}
