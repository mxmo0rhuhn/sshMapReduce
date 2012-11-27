package ch.zhaw.dna.ssh.mapreduce.model.framework.impl;

import java.util.List;

import ch.zhaw.dna.ssh.mapreduce.model.framework.MapRunner;
import ch.zhaw.dna.ssh.mapreduce.model.framework.Master;
import ch.zhaw.dna.ssh.mapreduce.model.framework.PoolHelper;
import ch.zhaw.dna.ssh.mapreduce.model.framework.ReduceEmitter;
import ch.zhaw.dna.ssh.mapreduce.model.framework.ReduceRunner;
import ch.zhaw.dna.ssh.mapreduce.model.framework.ReduceTask;

/**
 * Eine Implementation des ReduceRunner mit einem WorkerPool.
 * 
 * @author Reto
 *
 */
public class PooledReduceRunner implements ReduceRunner, ReduceEmitter {

	private String key;

	private ReduceTask reduceTask;

	private Master master;

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
		this.curState = State.COMPLETED;
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
	public void setMaster(Master master) {
		this.master = master;
	}

	/** {@inheritDoc} */
	@Override
	public String getKey() {
		return this.key;
	}

	/** {@inheritDoc} */
	@Override
	public ReduceTask getReduceTask() {
		return this.reduceTask;
	}

}
