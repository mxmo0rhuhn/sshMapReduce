/**
 * 
 */
package ch.zhaw.dna.ssh.mapreduce.model.framework;

/**
 * Die Middleware für einen MAP Task auf einem Worker. Ein MAP Task benötigt ein MAP-Speicher für
 * Zwischenresultate in welchen Daten geschrieben werden können, einen Status usw.
 * 
 * @author Max
 * 
 */
public interface MapWorkerTask extends WorkerTask {

	/**
	 * Weisst dem MapRunner eine MAP Task zu mit der aus einem gewissen Input KeyValue-Pairs
	 * erstellt werden.
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
	 * Führt die derzeit zugewiesene MAP Aufgabe mit dem gegebenen Input aus.
	 * 
	 * @param inputUID
	 *            eindeutiger identifyer für den mitgegebenen Input
	 * @param input
	 *            der Input der bearbeitet werden soll.
	 * 
	 */
	void runMapTask(String inputUID, String input);

	/**
	 * Gibt den eindeutigen Identifyer des derzeitigen Input zurück.
	 * 
	 * @return String mit der UID
	 */
	String getCurrentInputUID();
}
