package ch.zhaw.dna.ssh.mapreduce.model.framework;

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

	public void compute(String input) {
		MapRunner mapRunenr = new MapRunner();
		mapRunenr.assignMaster(this.mapTask);
		mapRunenr.runMapTask(input.split(" "), this.p);
		ReduceRunner reduceRunner = new ReduceRunner();
	}

}
