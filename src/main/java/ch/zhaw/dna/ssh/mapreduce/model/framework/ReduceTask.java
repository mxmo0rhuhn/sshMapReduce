package ch.zhaw.dna.ssh.mapreduce.model.framework;

import java.util.List;

public interface ReduceTask extends Task {
	
	public void reduce(ReduceRunner reduceRunner, List<MapRunner> mapRunners);

}
