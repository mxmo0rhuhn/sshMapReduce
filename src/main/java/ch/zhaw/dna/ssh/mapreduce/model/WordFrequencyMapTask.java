package ch.zhaw.dna.ssh.mapreduce.model;

import ch.zhaw.dna.ssh.mapreduce.model.framework.MapRunner;
import ch.zhaw.dna.ssh.mapreduce.model.framework.MapTask;

/**
 * Splittet jedes Wort und Emit'ed es an MapRunner
 * 
 * @author des
 * 
 */
public class WordFrequencyMapTask implements MapTask {

	/** {@inheritDoc} */
	@Override
	public void map(MapRunner mapRunner, String input) {
		System.out.println("I am a Map Task and I ran for " + input);
		for (String s : input.trim().split(" ")) {
			mapRunner.emitIntermediateMapResult(s, "1");
		}
	}

}
