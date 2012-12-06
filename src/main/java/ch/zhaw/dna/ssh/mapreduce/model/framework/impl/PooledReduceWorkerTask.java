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

	private final String mapReduceTaskUUID;

	private final String key;

	private final ReduceInstruction reduceInstruction;

	private List<KeyValuePair> input;

	private Worker processingWorker;

	private volatile State curState = State.INITIATED;

	@Inject
	public PooledReduceWorkerTask(Pool pool, @Assisted("uuid") String mapReduceTaskUUID, @Assisted("key") String key,
			@Assisted ReduceInstruction reduceInstruction) {
		this.pool = pool;
		this.mapReduceTaskUUID = mapReduceTaskUUID;
		this.key = key;
		this.reduceInstruction = reduceInstruction;
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

		this.reduceInstruction.reduce(this, key, input.iterator());
		this.curState = State.COMPLETED;
	}

	/** {@inheritDoc} */
	@Override
	public State getCurrentState() {
		return this.curState;
	}

	/** {@inheritDoc} */
	@Override
	public String getKey() {
		return this.key;
	}

	/** {@inheritDoc} */
	@Override
	public ReduceInstruction getReduceTask() {
		return this.reduceInstruction;
	}

	@Override
	public Worker getWorker() {
		return processingWorker;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getMapReduceTaskUUID() {
		return this.mapReduceTaskUUID;
	}
}
