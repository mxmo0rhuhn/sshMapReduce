package ch.zhaw.dna.ssh.mapreduce.model.framework.impl;

import java.util.concurrent.Executor;

import ch.zhaw.dna.ssh.mapreduce.model.framework.Pool;
import ch.zhaw.dna.ssh.mapreduce.model.framework.Worker;
import ch.zhaw.dna.ssh.mapreduce.model.framework.WorkerTask;

public class ThreadWorker implements Worker {

	private final Pool pool;

	private final Executor executor;

	public ThreadWorker(Pool pool, Executor executor) {
		this.pool = pool;
		this.executor = executor;
	}

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
