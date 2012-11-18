package ch.zhaw.dna.ssh.mapreduce.model.framework;

import java.util.List;

public interface ReduceRunner extends WorkerTask {

	void runReduceTask(List<MapRunner> mapRunners);
	
	void emit(String result);

} 
