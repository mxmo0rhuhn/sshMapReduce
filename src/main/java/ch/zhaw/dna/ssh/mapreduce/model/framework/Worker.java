package ch.zhaw.dna.ssh.mapreduce.model.framework;

import java.util.HashMap;

/**
 * Ein generischer Worker für Aufgaben. Ein Worker kann entweder einen MAP oder einen REDUCE Task ausführen. Ein Worker besitzt einen
 * MAP-Speicher auf den REDUCE Tasks zugreifen können.
 * 
 * @author Max
 */
public class Worker implements Runnable {

	// Alle möglichen Zustände in denen sich Worker befinden kann
	public enum State {
		IDLE, INPROGRESS, COMPLETED
	}

	// Der master für den der Worker arbeitet
	MapReduceTask master;

	// Der Zustand in dem sich der Worker befindet
	State currentState;

	// Ergebnisse von auf dem Worker ausgeführten MAP Tasks
	HashMap<String, String> results;

	/**
	 * Erstellt einen neuen Worker der bereit für Arbeit ist.
	 */
	public Worker() {
		currentState = State.IDLE;
	}

	/**
	 * Weisst dem Worker einen Master zu. Während der Worker für einen Master arbeitet besitzt er eine Outputstruktur für MAP
	 * Zwischenresultate.
	 * 
	 * @param master
	 *            der Master für den der Worker arbeitet
	 */
	public void assignMaster(MapReduceTask master) {
		this.master = master;
		this.results = new HashMap<String, String>();
	}

	/**
	 * Entfernt den derzeitigen Master und löscht den Speicher für Zwischenresultate, da sie nichtmehr der derzeitigen Master entsprechen.
	 */
	public void removeMaster() {

		synchronized (this) {
			results.clear();
		}
		this.master = null;
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
			results.put(key, value);
		}
	}

	/**
	 * Gibt einem Master die derzeitigen Zwischenergebnisse von auf diesem Worker ausgeführten MAP Tasks zurück zurück und löscht sie.
	 * 
	 * @return Die Bisherigen Zwischenergebnisse oder null wenn derzeit keine Zwischenergebnisse vorliegen.
	 */
	public HashMap<String, String> getIntermediate() {

		if (results.size() > 0) {
			HashMap<String, String> returnResults;
			synchronized (this) {
				returnResults = results;
				results.clear();
			}
			return returnResults;
		}
		return null;
	}

	@Override
	public void run() {

	}
}
