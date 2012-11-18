package ch.zhaw.dna.ssh.mapreduce.model.framework.impl;

import java.util.List;
import java.util.concurrent.ConcurrentMap;

import ch.zhaw.dna.ssh.mapreduce.model.framework.MapRunner;
import ch.zhaw.dna.ssh.mapreduce.model.framework.ReduceRunner;
import ch.zhaw.dna.ssh.mapreduce.model.framework.ReduceTask;

public class PooledReduceRunner implements ReduceRunner {
	
	private final String key;
	
	private final ReduceTask reduceTask;
	
	private final ConcurrentMap<String, List<String>> globalResultStructure;

	public PooledReduceRunner(String key, ReduceTask reduceTask,
			ConcurrentMap<String, List<String>> globalResultStructure) {
		this.key = key;
		this.reduceTask = reduceTask;
		this.globalResultStructure = globalResultStructure;
	}

	@Override
	public void reduce(List<MapRunner> mapRunners) {
		
	}

	@Override
	public void emit(String result) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isCompleted() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void doWork() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public State getCurrentState() {
		// TODO Auto-generated method stub
		return null;
	}

}
