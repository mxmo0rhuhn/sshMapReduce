/**
 * 
 */
package ch.zhaw.dna.ssh.mapreduce.model.framework;

import java.util.Map;

/**
 * Die Middleware für einen MAP Task auf einem Worker. Eine MAP Task benötigt einen Ein das Framework für MAP für Aufgaben. Ein Worker kann
 * entweder einen MAP oder einen REDUCE Task ausführen. Es stellt einen MAP-Speicher für Zwischenresultate zur Verfügung in welches Daten
 * geschrieben werden können.
 * 
 * @author Max
 * 
 */
public interface MapRunner {

	/**
	 * Möglichkeit Ein Zwischenergebnis aus einem MAP task zu schreiben.
	 * 
	 * @param key
	 *            Key des Zwischenergebnisses.
	 * @param value
	 *            Value des Zwischenergebnisses.
	 */
	public void emitIntermediateMapResult(String key, String value);

	/**
	 * Gibt einem Master die derzeitigen Zwischenergebnisse der ausgeführten MAP Tasks zurück und löscht sie.
	 * 
	 * @return Die Bisherigen Zwischenergebnisse oder null wenn derzeit keine Zwischenergebnisse vorliegen.
	 */
	public Map<String, String> getIntermediate();

	/**
	 * Führt die derzeit zugewiesene MAP Aufgabe mit dem gegebenen Input aus.
	 * 
	 * @param toDo
	 *            der Input der bearbeitet werden soll.
	 * @param pool
	 *            der Pool in dem die Aufgabe ausgeführt werden soll.
	 */
	public void runMapTask(String[] toDo, Pool pool);

	/**
	 * Bestimmt die Anzahl an Zwischenergebnissen die zwischen jedem reduce Task gewartet wird.
	 * 
	 * @param maxWaitResults
	 *            die neue Anzahl an gewarteten Zwischenergebnissen.
	 */
	public void setMaxWaitResults(int maxWaitResults);

	/**
	 * Gibt die Anzahl an Zwischenergebnissen die zwischen jedem reduce Task gewartet wird zurück.
	 * 
	 * @return die Anzahl abgewarteter Zwischenergebnisse.
	 */
	public int getMaxWaitResults();

}
