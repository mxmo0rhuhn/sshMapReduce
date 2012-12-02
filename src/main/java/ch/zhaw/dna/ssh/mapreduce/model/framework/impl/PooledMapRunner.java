package ch.zhaw.dna.ssh.mapreduce.model.framework.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.inject.Inject;

import ch.zhaw.dna.ssh.mapreduce.model.framework.CombinerTask;
import ch.zhaw.dna.ssh.mapreduce.model.framework.MapRunner;
import ch.zhaw.dna.ssh.mapreduce.model.framework.MapTask;
import ch.zhaw.dna.ssh.mapreduce.model.framework.Pool;

import com.google.inject.assistedinject.Assisted;

/**
 * Eine Implementation des MapRunners mit einem WorkerPool.
 * 
 * @author Max
 */
public class PooledMapRunner implements MapRunner {
	
	private final Pool pool;

	// Der Zustand in dem sich der Worker befindet
	private volatile State currentState = State.INITIATED;

	// Aufgabe, die der Task derzeit ausführt
	private final MapTask mapTask;

	// Das Limit für die Anzahl an neuen Zwischenergebnissen die gewartet werden soll, bis der Combiner ausgeführt wird.
	private volatile int maxWaitResults;

	// Die Anzahl an neuen Zwischenergebnissen seit dem letzten Combiner.
	private volatile int newResults;

	// Falls vorhanden ein Combiner für die Zwischenergebnisse
	private final CombinerTask combinerTask;

	// Die derzeit zu bearbeitenden Daten
	private volatile String toDo;

	// Ergebnisse von auf dem Worker ausgeführten MAP Tasks
	private final ConcurrentMap<String, List<String>> results = new ConcurrentHashMap<String, List<String>>();
	
	@Inject
	public PooledMapRunner(Pool pool, @Assisted MapTask mapTask, @Assisted CombinerTask combinerTask) {
		this.pool = pool;
		this.mapTask = mapTask;
		this.combinerTask = combinerTask;
	}

	/** {@inheritDoc} */
	@Override
	public void emitIntermediateMapResult(String key, String value) {
		if (!this.results.containsKey(key)) {
			this.results.putIfAbsent(key, new LinkedList<String>());
		}
		List<String> curValues = this.results.get(key);
		curValues.add(value);

		this.newResults++;
		

		if (this.combinerTask != null) {
			if (this.newResults >= this.maxWaitResults) {
				for (Entry<String, List<String>> entry : this.results.entrySet()) {
					ArrayList<String> resultList = new ArrayList<String>();
					resultList.add(this.combinerTask.combine(entry.getValue().iterator()));
					this.results.put(entry.getKey(), resultList);
				}
			}
		}
	}

	/** {@inheritDoc} */
	@Override
	public List<String> getIntermediate(String key) {
		return this.results.remove(key);
	}

	/** {@inheritDoc} */
	@Override
	public void runMapTask(String input) {
		this.toDo = input;
		this.currentState = State.ENQUEUED;
		this.pool.enqueueWork(this);
	}

	/** {@inheritDoc} */
	@Override
	public int getMaxWaitResults() {
		return this.maxWaitResults;
	}

	/** {@inheritDoc} */
	@Override
	public void setMaxWaitResults(int maxWaitResults) {
		this.maxWaitResults = maxWaitResults;
	}

	/**
	 *  {@inheritDoc} 
	 *  Diese Angabe ist optimistisch. Sie kann veraltet sein.
	 */
	@Override
	public State getCurrentState() {
		return this.currentState;
	}

	/** {@inheritDoc} */
	@Override
	public void doWork() {
		try {
			this.mapTask.map(this, toDo);
			this.currentState = State.COMPLETED;
		} catch (Throwable t) {
			this.currentState = State.FAILED;
		}
	}

	/** {@inheritDoc} */
	@Override
	public List<String> getKeysSnapshot() {
		return Collections.unmodifiableList(new ArrayList<String>(this.results.keySet()));
	}

	/** {@inheritDoc} */
	@Override
	public void setMapTask(MapTask task) {
		//this.mapTask = task;
		throw new UnsupportedOperationException();
	}

	/** {@inheritDoc} */
	@Override
	public void setCombineTask(CombinerTask task) {
		//this.combinerTask = task;
		throw new UnsupportedOperationException();
	}

	/** {@inheritDoc} */
	@Override
	public CombinerTask getCombinerTask() {
		return this.combinerTask;
	}

	/** {@inheritDoc} */
	@Override
	public MapTask getMapTask() {
		return this.mapTask;
	}
}
