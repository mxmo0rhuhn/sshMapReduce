package ch.zhaw.dna.ssh.mapreduce.model.framework.impl;

import javax.inject.Inject;

import ch.zhaw.dna.ssh.mapreduce.model.framework.CombinerInstruction;
import ch.zhaw.dna.ssh.mapreduce.model.framework.MapEmitter;
import ch.zhaw.dna.ssh.mapreduce.model.framework.MapInstruction;
import ch.zhaw.dna.ssh.mapreduce.model.framework.MapWorkerTask;
import ch.zhaw.dna.ssh.mapreduce.model.framework.Pool;
import ch.zhaw.dna.ssh.mapreduce.model.framework.Worker;

import com.google.inject.assistedinject.Assisted;

/**
 * Eine Implementation des MapRunners mit einem WorkerPool.
 * 
 * @author Max
 */

public class PooledMapWorkerTask implements MapWorkerTask, MapEmitter {

	private final Pool pool;

	// Der Zustand in dem sich der Worker befindet
	private volatile State currentState = State.INITIATED;

	// Aufgabe, die der Task derzeit ausführt
	private MapInstruction mapTask;

	// Falls vorhanden ein Combiner für die Zwischenergebnisse
	private CombinerInstruction combinerTask;

	// Die derzeit zu bearbeitenden Daten
	private String toDo;

	// Die eindeutihe ID die jeder input besitzt
	private String inputUID;

	// Eine eindeutige ID die jeder Map Reduce Task besitzt
	private final String mapReduceTaskUID;

	// Der den Task ausführende Worker
	private Worker processingWorker;

	@Inject
	public PooledMapWorkerTask(String mapReduceTaskUID, Pool pool) {
		this.pool = pool;
		this.mapReduceTaskUID = mapReduceTaskUID;

	}

	/** {@inheritDoc} */
	@Override
	public void emitIntermediateMapResult(String key, String value) {
		processingWorker.storeKeyValuePair(mapReduceTaskUID, key, value);
	}

	/** {@inheritDoc} */
	@Override
	public void runMapTask(String inputUID, String input) {
		this.toDo = input;
		this.inputUID = inputUID;
		this.currentState = State.ENQUEUED;
		this.pool.enqueueWork(this);
	}

	/**
	 * {@inheritDoc} Diese Angabe ist optimistisch. Sie kann veraltet sein.
	 */
	@Override
	public State getCurrentState() {
		return this.currentState;
	}

	/** {@inheritDoc} */
	@Override
	public void doWork(Worker processingWorker) {
		try {
			this.processingWorker = processingWorker;

			// Mappen
			this.mapTask.map(this, toDo);

			// Alle Ergebnisse verdichten. Die Ergebnisse aus der derzeitigen Worker sollen
			// einbezogen werden.
			this.combinerTask.combine(processingWorker.getStoredKeyValuePairs(mapReduceTaskUID)
					.iterator());

			this.currentState = State.COMPLETED;
		} catch (Throwable t) {
			this.currentState = State.FAILED;
		}
	}

	/** {@inheritDoc} */
	@Override
	@Inject
	public void setMapTask(@Assisted MapInstruction task) {
		this.mapTask = task;
	}

	/** {@inheritDoc} */
	@Override
	@Inject
	public void setCombineTask(@Assisted CombinerInstruction task) {
		this.combinerTask = task;
	}

	@Override
	public Worker getWorker() {
		return processingWorker;
	}

	@Override
	public String getCurrentInputUID() {
		return inputUID;
	}
}
