package ch.zhaw.dna.ssh.mapreduce.model.framework.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	private volatile State currentState = State.IDLE;

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
	public void emitIntermediateMapResult(String key, String value) {
		synchronized (this) {

			if (this.results.containsKey(key)) {
				List<String> curList = this.results.get(key);
				curList.add(value);
				results.put(key, curList);

			} else {
				ArrayList<String> resultList = new ArrayList<String>();
				resultList.add(value);
				this.results.put(key, resultList);
			}

			this.newResults++;

			if (this.combinerTask != null) {
				if (this.newResults >= this.maxWaitResults) {
					for (String currentKey : results.keySet()) {
						ArrayList<String> resultList = new ArrayList<String>();
						resultList.add(this.combinerTask.combine(results.get(currentKey).iterator()));
						this.results.put(currentKey, resultList);
					}
				}
			}
		}
	}

	/** {@inheritDoc} */
	@Override
	public List<String> getIntermediate(String key) {

		if (results.size() > 0) {
			List<String> returnResults;
			synchronized (this) {
				returnResults = this.results.get(key);
				this.results.remove(key);
			}
			return returnResults;
		}
		return null;
	}

	/** {@inheritDoc} */
	@Override
	public void runMapTask(String input) {
		this.currentState = State.INPROGRESS;
		this.toDo = input;
		PoolHelper.getPool().enqueueWork(this);
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

	/** {@inheritDoc} */
	@Override
	public State getCurrentState() {
		return this.currentState;
	}

	/** {@inheritDoc} */
	@Override
	public void doWork() {
		this.mapTask.map(this, toDo);
		this.currentState = State.COMPLETED;
	}

	/** {@inheritDoc} */
	@Override
	public List<String> getKeysSnapshot() {
		return new ArrayList<String>(results.keySet());
	}

	/** {@inheritDoc} */
	@Override
	public void setMapTask(MapTask task) {
		this.mapTask = task;
	}

	/** {@inheritDoc} */
	@Override
	public void setCombineTask(CombinerTask task) {
		this.combinerTask = task;

	}
}
