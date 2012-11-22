package ch.zhaw.dna.ssh.mapreduce.model;

import java.util.Iterator;

import ch.zhaw.dna.ssh.mapreduce.model.framework.CombinerTask;

/**
 * Dieser Task summiert die Values zu einem Key bereits nach dem Map Task. Dies reduziert den Aufwand für die Reduce Tasks erheblich.
 * 
 * @author Max
 * 
 */
public class WordFrequencyCombinerTask implements CombinerTask {

	/** {@inheritDoc} */
	@Override
	public String combine(Iterator<String> toCombine) {
		int sum = 0;
		while (toCombine.hasNext()) {
			sum += Integer.parseInt(toCombine.next());
		}
		return "" + sum;
	}

}
