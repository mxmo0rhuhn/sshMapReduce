package ch.zhaw.dna.ssh.mapreduce.model.framework;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;

import javax.inject.Inject;

import ch.zhaw.dna.ssh.mapreduce.model.framework.WorkerTask.State;

public final class Master {

	private final ConcurrentMap<String, Collection<String>> globalResultStructure = new ConcurrentHashMap<String, Collection<String>>();

	private final RunnerFactory runnerFactory;

	@Inject
	public Master(RunnerFactory runnerFactory) {
		this.runnerFactory = runnerFactory;
	}

	public Map<String, Collection<String>> runComputation(final MapTask mapTask, final CombinerTask combinerTask,
			final ReduceTask reduceTask, Iterator<String> input) {

		List<MapRunner> mapRunners = new LinkedList<MapRunner>();
		while (input.hasNext()) {
			MapRunner mapRunner = runnerFactory.createMapRunner(mapTask, combinerTask);
			mapRunners.add(mapRunner);
			mapRunner.runMapTask(input.next());
		}

		// TODO reduce runner müssen schon früher ausgegeben werden
		Map<String, ReduceRunner> reduceRunners = new HashMap<String, ReduceRunner>();
		while (!allWorkerTasksCompleted(mapRunners)) {
			for (MapRunner mapRunner : mapRunners) {
				for (String key : mapRunner.getKeysSnapshot()) {
					if (!reduceRunners.containsKey(key)) {
						ReduceRunner reduceRunner = runnerFactory.createReduceRunner(reduceTask);
						reduceRunner.runReduceTask(mapRunners);
						reduceRunners.put(key, reduceRunner);
					}
				}
			}
		}

		while (!allWorkerTasksCompleted(reduceRunners.values())) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				break;
			}
		}
		return globalResultStructure;
	}

	/**
	 * Fuegt das resultat fuer den Key dem globalen Resultat hinzu (thread-safe).
	 * 
	 * @param key
	 *            Key fuer das Resultat. Es kann mehere Resultate fuer einen Key geben
	 * @param result
	 *            das Resultate fuer den Key
	 */
	public void globalResultStructureAppend(String key, String result) {
		if (!this.globalResultStructure.containsKey(key)) {
			this.globalResultStructure.putIfAbsent(key, new ConcurrentLinkedQueue<String>());
		}
		Collection<String> res = this.globalResultStructure.get(key);
		res.add(result);
	}

	/**
	 * Retourniert die globale Resultat-Struktur, wo alle Resultate gespeichert werden.
	 * 
	 * @return Map mit allen Resultaten
	 */
	public Map<String, Collection<String>> getGlobalResultStructure() {
		return Collections.unmodifiableMap(this.globalResultStructure);
	}

	/**
	 * Iteriert ueber alle worker und prueft, ob sie fertig sind.
	 * 
	 * @param workers
	 * @return true, wenn alle fertig sind, sonst false.
	 */
	private boolean allWorkerTasksCompleted(Collection<? extends WorkerTask> workers) {
		for (WorkerTask worker : workers) {
			if (worker.getCurrentState() != State.COMPLETED) {
				return false;
			}
		}
		return true;
	}

}
