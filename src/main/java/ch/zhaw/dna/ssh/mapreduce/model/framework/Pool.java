package ch.zhaw.dna.ssh.mapreduce.model.framework;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Ein Pool verwaltet Worker für Aufgaben die ausgeführt werden müssen. Dazu ist es möglich dem Pool Aufgaben und Worker zu übergeben.
 * 
 * @author Max, Desiree Sacher
 * 
 */
public class Pool implements Runnable {

	// Liste mit allen Workern
	private final List<Worker> workingWorker;

	// Liste mit allen Workern, die Arbeit übernehmen können.
	private final List<Worker> emptyWorkerList;

	// Liste mit aller Arbeit, die von Workern übernommen werden kann.
	private final Queue<WorkerTask> toDoList;

	/**
	 * Erstellt einen neuen Pool der gern Aufgaben und Worker entgegen nimmt.
	 */
	public Pool() {
		workingWorker = new ArrayList<Worker>();
		emptyWorkerList = new ArrayList<Worker>();
		toDoList = new LinkedList<WorkerTask>();

		new Thread(this).start();
	}

	/**
	 * Gibt die Anzahle der Worker zurück die für diesen Pool arbeiten (können)
	 * 
	 * @return amountWorker
	 */
	public int getCurrentPoolSize() {
		return emptyWorkerList.size() + workingWorker.size();
	}

	/**
	 * Gibt die anzahl Verfuegbarer Worker zurueck
	 * 
	 * @return die Anzahl an freien Worker
	 */
	public int getFreeWorkers() {
		return emptyWorkerList.size();
	}

	@Override
	public void run() {
		try {
			while (true) {
				while (toDoList.size() > 0) {

					if (emptyWorkerList.size() > 0) {
						Worker nextWorker = emptyWorkerList.get(0);

						// Die Task am Kopf der Queue asynchron ausführen
						nextWorker.execute(toDoList.poll());
						
						emptyWorkerList.remove(nextWorker);
						workingWorker.add(nextWorker);
					} else {
						// Es sind Aufgaben zu erfüllen! schnell schauen ob wieder freie worker vorhanden sind.
						Thread.sleep(200);
					}
				}
				// Jede halbe Sekunde wird versucht Tasks abzuarbeiten
				Thread.sleep(500);
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Methode mit der sich ein Worker nach seiner Arbeit zurückmelden kann.
	 * @param finishedWorker der Worker der sich zurückmeldet
	 */
	public void workerIsFinished(Worker finishedWorker){
		workingWorker.remove(finishedWorker);
		emptyWorkerList.add(finishedWorker);
	}

	/**
	 * Reiht eien neuen WorkerTask in die Aufgabenliste des Pools ein
	 * @param mapRunner eine Aufgabe für den Worker
	 */
	public void enqueueWork(WorkerTask task) {
		toDoList.offer(task);
	}
	
	/**
	 * Stellt dem Pool einen Worker zur Verfügung
	 * @param newWorker der Worker der zur Verfügung gestellt werden soll.
	 */
	public void donateWorker(Worker newWorker){
		emptyWorkerList.add(newWorker);
	}
}
