package ch.zhaw.dna.ssh.mapreduce.model.framework.impl;

import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import ch.zhaw.dna.ssh.mapreduce.model.framework.Pool;
import ch.zhaw.dna.ssh.mapreduce.model.framework.Worker;
import ch.zhaw.dna.ssh.mapreduce.model.framework.WorkerTask;

/**
 * Implementation des Pools mit lokalen Threads auf dem jeweiligen PC
 * 
 * @author Max, Desiree Sacher
 * 
 */
public final class LocalThreadPool implements Runnable, Pool {

	// Liste mit allen Workern
	private final Queue<Worker> workingWorker = new ConcurrentLinkedQueue<Worker>();

	// Liste mit allen Workern, die Arbeit übernehmen können.
	private final BlockingQueue<Worker> workerBlockingQueue = new LinkedBlockingQueue<Worker>();

	// Liste mit aller Arbeit, die von Workern übernommen werden kann.
	private volatile Queue<WorkerTask> taskQueue = new ConcurrentLinkedQueue<WorkerTask>();

	/**
	 * Erstellt einen neuen Pool der Aufgaben und Worker entgegen nimmt.
	 */
	public LocalThreadPool() {
	}

	/**
	 * Startet den Thread zur asynchronen Arbeit
	 */
	@Override
	public void init() {
		Executors.newSingleThreadExecutor().execute(this);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getCurrentPoolSize() {
		return workerBlockingQueue.size() + workingWorker.size();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getFreeWorkers() {
		return workerBlockingQueue.size();
	}

	/**
	 * Wartet auf Auftraege und fuert diese mit den Workers aus.
	 */
	@Override
	public void run() {
		try {
			while (true) {
				WorkerTask task = this.taskQueue.poll();
				if (task != null) {
					Worker worker = this.workerBlockingQueue.take(); // blockiert, bis ein Worker frei ist
					this.workingWorker.add(worker);
					worker.execute(task);
				} else {
					Thread.sleep(100);
				}
			}
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void workerIsFinished(Worker finishedWorker) {
		workingWorker.remove(finishedWorker);
		workerBlockingQueue.add(finishedWorker);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean enqueueWork(WorkerTask task) {
		return taskQueue.offer(task);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void donateWorker(Worker newWorker) {
		workerBlockingQueue.add(newWorker);
	}
}
