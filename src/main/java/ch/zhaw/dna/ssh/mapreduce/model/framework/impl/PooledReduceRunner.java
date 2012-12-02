package ch.zhaw.dna.ssh.mapreduce.model.framework.impl;

import java.util.List;

import javax.inject.Inject;

import ch.zhaw.dna.ssh.mapreduce.model.framework.MapRunner;
import ch.zhaw.dna.ssh.mapreduce.model.framework.Master;
import ch.zhaw.dna.ssh.mapreduce.model.framework.Pool;
import ch.zhaw.dna.ssh.mapreduce.model.framework.ReduceRunner;
import ch.zhaw.dna.ssh.mapreduce.model.framework.ReduceTask;

import com.google.inject.assistedinject.Assisted;

/**
 * Eine Implementation des ReduceRunner mit einem WorkerPool.
 * 
 * @author Reto
 * 
 */
public class PooledReduceRunner implements ReduceRunner {

	private final Pool pool;

	private final ReduceTask reduceTask;
	
	private final Master master;

	private String key;

	private List<MapRunner> mapRunners;

	private volatile State curState = State.INITIATED;

	@Inject
	public PooledReduceRunner(Pool pool, Master master, @Assisted ReduceTask reduceTask) {
		this.pool = pool;
		this.reduceTask = reduceTask;
		this.master = master;
	}

	/** {@inheritDoc} */
	@Override
	public void runReduceTask(List<MapRunner> mapRunners) {
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
		// this.reduceTask = task;
		throw new UnsupportedOperationException();
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
	public ReduceTask getReduceTask() {
		return this.reduceTask;
	}

}
