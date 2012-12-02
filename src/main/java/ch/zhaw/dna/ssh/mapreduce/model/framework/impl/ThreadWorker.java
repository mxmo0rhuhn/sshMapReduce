package ch.zhaw.dna.ssh.mapreduce.model.framework.impl;

import java.util.concurrent.ExecutorService;

import javax.inject.Inject;

import ch.zhaw.dna.ssh.mapreduce.model.framework.Pool;
import ch.zhaw.dna.ssh.mapreduce.model.framework.Worker;
import ch.zhaw.dna.ssh.mapreduce.model.framework.WorkerTask;
import ch.zhaw.dna.ssh.mapreduce.model.framework.registry.SingleThreaded;

/**
 * Implementation von einem Thread-basierten Worker. Der Task wird ueber einen Executor ausgefuehrt.
 * 
 * @author Reto
 * 
 */
public class ThreadWorker implements Worker {

	/**
	 * Aus dem Pool kommt der Worker her und dahin muss er auch wieder zurueck.
	 */
	private final Pool pool;

	/**
	 * Der Executor ist fuer asynchrone ausfuehren.
	 */
	private final ExecutorService executor;

	/**
	 * Erstellt einen neunen ThreadWorker mit dem gegebenen Pool und Executor.
	 * 
	 * @param pool
	 * @param executor
	 */
	@Inject
	public ThreadWorker(Pool pool, @SingleThreaded ExecutorService executor) {
		this.pool = pool;
		this.executor = executor;
	}

	/**
	 * Fuehrt den gegebenen Task asynchron aus und offierirt sich selbst am Ende wieder dem Pool.
	 */
	@Override
	public void execute(final WorkerTask task) {
		this.executor.execute(new Runnable() {
			@Override
			public void run() {
				task.doWork();
				pool.workerIsFinished(ThreadWorker.this);
			}

		});
	}
}
