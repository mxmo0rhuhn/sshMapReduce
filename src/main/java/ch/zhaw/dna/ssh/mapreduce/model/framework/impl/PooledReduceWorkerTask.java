package ch.zhaw.dna.ssh.mapreduce.model.framework.impl;

import java.util.List;

import javax.inject.Inject;

import ch.zhaw.dna.ssh.mapreduce.model.framework.MapWorkerTask;
import ch.zhaw.dna.ssh.mapreduce.model.framework.Master;
import ch.zhaw.dna.ssh.mapreduce.model.framework.Pool;
import ch.zhaw.dna.ssh.mapreduce.model.framework.ReduceEmitter;
import ch.zhaw.dna.ssh.mapreduce.model.framework.ReduceWorkerTask;
import ch.zhaw.dna.ssh.mapreduce.model.framework.ReduceInstruction;

import com.google.inject.assistedinject.Assisted;

/**
 * Eine Implementation des ReduceRunner mit einem WorkerPool.
 * 
 * @author Reto
 * 
 */
public class PooledReduceWorkerTask implements ReduceWorkerTask, ReduceEmitter {

	private final Pool pool;

	private final Master master;

	private String key;

	private List<MapWorkerTask> mapRunners;

	private ReduceInstruction reduceTask;

	private volatile State curState = State.INITIATED;

	@Inject
	public PooledReduceWorkerTask(Pool pool, Master master) {
		this.pool = pool;
		this.master = master;
	}

	/** {@inheritDoc} */
	@Override
	public void runReduceTask(List<MapWorkerTask> mapRunners) {
		this.curState = State.INPROGRESS;
		this.mapRunners = mapRunners;
		this.pool.enqueueWork(this);
	}

	/** {@inheritDoc} */
	@Override
	public void emit(String result) {
		this.master.globalResultStructureAppend(key, result);
	}

	/** {@inheritDoc} */
	@Override
	public void doWork() {
		for (MapWorkerTask mapRunner : this.mapRunners) {
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
	@Inject
	public void setReduceTask(@Assisted ReduceInstruction task) {
		this.reduceTask = task;
	}

	/** {@inheritDoc} */
	@Override
	public void setKey(String key) {
		this.key = key;
	}

	/** {@inheritDoc} */
	@Override
	public String getKey() {
		return this.key;
	}

	/** {@inheritDoc} */
	@Override
	public ReduceInstruction getReduceTask() {
		return this.reduceTask;
	}

}
