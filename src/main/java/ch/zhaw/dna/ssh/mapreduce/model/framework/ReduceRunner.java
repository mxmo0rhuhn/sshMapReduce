package ch.zhaw.dna.ssh.mapreduce.model.framework;

import java.util.Map;

public interface ReduceRunner {

	void reduce(Map<String, String> intermediate);
}
