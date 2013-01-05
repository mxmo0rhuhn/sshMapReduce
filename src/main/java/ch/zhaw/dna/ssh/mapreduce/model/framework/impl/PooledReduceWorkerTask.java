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

	/**
	 * Der verwendete Pool
	 */
	private final Pool pool;

	/**
	 * Die globale MapReduce-Berechnungs-ID zu der dieser Task gehoert
	 */
	private final String mapReduceTaskUUID;

	/**
	 * Fuer diesen Key wollen wir reduzieren
	 */
	private final String key;

	/**
	 * Diese ReduceInstruction wird angewendet
	 */
	private final ReduceInstruction reduceInstruction;

	/**
	 * Der zu reduzierende Input
	 */
	private final List<KeyValuePair> input;

	/**
	 * Dieser Worker arbeitet daran. Wenn null arbeitet gerade keiner dran. Wenn der Task erfolgreich abgschlossen ist,
	 * steht hier der Worker, der den Task ausgefuert hat. Wenn der Task nicht erfolgreich beendet werden konnte, steht
	 * hier wieder null.
	 * 
	 * Dieses Feld ist nicht volatile, da es entweder vom gleichen Thread gelesen wird um Werte zu speichern, dir die
	 * Werte speichern will, oder dann nur im Zusammenhang mit {{@link #getCurrentState()}, welches volatile ist.
	 */
	private Worker processingWorker;

	/**
	 * Der momentane Status
	 */
	private volatile State curState = State.INITIATED;

	@Inject
	public PooledReduceWorkerTask(Pool pool, @Assisted("uuid") String mapReduceTaskUUID, @Assisted("key") String key,
			@Assisted ReduceInstruction reduceInstruction, @Assisted List<KeyValuePair> toDo) {
		this.pool = pool;
		this.mapReduceTaskUUID = mapReduceTaskUUID;
		this.key = key;
		this.reduceInstruction = reduceInstruction;
		this.input = toDo;
	}

	/** {@inheritDoc} */
	@Override
	public boolean runReduceTask() {
		this.curState = State.ENQUEUED;
		return this.pool.enqueueWork(this);
	}

	/** {@inheritDoc} */
	@Override
	public void emit(String result) {
		this.processingWorker.storeKeyValuePair(this.mapReduceTaskUUID, this.key, result);
	}

	/** {@inheritDoc} */
	@Override
	public void doWork(Worker worker) {
		this.curState = State.INPROGRESS;
		this.processingWorker = worker;

		try {
			this.reduceInstruction.reduce(this, key, input.iterator());
			this.curState = State.COMPLETED;
		} catch (Exception e) {
			this.curState = State.FAILED;
			this.processingWorker = null;
		}
	}

	/** {@inheritDoc} */
	@Override
	public State getCurrentState() {
		return this.curState;
	}

	/** {@inheritDoc} */
	@Override
	public String getUUID() {
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
