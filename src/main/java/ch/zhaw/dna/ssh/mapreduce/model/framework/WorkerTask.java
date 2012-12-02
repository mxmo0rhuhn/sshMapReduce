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
	 */
	void doWork();

	/***
	 * Gibt den Zustand der Aufgabe die erfüllt werden soll zurück.
	 * 
	 * @return der Zustand
	 */
	State getCurrentState();
}
