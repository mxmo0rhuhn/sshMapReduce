package ch.zhaw.dna.ssh.mapreduce.model.framework;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

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

	public Map<String, List<String>> compute(String input) {
		InputSplitter inputSplitter = InputSplitterFactory.create(input);
		List<MapRunner> mapRunners = new LinkedList<MapRunner>();
		while (inputSplitter.hasNext()) {
			MapRunner mapRunner = mapRunnerFactory.getMapRunner();
			mapRunners.add(mapRunner);
			mapRunner.runMapTask(inputSplitter.next());
		}

		Map<String, ReduceRunner> reduceRunners = new HashMap<String, ReduceRunner>();
		while (!allMapRunnersCompleted(mapRunners)) {
			for (MapRunner mapRunner : mapRunners) {
				for (String key : mapRunner.getKeysSnapshot()) {
					if (!reduceRunners.containsKey(key)) {
						ReduceRunner reduceRunner = this.reduceRunnerFactory.create();
						reduceRunner.reduce(mapRunners);
						reduceRunners.put(key, reduceRunner);
					}
				}
			}
		}
		while (!allReduceRunnersCompleted(reduceRunners)) {
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
	 * Iteriert ueber alle MapRunner und prueft, ob alle fertig sind.
	 * 
	 * @param mapRunners
	 * @return true, wenn alle fertig sind, sonst false.
	 */
	private boolean allMapRunnersCompleted(List<MapRunner> mapRunners) {
		for (MapRunner mapRunner : mapRunners) {
			if (!mapRunner.isCompleted()) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Iteriert ueber alle MapRunner und prueft, ob alle fertig sind.
	 * 
	 * @param mapRunners
	 * @return true, wenn alle fertig sind, sonst false.
	 */
	private boolean allReduceRunnersCompleted(Map<String, ReduceRunner> reduceRunners) {
		for (Entry<String, ReduceRunner> entry : reduceRunners.entrySet()) {
			if (!entry.getValue().isCompleted()) {
				return false;
			}
		}
		return true;
	}

}
