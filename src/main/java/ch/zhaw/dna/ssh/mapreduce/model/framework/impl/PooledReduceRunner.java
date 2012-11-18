package ch.zhaw.dna.ssh.mapreduce.model.framework.impl;

import java.util.List;

import ch.zhaw.dna.ssh.mapreduce.model.framework.MapReduceTask;
import ch.zhaw.dna.ssh.mapreduce.model.framework.MapRunner;
import ch.zhaw.dna.ssh.mapreduce.model.framework.PoolHelper;
import ch.zhaw.dna.ssh.mapreduce.model.framework.ReduceRunner;
import ch.zhaw.dna.ssh.mapreduce.model.framework.ReduceTask;

public class PooledReduceRunner implements ReduceRunner {

	private String key;

	private ReduceTask reduceTask;

	private MapReduceTask master;

	private List<MapRunner> mapRunners;

	private volatile State curState = State.IDLE;

	@Override
	public void runReduceTask(List<MapRunner> mapRunners) {
		this.curState = State.INPROGRESS;
		this.mapRunners = mapRunners;
		PoolHelper.getPool().enqueueWork(this);
	}

	@Override
	public void emit(String result) {
		this.master.globalResultStructureAppend(key, result);
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

	@Override
	public void setReduceTask(ReduceTask task) {
		this.reduceTask = task;
	}

	@Override
	public void setKey(String key) {
		this.key = key;
	}

	@Override
	public void setMaster(MapReduceTask master) {
		this.master = master;
	}

}
