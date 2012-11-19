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

	/** {@inheritDoc} */
	@Override
	public void runReduceTask(List<MapRunner> mapRunners) {
		this.curState = State.INPROGRESS;
		this.mapRunners = mapRunners;
		PoolHelper.getPool().enqueueWork(this);
	}

	/** {@inheritDoc} */
	@Override
	public void emit(String result) {
		this.master.globalResultStructureAppend(key, result);
	}

	/** {@inheritDoc} */
	@Override
	public void doWork() {
		for (MapRunner mapRunner : this.mapRunners) {
			List<String> intermediate = mapRunner.getIntermediate(this.key);
			if (intermediate != null) {
				this.reduceTask.reduce(this, this.key, intermediate.iterator());
			}
		}
	}

	/** {@inheritDoc} */
	@Override
	public State getCurrentState() {
		return this.curState;
	}

	/** {@inheritDoc} */
	@Override
	public void setReduceTask(ReduceTask task) {
		this.reduceTask = task;
	}

	/** {@inheritDoc} */
	@Override
	public void setKey(String key) {
		this.key = key;
	}

	/** {@inheritDoc} */
	@Override
	public void setMaster(MapReduceTask master) {
		this.master = master;
	}

}
