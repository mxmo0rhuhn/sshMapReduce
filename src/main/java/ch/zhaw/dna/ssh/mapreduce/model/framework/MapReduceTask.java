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

import ch.zhaw.dna.ssh.mapreduce.model.framework.WorkerTask.State;
import ch.zhaw.dna.ssh.mapreduce.model.framework.impl.PooledMapRunnerFactory;
import ch.zhaw.dna.ssh.mapreduce.model.framework.impl.PooledReduceRunnerFactory;

/**
 * Dies ist ein neuer MapReduce Task. Er ist für seine Worker der Master.
 * 
 * @author Max, Reto, Desiree
 */
public final class MapReduceTask {

	private final MapRunnerFactory mapRunnerFactory;
	private final ReduceRunnerFactory reduceRunnerFactory;
	private final ConcurrentMap<String, Collection<String>> globalResultStructure;

	/**
	 * Erstellt einen neuen MapReduceTask mit den übergebenen map und reduce tasks.
	 * 
	 * @param mapTask
	 *            der Map-Task
	 * @param reduceTask
	 *            der Reduce-Task
	 */
	public MapReduceTask(MapTask mapTask, ReduceTask reduceTask) {
		this.mapRunnerFactory = new PooledMapRunnerFactory();
		this.reduceRunnerFactory = new PooledReduceRunnerFactory();
		this.globalResultStructure = new ConcurrentHashMap<String, Collection<String>>();

		this.mapRunnerFactory.assignMapTask(mapTask);
		this.reduceRunnerFactory.assignReduceTask(reduceTask);
		this.reduceRunnerFactory.setMaster(this);
	}

	/**
	 * Wendet auf alle Elemente vom übergebenen Iterator (via next) den Map- und Reduce-Task an. Die Methode blockiert,
	 * bis alle Aufgaben erledigt sind. Es wird über den Iterator iteriert und für jeden Aufruf von
	 * {@link Iterator#next()} ein Map-Task abgesetzt (asynchron). Aus diesem Grund könnten im Iterator die Werte auf
	 * lazy generiert werden.
	 * 
	 * @param inputs
	 *            der gesamte input als Iterator
	 * @return das Resultat von dem ganzen MapReduceTask
	 */
	public Map<String, Collection<String>> compute(Iterator<String> inputs) {
		List<MapRunner> mapRunners = new LinkedList<MapRunner>();
		while (inputs.hasNext()) {
			MapRunner mapRunner = mapRunnerFactory.create();
			mapRunners.add(mapRunner);
			mapRunner.runMapTask(inputs.next());
		}

		// TODO reduce runner müssen schon früher ausgegeben werden
		Map<String, ReduceRunner> reduceRunners = new HashMap<String, ReduceRunner>();
		while (!allWorkerTasksCompleted(mapRunners)) {
			for (MapRunner mapRunner : mapRunners) {
				for (String key : mapRunner.getKeysSnapshot()) {
					if (!reduceRunners.containsKey(key)) {
						ReduceRunner reduceRunner = this.reduceRunnerFactory.create(key);
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
	 * Fügt dem Map Reduce eine Combiner Task hinzu mit der man die Effizienz massiv steigern kann
	 * 
	 * @param task eine Combiner Task die die Ergebnisse bereits periodisch nach dem MAP aggregiert.
	 */
	public void addCombinerTask(CombinerTask task) {
		mapRunnerFactory.assignCombineTask(task);
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
