package ch.zhaw.dna.ssh.mapreduce.model.framework;

public interface RunnerFactory {
	
	MapRunner createMapRunner(MapTask mapTask, CombinerTask combinerTask);
	
	ReduceRunner createReduceRunner(ReduceTask reduceTask);

}
