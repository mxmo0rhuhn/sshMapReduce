package ch.zhaw.dna.ssh.mapreduce.model.framework;

public interface ReduceTask extends Task {
	
	public void reduce(ReduceRunner reduceRunner, String[] input);

}
