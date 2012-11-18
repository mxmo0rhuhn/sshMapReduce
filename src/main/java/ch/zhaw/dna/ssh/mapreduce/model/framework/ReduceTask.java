package ch.zhaw.dna.ssh.mapreduce.model.framework;

import java.util.Iterator;

public interface ReduceTask extends Task {
	
	public void reduce(ReduceRunner reduceRunner, String key, Iterator<String> values);

}
