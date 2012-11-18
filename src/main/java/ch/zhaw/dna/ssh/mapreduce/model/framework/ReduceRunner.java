package ch.zhaw.dna.ssh.mapreduce.model.framework;

import java.util.List;

public interface ReduceRunner extends WorkerTask {

	void reduce(List<MapRunner> mapRunners);
	
	void emit(String result);

} 
