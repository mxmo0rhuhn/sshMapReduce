package ch.zhaw.dna.ssh.mapreduce.model;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import ch.zhaw.dna.ssh.mapreduce.model.framework.CombinerInstruction;
import ch.zhaw.dna.ssh.mapreduce.model.framework.KeyValuePair;

/**
 * Dieser Task summiert die Values zu einem Key bereits nach dem Map Task. Dies reduziert den Aufwand für die Reduce Tasks erheblich.
 * 
 * @author Max
 * 
 */
public class WordFrequencyCombinerTask implements CombinerInstruction {

	/** {@inheritDoc} */
	@Override
	public List<KeyValuePair> combine(Iterator<KeyValuePair> toCombine) {
		Map<String, KeyValuePair> combinedKeyValuePairs = new HashMap<String, KeyValuePair>(); 
		
		while (toCombine.hasNext()) {
			KeyValuePair currentKeyValuePair = toCombine.next();
			if(combinedKeyValuePairs.containsKey(currentKeyValuePair.getKey())) {
				KeyValuePair formerKeyValuePair = combinedKeyValuePairs.get(currentKeyValuePair.getKey());
				combinedKeyValuePairs.remove(currentKeyValuePair.getKey());
				
				combinedKeyValuePairs.put(currentKeyValuePair.getKey(), new KeyValuePair(currentKeyValuePair.getKey(), "" + 1 + Integer.parseInt(formerKeyValuePair.getValue())));
				
			} else {
				combinedKeyValuePairs.put(currentKeyValuePair.getKey(), currentKeyValuePair);
			}
		}
		
		return new LinkedList<KeyValuePair>(combinedKeyValuePairs.values());
	}

}
