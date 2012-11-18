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

	Pool p = new Pool(); // TODO Scope
	
	

	public MapReduceTask(MapTask mapTask, ReduceTask reduceTask) {
		this.mapTask = mapTask;
		this.reduceTask = reduceTask;
	}

	public Map<String, List<String>> compute(String input) {
		MapRunner mapRunenr = new MapRunner();
		mapRunenr.assignMaster(this.mapTask);
		mapRunenr.runMapTask(input.split(" "), this.p);
		Map<String, List<String>> results = new HashMap<String, List<String>>();
		ReduceRunner reduceRunner = null;
		reduceRunner.reduce(mapRunenr.getIntermediate());
		return results;
	}

}
