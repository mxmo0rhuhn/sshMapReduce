package ch.zhaw.dna.ssh.mapreduce.model;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import ch.zhaw.dna.ssh.mapreduce.model.framework.KeyValuePair;

/**
 * Testet den combiner fürs Wörter Zählen
 * 
 * @author Max
 * 
 */
public class WordFrequencyCombinerInstructionTest {

	private final WordFrequencyCombinerInstruction combiner = new WordFrequencyCombinerInstruction();

	@Test
	public void oneValueTest() {
		List<KeyValuePair> pairs = new ArrayList<KeyValuePair>();
		pairs.add(new KeyValuePair("key", "1"));
		pairs.add(new KeyValuePair("key", "2"));
		List<KeyValuePair> res = combiner.combine(pairs.iterator());
		assertEquals(1, res.size());
	}

}
