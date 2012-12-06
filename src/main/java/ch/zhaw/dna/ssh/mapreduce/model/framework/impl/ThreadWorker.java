package ch.zhaw.dna.ssh.mapreduce.model.framework.impl;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

import javax.inject.Inject;

import ch.zhaw.dna.ssh.mapreduce.model.framework.KeyValuePair;
import ch.zhaw.dna.ssh.mapreduce.model.framework.Pool;
import ch.zhaw.dna.ssh.mapreduce.model.framework.Worker;
import ch.zhaw.dna.ssh.mapreduce.model.framework.WorkerTask;
import ch.zhaw.dna.ssh.mapreduce.model.framework.registry.WorkerExecutor;

/**
 * Implementation von einem Thread-basierten Worker. Der Task wird ueber einen Executor ausgefuehrt.
 * 
 * @author Reto
 * 
 */
public class ThreadWorker implements Worker {

	private final Map<String, List<KeyValuePair>> storedKeyValues = new HashMap<String, List<KeyValuePair>>();

	/**
	 * Aus dem Pool kommt der Worker her und dahin muss er auch wieder zurueck.
	 */
	private final Pool pool;

	/**
	 * Der Executor ist fuer asynchrone ausfuehren.
	 */
	private final Executor executor;

	/**
	 * Erstellt einen neunen ThreadWorker mit dem gegebenen Pool und Executor.
	 * 
	 * @param pool
	 * @param executor
	 */
	@Inject
	public ThreadWorker(Pool pool, @WorkerExecutor Executor executor) {
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
				task.doWork(ThreadWorker.this);
				pool.workerIsFinished(ThreadWorker.this);
			}

		});
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void storeKeyValuePair(String mapReduceTaskUID, String key, String value) {
		// TODO: Sollte auch save sein, wenn gerade ein anderer MapReduce Task an seine Daten will
		List<KeyValuePair> newKeyValues;

		if (storedKeyValues.containsKey(mapReduceTaskUID)) {
			newKeyValues = storedKeyValues.get(mapReduceTaskUID);
		} else {
			newKeyValues = new LinkedList<KeyValuePair>();
		}

		newKeyValues.add(new KeyValuePair(key, value));
		storedKeyValues.put(mapReduceTaskUID, newKeyValues);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<KeyValuePair> getStoredKeyValuePairs(String mapReduceTaskUID) {
		return this.storedKeyValues.get(mapReduceTaskUID);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void replaceStoredKeyValuePairs(String mapReduceTaskUID, List<KeyValuePair> newList) {
		if (storedKeyValues.containsKey(mapReduceTaskUID)) {
			storedKeyValues.remove(mapReduceTaskUID);
		}
		storedKeyValues.put(mapReduceTaskUID, newList);
	}
}
