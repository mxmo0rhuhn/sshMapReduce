package ch.zhaw.dna.ssh.mapreduce.model.framework.impl;

import ch.zhaw.dna.ssh.mapreduce.model.framework.Pool;
import ch.zhaw.dna.ssh.mapreduce.model.framework.Worker;
import ch.zhaw.dna.ssh.mapreduce.model.framework.WorkerTask;

public class ThreadWorker implements Worker {

	private final Pool pool;

	public ThreadWorker(Pool pool) {
		this.pool = pool;
	}

	@Override
	public void execute(final WorkerTask task) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				task.doWork();
				pool.workerIsFinished(ThreadWorker.this);
			}

		}).start();
	}

}
