package ch.zhaw.dna.ssh.mapreduce.model.framework.impl;

import java.util.List;
import java.util.concurrent.ConcurrentMap;

import ch.zhaw.dna.ssh.mapreduce.model.framework.MapRunner;
import ch.zhaw.dna.ssh.mapreduce.model.framework.PoolHelper;
import ch.zhaw.dna.ssh.mapreduce.model.framework.ReduceRunner;
import ch.zhaw.dna.ssh.mapreduce.model.framework.ReduceTask;

public class PooledReduceRunner implements ReduceRunner {

	private final String key;

	private final ReduceTask reduceTask;

	private final ConcurrentMap<String, List<String>> globalResultStructure;

	private final List<MapRunner> mapRunners;

	private volatile State curState = State.IDLE;

	public PooledReduceRunner(String key, ReduceTask reduceTask,
			ConcurrentMap<String, List<String>> globalResultStructure) {
		this.key = key;
		this.reduceTask = reduceTask;
		this.globalResultStructure = globalResultStructure;
	}

	@Override
	public void runReduceTask(List<MapRunner> mapRunners) {
		this.curState = State.INPROGRESS;
		this.mapRunners = mapRunners;
		PoolHelper.getPool().enqueueWork(this);
	}

	@Override
	public void emit(String result) {
		// TODO Auto-generated method stub

	}

	@Override
	public void doWork() {
		for (MapRunner mapRunner : mapRunners) {
			this.reduceTask.reduce(this, this.key, mapRunner.getIntermediate(this.key).iterator());
		}
	}

	@Override
	public State getCurrentState() {
		return this.curState;
	}

}
