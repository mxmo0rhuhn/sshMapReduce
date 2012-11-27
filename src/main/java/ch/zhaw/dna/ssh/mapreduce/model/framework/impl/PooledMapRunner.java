package ch.zhaw.dna.ssh.mapreduce.model.framework.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import ch.zhaw.dna.ssh.mapreduce.model.framework.CombinerTask;
import ch.zhaw.dna.ssh.mapreduce.model.framework.MapRunner;
import ch.zhaw.dna.ssh.mapreduce.model.framework.MapTask;
import ch.zhaw.dna.ssh.mapreduce.model.framework.PoolHelper;

/**
 * Eine Implementation des MapRunners mit einem WorkerPool.
 * 
 * @author Max
 */
public class PooledMapRunner implements MapRunner {

	// Der Zustand in dem sich der Worker befindet
	private State currentState = State.IDLE;

	// Ergebnisse von auf dem Worker ausgeführten MAP Tasks
	private Map<String, List<String>> results = new HashMap<String, List<String>>();

	// Aufgabe, die der Task derzeit ausführt
	private MapTask mapTask;

	// Das Limit für die Anzahl an neuen Zwischenergebnissen die gewartet werden soll, bis der Combiner ausgeführt wird.
	private int maxWaitResults;

	// Die Anzahl an neuen Zwischenergebnissen seit dem letzten Combiner.
	private int newResults;

	// Falls vorhanden ein Combiner für die Zwischenergebnisse
	private CombinerTask combinerTask;

	// Die derzeit zu bearbeitenden Daten
	private String toDo;

	/** {@inheritDoc} */
	@Override
	public synchronized void emitIntermediateMapResult(String key, String value) {
		if (!this.results.containsKey(key)) {
			this.results.put(key, new LinkedList<String>());
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
	public synchronized List<String> getIntermediate(String key) {
		if (!results.isEmpty()) {
			List<String> returnResults = this.results.get(key);
			this.results.remove(key);
			return returnResults;
		}
		return null;
	}

	/** {@inheritDoc} */
	@Override
	public synchronized void runMapTask(String input) {
		this.currentState = State.INPROGRESS;
		this.toDo = input;
		PoolHelper.getPool().enqueueWork(this);
	}

	/** {@inheritDoc} */
	@Override
	public synchronized int getMaxWaitResults() {
		return this.maxWaitResults;
	}

	/** {@inheritDoc} */
	@Override
	public synchronized void setMaxWaitResults(int maxWaitResults) {
		this.maxWaitResults = maxWaitResults;
	}

	/** {@inheritDoc} */
	@Override
	public synchronized State getCurrentState() {
		return this.currentState;
	}

	/** {@inheritDoc} */
	@Override
	public synchronized void doWork() {
		this.mapTask.map(this, toDo);
		this.currentState = State.COMPLETED;
	}

	/** {@inheritDoc} */
	@Override
	public synchronized List<String> getKeysSnapshot() {
		return Collections.unmodifiableList(new ArrayList<String>(this.results.keySet()));
	}

	/** {@inheritDoc} */
	@Override
	public synchronized void setMapTask(MapTask task) {
		this.mapTask = task;
	}

	/** {@inheritDoc} */
	@Override
	public synchronized void setCombineTask(CombinerTask task) {
		this.combinerTask = task;

	}

	/** {@inheritDoc} */
	@Override
	public synchronized CombinerTask getCombinerTask() {
		return this.combinerTask;
	}
	
	/** {@inheritDoc} */
	@Override
	public synchronized MapTask getMapTask() {
		return this.mapTask;
	}
}
