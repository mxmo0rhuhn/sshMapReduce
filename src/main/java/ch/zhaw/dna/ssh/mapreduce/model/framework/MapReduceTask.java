package ch.zhaw.dna.ssh.mapreduce.model.framework;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import ch.zhaw.dna.ssh.mapreduce.model.framework.impl.PooledMapRunnerFactory;

/**
 * Dies ist ein neuer MapReduce Task. Er ist für seine Worker der Master.
 * 
 * @author Max
 */
public final class MapReduceTask {

	private final ReduceTask reduceTask;

	private final MapRunnerFactory mapRunnerFactory;
	
	public MapReduceTask(MapTask mapTask, ReduceTask reduceTask) {
		this.reduceTask = reduceTask;
		this.mapRunnerFactory = new PooledMapRunnerFactory();
		this.mapRunnerFactory.assignMapTask(mapTask);
	}

	public Map<String, List<String>> compute(String input) {
		InputSplitter inputSplitter = InputSplitterFactory.create(input);
		List<MapRunner> mapRunners = new LinkedList<MapRunner>();
		while (inputSplitter.hasNext()) {
			MapRunner mapRunner = mapRunnerFactory.getMapRunner();
			mapRunners.add(mapRunner);
			mapRunner.runMapTask(inputSplitter.next());
		}
		
		
		
		
		
		Map<String, List<String>> results = new HashMap<String, List<String>>();
//		ReduceRunner reduceRunner = this.reduceRunnerFactory.createReduceRunner();
//		reduceRunner.reduce(.getIntermediate());
		return results;
	}

}
