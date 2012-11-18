package ch.zhaw.dna.ssh.mapreduce.model.framework;

import java.util.List;
import java.util.concurrent.ConcurrentMap;

public interface ReduceRunnerFactory {

	void assignReduceTask(ReduceTask reduceTask);
	
	ReduceRunner create();

	void setGlobalResultStructure(ConcurrentMap<String, List<String>> globalResultStructure); 

}
