package ch.zhaw.dna.ssh.mapreduce.model.framework;

public interface WorkerTaskFactory {
	
	MapWorkerTask createMapRunner(MapInstruction mapTask, CombinerInstruction combinerTask);
	
	ReduceWorkerTask createReduceRunner(ReduceInstruction reduceTask);

}
