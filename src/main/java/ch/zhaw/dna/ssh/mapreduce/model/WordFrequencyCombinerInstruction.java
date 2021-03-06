package ch.zhaw.dna.ssh.mapreduce.model;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import ch.zhaw.mapreduce.CombinerInstruction;
import ch.zhaw.mapreduce.KeyValuePair;

/**
 * Dieser Task summiert die Values zu einem Key bereits nach dem Map Task. Dies reduziert den Aufwand für die Reduce Tasks erheblich.
 * 
 * @author Max
 * 
 */
public class WordFrequencyCombinerInstruction implements CombinerInstruction {

	/** {@inheritDoc} */
	@Override 
	public List<KeyValuePair> combine(Iterator<KeyValuePair> toCombine) {
		Map<String, KeyValuePair<String, String>> combinedKeyValuePairs = new HashMap<String, KeyValuePair<String, String>>(); 
		
		while (toCombine.hasNext()) {
			KeyValuePair<String, String> currentKeyValuePair = (KeyValuePair<String, String>) toCombine.next();
			if(combinedKeyValuePairs.containsKey(currentKeyValuePair.getKey())) {
				KeyValuePair<String, String> formerKeyValuePair = (KeyValuePair<String, String>) combinedKeyValuePairs.get(currentKeyValuePair.getKey());
				combinedKeyValuePairs.remove(currentKeyValuePair.getKey());
				
				combinedKeyValuePairs.put( currentKeyValuePair.getKey(), new KeyValuePair<String, String>(currentKeyValuePair.getKey(), "" + 1 + Long.parseLong(formerKeyValuePair.getValue())));
				
			} else {
				combinedKeyValuePairs.put(currentKeyValuePair.getKey(), currentKeyValuePair);
			}
		}
		
		return new LinkedList<KeyValuePair>(combinedKeyValuePairs.values());
	}

}
