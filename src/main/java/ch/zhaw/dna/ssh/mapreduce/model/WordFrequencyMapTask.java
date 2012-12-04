package ch.zhaw.dna.ssh.mapreduce.model;

import ch.zhaw.dna.ssh.mapreduce.model.framework.MapEmitter;
import ch.zhaw.dna.ssh.mapreduce.model.framework.MapInstruction;

/**
 * Splittet jedes Wort und Emit'ed es an MapRunner
 * 
 * @author des
 * 
 */
public class WordFrequencyMapTask implements MapInstruction {

	/** {@inheritDoc} */
	@Override
	public void map(MapEmitter emitter, String input) {
		for (String s : input.trim().split(" ")) {
			emitter.emitIntermediateMapResult(s, "1");
		}
	}

}
