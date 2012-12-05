package ch.zhaw.dna.ssh.mapreduce.model.framework.impl;

import java.util.List;

import javax.inject.Inject;

import ch.zhaw.dna.ssh.mapreduce.model.framework.KeyValuePair;
import ch.zhaw.dna.ssh.mapreduce.model.framework.Pool;
import ch.zhaw.dna.ssh.mapreduce.model.framework.ReduceEmitter;
import ch.zhaw.dna.ssh.mapreduce.model.framework.ReduceInstruction;
import ch.zhaw.dna.ssh.mapreduce.model.framework.ReduceWorkerTask;
import ch.zhaw.dna.ssh.mapreduce.model.framework.Worker;

import com.google.inject.assistedinject.Assisted;

/**
 * Eine Implementation des ReduceRunner mit einem WorkerPool.
 * 
 * @author Reto
 * 
 */
public class PooledReduceWorkerTask implements ReduceWorkerTask, ReduceEmitter {

	private final Pool pool;

	private String key;

	private List<KeyValuePair> input;

	private ReduceInstruction reduceTask;

	private volatile State curState = State.INITIATED;

	private Worker processingWorker;
	

	@Inject
	public PooledReduceWorkerTask(Pool pool) {
		this.pool = pool;
	}

	/** {@inheritDoc} */
	@Override
	public void runReduceTask(List<KeyValuePair> keyValues) {
		this.curState = State.INPROGRESS;
		this.input = keyValues;
		this.pool.enqueueWork(this);
	}

	/** {@inheritDoc} */
	@Override
	public void emit(String result) {
		this.processingWorker.storeKeyValuePair(key, result, result);
	}

	/** {@inheritDoc} */
	@Override
	public void doWork(Worker worker) {
		this.processingWorker = worker;

		this.reduceTask.reduce(this, key, input.iterator());
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

	@Override
	public Worker getWorker() {
		return processingWorker;
	}

}
