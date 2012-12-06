package ch.zhaw.dna.ssh.mapreduce.model.framework;


/**
 * Ein Pool verwaltet Worker für Aufgaben die ausgeführt werden müssen. Dazu ist es möglich dem Pool Aufgaben und Worker
 * zu übergeben.
 * 
 * @author Reto
 *
 */
public interface Pool {
	
	/**
	 * Gibt die Anzahle der Worker zurück die für diesen Pool arbeiten (können)
	 * 
	 * @return amountWorker
	 */
	int getCurrentPoolSize();

	/**
	 * Gibt die anzahl Verfuegbarer Worker zurueck
	 * 
	 * @return die Anzahl an freien Worker
	 */
	int getFreeWorkers();

	/**
	 * Methode mit der sich ein Worker nach seiner Arbeit zurückmelden kann.
	 * @param finishedWorker der Worker der sich zurückmeldet
	 */
	void workerIsFinished(Worker finishedWorker);

	/**
	 * Reiht eien neuen WorkerTask in die Aufgabenliste des Pools ein
	 * @param mapRunner eine Aufgabe für den Worker
	 * @return true, wenn der task angenommen wurde, sonst false
	 */
	boolean enqueueWork(WorkerTask task);

	/**
	 * Stellt dem Pool einen Worker zur Verfügung
	 * @param newWorker der Worker der zur Verfügung gestellt werden soll.
	 */
	void donateWorker(Worker newWorker);
	
	/**
	 * Check, ob der Pool gestartet ist.
	 */
	boolean isRunning();

}
