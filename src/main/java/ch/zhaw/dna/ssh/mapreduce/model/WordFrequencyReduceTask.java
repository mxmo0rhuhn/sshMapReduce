package ch.zhaw.dna.ssh.mapreduce.model;

import java.util.Iterator;

import ch.zhaw.dna.ssh.mapreduce.model.framework.ReduceEmitter;
import ch.zhaw.dna.ssh.mapreduce.model.framework.ReduceTask;

/**
 * Reduced den Input und zaehlt ihn zusammen, emit'ed Summe
 * 
 * @author des
 * 
 */
public class WordFrequencyReduceTask implements ReduceTask {

	/** {@inheritDoc} */
	@Override
	public void reduce(ReduceEmitter emitter, String key, Iterator<String> values) {

		Long value = 0L;

		while (values.hasNext()) {
			value += Long.parseLong(values.next());
		}
		emitter.emit(value.toString());

	}

}
