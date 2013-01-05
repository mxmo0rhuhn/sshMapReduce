package ch.zhaw.dna.ssh.mapreduce.model.framework;


/***
 * Eine WorkerTask ist eine Aufgabe die von einem Worker ausgeführt werden kann.
 * 
 * @author Max
 * 
 */
public interface WorkerTask {

	// Alle möglichen Zustände in denen sich Worker befinden kann
	public enum State {
		INITIATED, // erstellt
		ENQUEUED, // dem pool zur ausfuehrung ueberreicht
		INPROGRESS, // pool hat task akzeptiert
		COMPLETED, // completed
		FAILED // failed
	}

	/**
	 * Führt die Aufgabe, die der Worker erfüllen soll aus.
	 * 
	 * @param processingWorker
	 *            der Worker auf dem die Aufgabe ausgeführt wird.
	 */
	void doWork(Worker processingWorker);

	/***
	 * Gibt den Zustand der Aufgabe die erfüllt werden soll zurück.
	 * 
	 * @return der Zustand
	 */
	State getCurrentState();

	/**
	 * Gibt den Worker auf dem diese Aufgabe ausgeführt wurde zurück.
	 * 
	 * @return Der worker der diese Task ausgeführt hat.
	 */
	Worker getWorker();
	
	/**
	 * Die ID dieses Worker Tasks.
	 * @return
	 */
	String getUUID();

	/**
	 * Liefert die MapReduceTask ID zu der dieser Task gehoert
	 * 
	 * @return die MapReduceTask ID zu der dieser Task gehoert
	 */
	String getMapReduceTaskUUID();
}
