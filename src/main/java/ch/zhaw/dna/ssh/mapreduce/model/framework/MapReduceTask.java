package ch.zhaw.dna.ssh.mapreduce.model.framework;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Dies ist ein neuer MapReduce Task. Er ist für seine Worker der Master.
 * 
 * @author Max
 */
public class MapReduceTask {

	MapTask mapTask;

	ReduceTask reduceTask;

	MapRunnerFactory myFactory;
	
	Pool p = new Pool(); // TODO Scope
	
	

	public MapReduceTask(MapTask mapTask, ReduceTask reduceTask) {
		this.mapTask = mapTask;
		this.reduceTask = reduceTask;
		myFactory = new PooledMapRunnerFactory();
		myFactory.assignMapTask(mapTask);
		
	}

	public Map<String, List<String>> compute(String input) {
		MapRunner mapRunenr = myFactory.getMapRunner();
		mapRunenr.runMapTask(input.split(" "), this.p);
		Map<String, List<String>> results = new HashMap<String, List<String>>();
		ReduceRunner reduceRunner = null;
		reduceRunner.reduce(mapRunenr.getIntermediate());
		return results;
	}

}
