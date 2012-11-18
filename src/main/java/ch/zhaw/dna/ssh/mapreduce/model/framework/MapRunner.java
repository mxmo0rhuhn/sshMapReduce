package ch.zhaw.dna.ssh.mapreduce.model.framework;

import java.util.HashMap;

/**
 * Ein das Framework für MAP für Aufgaben. Ein Worker kann entweder einen MAP oder einen REDUCE Task ausführen. Es stellt einen MAP-Speicher
 * für Zwischenresultate zur Verfügung in welches Daten geschrieben werden können.
 * 
 * @author Max
 */
public class MapRunner implements Worker {

	// Alle möglichen Zustände in denen sich Worker befinden kann
	public enum State {
		IDLE, INPROGRESS, COMPLETED
	}

	// Der Zustand in dem sich der Worker befindet
	private State currentState;

	// Ergebnisse von auf dem Worker ausgeführten MAP Tasks
	private HashMap<String, String> results;

	// Aufgabe, die der Task derzeit ausführt
	private MapTask task;

	// Das Limit für die Anzahl an neuen Zwischenergebnissen die gewartet werden soll, bis der Combiner ausgeführt wird.
	private int maxWaitResults;

	// Die Anzahl an neuen Zwischenergebnissen seit dem letzten Combiner.
	private int newResults;

	// Falls vorhanden ein Combiner für die Zwischenergebnisse
	private CombinerTask combiner;

	// Die derzeit zu bearbeitenden Daten, falls welche vorhanden sind

	/**
	 * Weisst dem Worker einen Master implizit über eine aufgabe zu. Während der Worker für einen Master arbeitet besitzt er eine
	 * Outputstruktur für MAP Zwischenresultate.
	 * 
	 * @param task
	 *            die Aufgabe die für den derzeitigen master bearbeitet wird
	 */
	public void assignMaster(MapTask task) {
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
	public void assignMaster(MapTask task, CombinerTask combiner) {
		this.task = task;
		this.combiner = combiner;
		this.maxWaitResults = 500;
		this.results = new HashMap<String, String>();
		currentState = State.IDLE;
	}

	/**
	 * Entfernt den derzeitigen Master und löscht den Speicher für Zwischenresultate, da sie nichtmehr der derzeitigen Master entsprechen.
	 */
	public void removeMaster() {

		synchronized (this) {
			this.results.clear();
		}
		this.combiner = null;
		this.task = null;
	}

	/**
	 * Möglichkeit Ein Zwischenergebnis aus einem MAP task zu schreiben.
	 * 
	 * @param key
	 *            Key des Zwischenergebnisses.
	 * @param value
	 *            Value des Zwischenergebnisses.
	 */
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

	/**
	 * Gibt einem Master die derzeitigen Zwischenergebnisse der ausgeführten MAP Tasks zurück und löscht sie.
	 * 
	 * @return Die Bisherigen Zwischenergebnisse oder null wenn derzeit keine Zwischenergebnisse vorliegen.
	 */
	public HashMap<String, String> getIntermediate() {

		if (results.size() > 0) {
			HashMap<String, String> returnResults;
			synchronized (this) {
				returnResults = this.results;
				this.results.clear();
			}
			return returnResults;
		}
		return null;
	}

	/**
	 * Führt die derzeit zugewiesene MAP Aufgabe mit dem gegebenen Input aus.
	 * 
	 * @param toDo
	 *            der Input der bearbeitet werden soll.
	 */
	public void runMapTask(String[] toDo) {
		this.currentState = State.INPROGRESS;
		this.task.map(this, toDo);
		this.currentState = State.COMPLETED;
	}

	/**
	 * Gibt den derzeitigen Zustand des MapRunners zurück.
	 * 
	 * @return der Zustand des MapRunners
	 */
	public State getCurrentState() {
		return this.currentState;
	}

	/**
	 * Gibt die Anzahl an Zwischenergebnissen die zwischen jedem reduce Task gewartet wird zurück.
	 * 
	 * @return die Anzahl abgewarteter Zwischenergebnisse.
	 */
	public int getMaxWaitResults() {
		return this.maxWaitResults;
	}

	/**
	 * Bestimmt die Anzahl an Zwischenergebnissen die zwischen jedem reduce Task gewartet wird.
	 * 
	 * @param maxWaitResults
	 *            die neue Anzahl an gewarteten Zwischenergebnissen.
	 */
	public void setMaxWaitResults(int maxWaitResults) {
		this.maxWaitResults = maxWaitResults;
	}

}
