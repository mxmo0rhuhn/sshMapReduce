package ch.zhaw.dna.ssh.mapreduce.model.framework;

import java.util.List;

public interface ReduceRunner {

	void reduce(List<MapRunner> mapRunners);
	
	void emit(String result);

	boolean isCompleted();

} 
