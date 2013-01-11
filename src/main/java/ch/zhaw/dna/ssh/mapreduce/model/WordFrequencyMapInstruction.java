package ch.zhaw.dna.ssh.mapreduce.model;

import ch.zhaw.mapreduce.MapEmitter;
import ch.zhaw.mapreduce.MapInstruction;

/**
 * Splittet jedes Wort und Emit'ed es an MapWorkerTask
 * 
 * @author des
 * 
 */
public class WordFrequencyMapInstruction implements MapInstruction {

	/** {@inheritDoc} */
	@Override
	public void map(MapEmitter emitter, String input) {
		for (String s : input.trim().split(" ")) {
			emitter.emitIntermediateMapResult(s, "1");
		}
	}

}
