package ch.zhaw.dna.ssh.mapreduce.model.framework.impl;

import java.util.List;
import java.util.concurrent.ConcurrentMap;

import ch.zhaw.dna.ssh.mapreduce.model.framework.ReduceRunner;
import ch.zhaw.dna.ssh.mapreduce.model.framework.ReduceRunnerFactory;
import ch.zhaw.dna.ssh.mapreduce.model.framework.ReduceTask;

public class PooledReduceRunnerFactory implements ReduceRunnerFactory {
	
	private ReduceTask reduceTask;
	
	private ConcurrentMap<String, List<String>> globalResultStructure;
	
	@Override
	public void assignReduceTask(ReduceTask reduceTask) {
		this.reduceTask = reduceTask;
	}

	@Override
	public ReduceRunner create(String forKey) {
		return new PooledReduceRunner(forKey, this.reduceTask, this.globalResultStructure);
	}

	@Override
	public void setGlobalResultStructure(ConcurrentMap<String, List<String>> globalResultStructure) {
		this.globalResultStructure = globalResultStructure;
	}

}
