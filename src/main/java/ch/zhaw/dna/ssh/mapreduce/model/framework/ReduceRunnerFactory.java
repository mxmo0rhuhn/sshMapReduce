package ch.zhaw.dna.ssh.mapreduce.model.framework;


public interface ReduceRunnerFactory {

	void assignReduceTask(ReduceTask reduceTask);
	
	ReduceRunner create(String forKey);

	void setMaster(MapReduceTask master); 
}
