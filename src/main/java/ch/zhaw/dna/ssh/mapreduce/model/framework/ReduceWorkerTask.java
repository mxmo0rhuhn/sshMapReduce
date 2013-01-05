package ch.zhaw.dna.ssh.mapreduce.model.framework;

/**
 * Die Middleware für einen REDUCE Task auf einem Worker. Ein ReduceTask muss Ergebnisse für seine derzeitige Aufgabe
 * ausgeben können.
 * 
 * @author Reto
 * 
 */
public interface ReduceWorkerTask extends WorkerTask {

	/**
	 * Führt die derzeit zugewiesene Reduce Aufgabe für den Output der gegebenen ReduceInstruction aus.
	 */
	boolean runReduceTask();

	/**
	 * Gibt den ReduceTask fuer diesen Runner zurueck.
	 * 
	 * @return Gibt den ReduceTask fuer diesen Runner zurueck. null wenn keiner gesetzt ist.
	 */
	ReduceInstruction getReduceTask();
}
