package ch.zhaw.dna.ssh.mapreduce.model.framework.impl;

import ch.zhaw.dna.ssh.mapreduce.model.framework.MapReduceTask;
import ch.zhaw.dna.ssh.mapreduce.model.framework.ReduceRunner;
import ch.zhaw.dna.ssh.mapreduce.model.framework.ReduceRunnerFactory;
import ch.zhaw.dna.ssh.mapreduce.model.framework.ReduceTask;

public class PooledReduceRunnerFactory implements ReduceRunnerFactory {
	
	private ReduceTask reduceTask;
	
	private MapReduceTask master;
	
	@Override
	public void assignReduceTask(ReduceTask reduceTask) {
		this.reduceTask = reduceTask;
	}

	@Override
	public ReduceRunner create(String forKey) {
		return new PooledReduceRunner(forKey, this.reduceTask, this.master);
	}

	@Override
	public void setMaster (MapReduceTask master) {
		this.master = master;
	}

}
