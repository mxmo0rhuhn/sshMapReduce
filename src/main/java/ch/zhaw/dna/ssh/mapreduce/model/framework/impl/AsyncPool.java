package ch.zhaw.dna.ssh.mapreduce.model.framework.impl;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import ch.zhaw.dna.ssh.mapreduce.model.framework.Pool;
import ch.zhaw.dna.ssh.mapreduce.model.framework.Worker;
import ch.zhaw.dna.ssh.mapreduce.model.framework.WorkerTask;

/**
 * Ein Pool verwaltet Worker für Aufgaben die ausgeführt werden müssen. Dazu ist es möglich dem Pool Aufgaben und Worker zu übergeben.
 * 
 * @author Max, Desiree Sacher
 * 
 */
public final class AsyncPool implements Runnable, Pool {

	// Liste mit allen Workern
	private final List<Worker> workingWorker;

	// Liste mit allen Workern, die Arbeit übernehmen können.
	private final List<Worker> emptyWorkerList;

	// Liste mit aller Arbeit, die von Workern übernommen werden kann.
	private final Queue<WorkerTask> toDoList;

	/**
	 * Erstellt einen neuen Pool der gern Aufgaben und Worker entgegen nimmt.
	 */
	public AsyncPool() {
		workingWorker = new ArrayList<Worker>();
		emptyWorkerList = new ArrayList<Worker>();
		toDoList = new LinkedList<WorkerTask>();
	}
	
	/**
	 * Startet den Thread zur asynchronen Arbeit
	 */
	@Override
	public void init() {
		new Thread(this).start();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getCurrentPoolSize() {
		return emptyWorkerList.size() + workingWorker.size();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getFreeWorkers() {
		return emptyWorkerList.size();
	}

	/**
	 * Wartet auf Auftraege und fuert diese mit den Workers aus.
	 */
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
			Thread.currentThread().interrupt();
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void workerIsFinished(Worker finishedWorker){
		workingWorker.remove(finishedWorker);
		emptyWorkerList.add(finishedWorker);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void enqueueWork(WorkerTask task) {
		toDoList.offer(task);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void donateWorker(Worker newWorker){
		emptyWorkerList.add(newWorker);
	}
}
