/**
 * 
 */
package ch.zhaw.dna.ssh.mapreduce.model.framework;

import java.util.List;

/**
 * Die Middleware für einen MAP Task auf einem Worker. Ein MAP Task benötigt ein MAP-Speicher für Zwischenresultate in welchen Daten
 * geschrieben werden können, einen Status usw.
 * 
 * @author Max
 * 
 */
public interface MapWorkerTask extends WorkerTask {

	/**
	 * Weisst dem MapRunner eine MAP Task zu mit der aus einem gewissen Input KeyValue-Pairs erstellt werden.
	 * 
	 * @param task
	 *            die Aufgabe die zugewiesen werden soll
	 */
	void setMapTask(MapInstruction task);

	/**
	 * Weisst dem Map Runner eine COMBINE Task zu die die Zwischenergebnisse aggregiert.
	 * 
	 * @param task
	 *            die Aufgabe die zugewiesen werden soll
	 */
	void setCombineTask(CombinerInstruction task);

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
	
	/**
	 * Gibt den verwendeten Combiner Task zurueck.
	 * @return den verwendeten CombinerTask, wenn keiner verwendet wurde null
	 */
	CombinerInstruction getCombinerTask();

	/**
	 * Gibt den verwendeten Map Task zurueck.
	 * @return den verwendeten MapTask, wenn keiner verwendet wurde null
	 */
	MapInstruction getMapTask();
}
