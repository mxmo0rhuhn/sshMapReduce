package ch.zhaw.dna.ssh.mapreduce.model.framework;

import java.util.HashMap;
import java.util.Map;

/**
 * Eine Implementation des MapRunners mit einem WorkerPool.
 * 
 * @author Max
 */
public class PooledMapRunner implements WorkerTask, MapRunner {

	// Der Zustand in dem sich der Worker befindet
	private State currentState;

	// Ergebnisse von auf dem Worker ausgeführten MAP Tasks
	private Map<String, String> results;

	// Aufgabe, die der Task derzeit ausführt
	private final MapTask task;

	// Das Limit für die Anzahl an neuen Zwischenergebnissen die gewartet werden soll, bis der Combiner ausgeführt wird.
	private int maxWaitResults;

	// Die Anzahl an neuen Zwischenergebnissen seit dem letzten Combiner.
	private int newResults;

	// Falls vorhanden ein Combiner für die Zwischenergebnisse
	private final CombinerTask combiner;

	// Die derzeit zu bearbeitenden Daten
	private String[] toDo;

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
		this.results = new HashMap<String, String>();
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
		this.results = new HashMap<String, String>();
		currentState = State.IDLE;
	}

	@Override
	public void emitIntermediateMapResult(String key, String value) {
		synchronized (this) {
			this.results.put(key, value);
			this.newResults++;

			if (this.combiner != null) {
				if (this.newResults >= this.maxWaitResults) {
					this.results = this.combiner.combine(results);
					this.newResults = 0;
				}
			}
		}
	}

	@Override
	public Map<String, String> getIntermediate() {

		if (results.size() > 0) {
			Map<String, String> returnResults;
			synchronized (this) {
				returnResults = this.results;
				this.results.clear();
			}
			return returnResults;
		}
		return null;
	}

	@Override
	public void runMapTask(String[] toDo, Pool pool) {
		this.currentState = State.INPROGRESS;
		this.toDo = toDo;
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
}
