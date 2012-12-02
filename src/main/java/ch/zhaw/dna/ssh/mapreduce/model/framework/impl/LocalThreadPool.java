package ch.zhaw.dna.ssh.mapreduce.model.framework.impl;

import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.annotation.PostConstruct;
import javax.inject.Singleton;

import ch.zhaw.dna.ssh.mapreduce.model.framework.Pool;
import ch.zhaw.dna.ssh.mapreduce.model.framework.Worker;
import ch.zhaw.dna.ssh.mapreduce.model.framework.WorkerTask;

/**
 * Implementation des Pools mit lokalen Threads auf dem jeweiligen PC
 * 
 * @author Max, Desiree Sacher
 * 
 */
@Singleton
public final class LocalThreadPool implements Pool {

	// Liste mit allen Workern
	private final Queue<Worker> workingWorker = new ConcurrentLinkedQueue<Worker>();

	// Liste mit allen Workern, die Arbeit übernehmen können.
	private final BlockingQueue<Worker> availableWorkerBlockingQueue = new LinkedBlockingQueue<Worker>();

	// Liste mit aller Arbeit, die von Workern übernommen werden kann.
	private final BlockingQueue<WorkerTask> taskQueue = new LinkedBlockingQueue<WorkerTask>();

	private final AtomicBoolean isRunning = new AtomicBoolean();

	private final ExecutorService workTaskAdministrator;

	/**
	 * Erstellt einen neuen Pool der Aufgaben und Worker entgegen nimmt.
	 */
	public LocalThreadPool() {
		this.workTaskAdministrator = Executors.newSingleThreadExecutor();
	}

	/**
	 * Startet den Thread zur asynchronen Arbeit
	 */
	// wird nach dem konstruktor aufgerufen
	@PostConstruct
	public void init() {
		// nur starten, wenn er noch nicht gestartet wurde
		if (this.isRunning.compareAndSet(false, true)) {
			this.workTaskAdministrator.execute(new WorkerTaskAdministrator());
		} else {
			throw new RuntimeException("Cannot start Pool twice");
		}
	}

	@Override
	public void shutdownNow() {
		// forciert einen abrupten shutdown
		if (this.isRunning.getAndSet(false)) {
			this.workTaskAdministrator.shutdownNow();
		} else {
			throw new IllegalStateException("Wasn't running");
		}
	}

	@Override
	public boolean isRunning() {
		return this.isRunning.get();
	}

	/**
	 * {@inheritDoc} Der Wert ist optimistisch - kann veraltet sein.
	 */
	@Override
	public int getCurrentPoolSize() {
		return availableWorkerBlockingQueue.size() + workingWorker.size();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getFreeWorkers() {
		return availableWorkerBlockingQueue.size();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void workerIsFinished(Worker finishedWorker) {
		// TODO atomicity
		// TODO return values
		workingWorker.remove(finishedWorker);
		availableWorkerBlockingQueue.add(finishedWorker);
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
		// TODO add vs offer
		availableWorkerBlockingQueue.add(newWorker);
	}

	private class WorkerTaskAdministrator implements Runnable {

		/**
		 * Wartet auf Auftraege und fuert diese mit den Workers aus.
		 */
		@Override
		public void run() {
			try {
				while (true) {
					WorkerTask task = taskQueue.take(); // blockiert bis ein Task da ist
					Worker worker = availableWorkerBlockingQueue.take(); // blockiert, bis ein Worker frei ist
					workingWorker.add(worker);
					worker.execute(task);
				}
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}

	}
}
