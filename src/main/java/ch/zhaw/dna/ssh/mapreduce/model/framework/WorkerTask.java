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
		IDLE, INPROGRESS, COMPLETED
	}

	/**
	 * Führt die Aufgabe, die der Worker erfüllen soll aus.
	 */
	public void doWork();
	
	/***
	 * Gibt den Zustand der Aufgabe die erfüllt werden soll zurück.
	 * @return der Zustand
	 */
	public State getCurrentState();
}
