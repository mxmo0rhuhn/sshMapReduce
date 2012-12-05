package ch.zhaw.dna.ssh.mapreduce.model;

import java.util.Iterator;

import ch.zhaw.dna.ssh.mapreduce.model.framework.KeyValuePair;
import ch.zhaw.dna.ssh.mapreduce.model.framework.ReduceEmitter;
import ch.zhaw.dna.ssh.mapreduce.model.framework.ReduceInstruction;

/**
 * Reduced den Input und zaehlt ihn zusammen, emit'ed Summe
 * 
 * @author des
 * 
 */
public class WordFrequencyReduceTask implements ReduceInstruction {

	/** {@inheritDoc} */
	@Override
	public void reduce(ReduceEmitter emitter, String key, Iterator<KeyValuePair> values) {
		Long value = 0L;

		while (values.hasNext()) {
			value += Long.parseLong(values.next().getValue());
		}
		emitter.emit(value.toString());
	}

}
