/**
 * 
 */
package ch.zhaw.dna.ssh.mapreduce.model.framework;

/**
 * Die Middleware für eine MapInstruction auf einem Worker. Eine MapInstruction benötigt ein MAP-Speicher für
 * Zwischenresultate in welchen Daten geschrieben werden können, einen Status usw.
 * 
 * @author Max
 * 
 */
public interface MapWorkerTask extends WorkerTask {

	/**
	 * Führt die derzeit zugewiesene MAP Aufgabe mit dem gegebenen Input aus.
	 * 
	 * @param inputUID
	 *            eindeutiger identifyer für den mitgegebenen Input
	 * @param input
	 *            der Input der bearbeitet werden soll.
	 * 
	 */
	void runMapTask();

	/**
	 * Gibt die verwendete MapInstruciton zurueck.
	 * 
	 * @return verwendete MapInstruction
	 */
	MapInstruction getMapInstruction();

	/**
	 * Liefert die verwendete CombinerInstruction
	 * 
	 * @return die verwendete CombinerInstruction. null wenn keine verwendet wurde.
	 */
	CombinerInstruction getCombinerInstruction();

	/**
	 * Liefert die MapReduceTask ID zu der dieser Task gehoert
	 * 
	 * @return die MapReduceTask ID zu der dieser Task gehoert
	 */
	String getMapReduceTaskUUID();
}
