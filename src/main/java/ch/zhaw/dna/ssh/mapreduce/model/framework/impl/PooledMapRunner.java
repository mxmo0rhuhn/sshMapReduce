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
	private volatile State currentState;

	// Ergebnisse von auf dem Worker ausgeführten MAP Tasks
	private Map<String, List<String>> results;

	// Aufgabe, die der Task derzeit ausführt
	private final MapTask task;

	// Das Limit für die Anzahl an neuen Zwischenergebnissen die gewartet werden soll, bis der Combiner ausgeführt wird.
	private int maxWaitResults;

	// Die Anzahl an neuen Zwischenergebnissen seit dem letzten Combiner.
	private int newResults;

	// Falls vorhanden ein Combiner für die Zwischenergebnisse
	private final CombinerTask combiner;

	// Die derzeit zu bearbeitenden Daten
	private String toDo;

	/**
	 * Weisst dem Worker einen Master implizit über eine aufgabe zu. Während der Worker für einen Master arbeitet besitzt er eine
	 * Outputstruktur für MAP Zwischenresultate.
	 * 
	 * @param task
	 *            die Aufgabe die für den derzeitigen master bearbeitet wird
	 */
	public PooledMapRunner(MapTask task) {
		this.task = task;
		this.combiner = null;
		this.results = new HashMap<String, List<String>>();
		currentState = State.IDLE;
	}

	/**
	 * Weisst dem Worker einen Master implizit über eine aufgabe zu. Während der Worker für einen Master arbeitet besitzt er eine
	 * Outputstruktur für MAP Zwischenresultate. Diese Outputstruktur wird regelmässig durch einen combiner Task verdichtet.
	 * 
	 * @param task
	 *            die Aufgabe die für den derzeitigen master bearbeitet wird
	 * @param combiner
	 *            die Aufgabe um Zwischenergebnisse zu verdichten
	 */
	public PooledMapRunner(MapTask task, CombinerTask combiner) {
		this.task = task;
		this.combiner = combiner;
		this.maxWaitResults = 500;
		this.results = new HashMap<String, List<String>>();
		currentState = State.IDLE;
	}

	@Override
	public void emitIntermediateMapResult(String key, String value) {
		synchronized (this) {

			if(this.results.containsKey(key)) {
				List<String> curList = this.results.get(key);
				curList.add(value);
				results.put(key, curList);

			} else {
				ArrayList<String> resultList = new ArrayList<String>();
				resultList.add(value);
				this.results.put(key, resultList);
			}

			this.newResults++;

			if (this.combiner != null) {
				if (this.newResults >= this.maxWaitResults) {
					for(String currentKey : results.keySet()) {
						ArrayList<String> resultList = new ArrayList<String>();
						resultList.add(this.combiner.combine(results.get(currentKey).iterator()));
						this.results.put(currentKey, resultList);
					}
				}
			}
		}
	}

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

	@Override
	public void runMapTask(String input) {
		this.currentState = State.INPROGRESS;
		this.toDo = input;
		PoolHelper.getPool().enqueueWork(this);
	}

	@Override
	public int getMaxWaitResults() {
		return this.maxWaitResults;
	}

	@Override
	public void setMaxWaitResults(int maxWaitResults) {
		this.maxWaitResults = maxWaitResults;
	}

	@Override
	public State getCurrentState() {
		return this.currentState;
	}

	@Override
	public void doWork() {
		this.task.map(this, toDo);
		this.currentState = State.COMPLETED;
	}

	@Override
	public List<String> getKeysSnapshot() {
		// TODO Auto-generated method stub
		return null;
	}
}
