package ch.zhaw.dna.ssh.mapreduce.model.framework;

import java.util.List;

/**
 * Die Middleware für einen REDUCE Task auf einem Worker. Ein ReduceTask muss Ergebnisse für seine derzeitige Aufgabe
 * ausgeben können.
 * 
 * @author Reto
 * 
 */
public interface ReduceRunner extends WorkerTask {

	/**
	 * Weisst dem ReduceRunner eine REDUCE Task zu mit der aus einem Input ein aggregierter Output erstellt wird.
	 * 
	 * @param task
	 *            die Aufgabe die zugewiesen werden soll
	 */
	void setReduceTask(ReduceTask task);

	/**
	 * Weisst dem ReduceRunner einen Key für den er ausgeführt wird zu.
	 * 
	 * @param key
	 *            der Key für den der Reduce Task ausgeführt wird.
	 */
	void setKey(String key);

	/**
	 * Führt die derzeit zugewiesene Reduce Aufgabe für den Output der gegebenen MapTasks aus.
	 * 
	 * @param toDo
	 *            der Input der bearbeitet werden soll.
	 */
	void runReduceTask(List<MapRunner> mapRunners);

	/**
	 * Gibt einem Reduce Task die Möglichkeit ein Ergebnis ins Framework zu übergeben.
	 * 
	 * @param result
	 *            das Ergebnis das übergeben werden soll
	 */
	void emit(String result);

	/**
	 * Jeder Reduce Task reduziert ein Wort. Diese Wort wird hier zurueckgegeben.
	 * 
	 * @return das verwendetete Wort. Null, wenns keins gesetzt ist.
	 */
	String getKey();

	/**
	 * Gibt den ReduceTask fuer diesen Runner zurueck.
	 * 
	 * @return Gibt den ReduceTask fuer diesen Runner zurueck. null wenn keiner gesetzt ist.
	 */
	ReduceTask getReduceTask();
}
