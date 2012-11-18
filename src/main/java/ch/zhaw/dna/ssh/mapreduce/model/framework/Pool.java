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
	private List<Worker> workingWorker;

	// Liste mit allen Workern, die Arbeit übernehmen können.
	private List<Worker> emptyWorkerList;

	// Liste mit aller Arbeit, die von Workern übernommen werden kann.
	private Queue<WorkerTask> toDoList;

	/**
	 * Erstellt einen neuen Pool der gern Aufgaben und Worker entgegen nimmt.
	 */
	public Pool() {
		emptyWorkerList = new ArrayList<Worker>();
		toDoList = new LinkedList<WorkerTask>();

		new Thread(this).run();
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

						// Die Task am Kopf der Queue ausführen
						nextWorker.assignNextTask(toDoList.poll());

						// Der Worker wird asynchron die Aufgabe ausführen
						nextWorker.work();
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
	 * @param finishedWorker
	 */
	public void workerIsFinished(Worker finishedWorker){
		emptyWorkerList.add(finishedWorker);
	}

	/***
	 * Reiht eien neuen WorkerTask in die Aufgabenliste des Pools ein
	 * @param mapRunner eine Aufgabe für den Worker
	 */
	public void enqueueWork(WorkerTask task) {
		toDoList.offer(task);
	}
	
}
