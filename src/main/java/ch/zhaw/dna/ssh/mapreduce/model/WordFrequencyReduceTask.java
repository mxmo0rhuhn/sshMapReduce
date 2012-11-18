package ch.zhaw.dna.ssh.mapreduce.model;

import java.util.Iterator;

import ch.zhaw.dna.ssh.mapreduce.model.framework.ReduceRunner;
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
	public void reduce(ReduceRunner reduceRunner, String key, Iterator<String> values) {

		System.out.println("I am a ReduceTask and I ran for " + key);

		Long value = 0L;

		while (values.hasNext()) {
			value += Long.parseLong(values.next());
		}
		reduceRunner.emit(value.toString());

	}

}
