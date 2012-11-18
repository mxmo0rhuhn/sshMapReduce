/**
 * 
 */
package ch.zhaw.dna.ssh.mapreduce.model.framework;

import java.util.List;

/**
 * Die Middleware für einen MAP Task auf einem Worker. Ein MAP Task benötigt ein MAP-Speicher für
 * Zwischenresultate in welchen Daten geschrieben werden können, einen Status usw.
 * 
 * @author Max
 * 
 */
public interface MapRunner extends WorkerTask {

	/**
	 * Möglichkeit Ein Zwischenergebnis aus der Ausführung eines MAP tasks heraus zu schreiben.
	 * 
	 * @param key
	 *            Key des Zwischenergebnisses.
	 * @param value
	 *            Value des Zwischenergebnisses.
	 */
	void emitIntermediateMapResult(String key, String value);

	/**
	 * Gibt einem die derzeitigen Zwischenergebnisse für MAP Tasks mit einem bestimmten Key zurück und löscht sie.
	 * 
	 * @return Die Bisherigen Zwischenergebnisse für den key oder null wenn derzeit keine Zwischenergebnisse vorliegen.
	 */
	List<String> getIntermediate(String key);

	/**
	 * Führt die derzeit zugewiesene MAP Aufgabe mit dem gegebenen Input aus.
	 * 
	 * @param toDo
	 *            der Input der bearbeitet werden soll.
	 */
	void runMapTask(String input);

	/**
	 * Bestimmt die Anzahl an Zwischenergebnissen die zwischen jedem reduce Task gewartet wird.
	 * 
	 * @param maxWaitResults
	 *            die neue Anzahl an gewarteten Zwischenergebnissen.
	 */
	void setMaxWaitResults(int maxWaitResults);

	/**
	 * Gibt die Anzahl an Zwischenergebnissen die zwischen jedem reduce Task gewartet wird zurück.
	 * 
	 * @return die Anzahl abgewarteter Zwischenergebnisse.
	 */
	int getMaxWaitResults();

	/**
	 * Gibt eine Liste von allen Keys, die beim intermediate result von diesem MapRunner existieren zurueck.
	 * 
	 * @return die Liste mit keys.
	 */
	List<String> getKeysSnapshot();
}
