package ch.zhaw.dna.ssh.mapreduce.model.framework;

import java.util.List;

/**
 * Die Middleware für einen REDUCE Task auf einem Worker. Ein ReduceTask muss Ergebnisse für seine derzeitige Aufgabe
 * ausgeben können.
 * 
 * @author Reto
 * 
 */
public interface ReduceWorkerTask extends WorkerTask {

	/**
	 * Führt die derzeit zugewiesene Reduce Aufgabe für den Output der gegebenen MapInstructions aus.
	 * 
	 * @param toDo
	 *            der Input der bearbeitet werden soll.
	 */
	boolean runReduceTask(List<KeyValuePair> toDo);

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
	ReduceInstruction getReduceTask();

	/**
	 * Liefert die MapReduceTask ID zu der dieser Task gehoert
	 * 
	 * @return die MapReduceTask ID zu der dieser Task gehoert
	 */
	String getMapReduceTaskUUID();
}
