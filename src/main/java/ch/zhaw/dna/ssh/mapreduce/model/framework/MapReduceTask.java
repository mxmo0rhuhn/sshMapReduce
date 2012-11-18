package ch.zhaw.dna.ssh.mapreduce.model.framework;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import ch.zhaw.dna.ssh.mapreduce.model.framework.WorkerTask.State;
import ch.zhaw.dna.ssh.mapreduce.model.framework.impl.PooledMapRunnerFactory;
import ch.zhaw.dna.ssh.mapreduce.model.framework.impl.PooledReduceRunnerFactory;

/**
 * Dies ist ein neuer MapReduce Task. Er ist für seine Worker der Master.
 * 
 * @author Max
 */
public final class MapReduceTask {

	private final MapRunnerFactory mapRunnerFactory;

	private final ReduceRunnerFactory reduceRunnerFactory;

	private final ConcurrentMap<String, List<String>> globalResultStructure;
	
	public MapReduceTask(MapTask mapTask, ReduceTask reduceTask) {
		this.mapRunnerFactory = new PooledMapRunnerFactory();
		this.reduceRunnerFactory = new PooledReduceRunnerFactory();
		this.globalResultStructure = new ConcurrentHashMap<String, List<String>>();

		this.mapRunnerFactory.assignMapTask(mapTask);
		this.reduceRunnerFactory.assignReduceTask(reduceTask);
		this.reduceRunnerFactory.setGlobalResultStructure(this.globalResultStructure);
	}

	public Map<String, List<String>> compute(Iterator<String> inputs) {
		List<MapRunner> mapRunners = new LinkedList<MapRunner>();
		while (inputs.hasNext()) {
			MapRunner mapRunner = mapRunnerFactory.getMapRunner();
			mapRunners.add(mapRunner);
			mapRunner.runMapTask(inputs.next());
		}

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
	 * Iteriert ueber alle workers und prueft, ob alle fertig sind.
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
