package ch.zhaw.dna.ssh.mapreduce.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import ch.zhaw.mapreduce.CombinerInstruction;
import ch.zhaw.mapreduce.KeyValuePair;

public class ConcreteWebCombine implements CombinerInstruction {

	private final Map<String, StringBuilder> concatenatedStrings = new HashMap<String, StringBuilder>();

	@Override
	public List<KeyValuePair> combine(Iterator<KeyValuePair> toCombine) {

		// TODO geht bessere Performance?

		while (toCombine.hasNext()) {
			KeyValuePair currentKeyValuePair = toCombine.next();
			if (concatenatedStrings.containsKey(currentKeyValuePair.getKey())) {
				if (concatenatedStrings.get(currentKeyValuePair.getKey()).length() != 0) {
					concatenatedStrings.get(currentKeyValuePair.getKey()).append(' ');
				}
				concatenatedStrings.get(currentKeyValuePair.getKey()).append(currentKeyValuePair.getValue());

			} else {
				concatenatedStrings
						.put(currentKeyValuePair.getKey(), new StringBuilder(currentKeyValuePair.getValue()));
			}

		}
		List<KeyValuePair> returnValues = new ArrayList<KeyValuePair>();
		for (String key : concatenatedStrings.keySet()) {
			returnValues.add(new KeyValuePair(key, concatenatedStrings.get(key).toString()));
		}
		return returnValues;
	}
}
