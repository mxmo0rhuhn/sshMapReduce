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
	 * Initialisiert den Pool. Wenn eine konkrete Implementation Logik zu initialisierung hat, muss diese hier ausgfuert werden.
	 */
	void init();

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
	 */
	void enqueueWork(WorkerTask task);

	/**
	 * Stellt dem Pool einen Worker zur Verfügung
	 * @param newWorker der Worker der zur Verfügung gestellt werden soll.
	 */
	void donateWorker(Worker newWorker);

}
