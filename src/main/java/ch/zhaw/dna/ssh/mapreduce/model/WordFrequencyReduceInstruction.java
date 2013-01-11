package ch.zhaw.dna.ssh.mapreduce.model;

import java.util.Iterator;

import ch.zhaw.mapreduce.KeyValuePair;
import ch.zhaw.mapreduce.ReduceEmitter;
import ch.zhaw.mapreduce.ReduceInstruction;

/**
 * Reduced den Input und zaehlt ihn zusammen, emit'ed Summe
 * 
 * @author des
 * 
 */
public class WordFrequencyReduceInstruction implements ReduceInstruction {

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
