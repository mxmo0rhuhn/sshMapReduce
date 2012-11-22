package ch.zhaw.dna.ssh.mapreduce.model;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

/**
 * Testet den combiner fürs Wörter Zählen
 * @author Max
 *
 */
public class WordFrequencyCombinerTaskTest {

	@Test
	public void oneValueTest(){
		Assert.assertTrue("1".equals(new WordFrequencyCombinerTask().combine(Arrays.asList("1").iterator())));
	}
	@Test
	public void twoValuesTest(){
		Assert.assertTrue("3".equals(new WordFrequencyCombinerTask().combine(Arrays.asList("1", "2").iterator())));
	}
	@Test
	public void threValuesTest(){
		Assert.assertTrue("5".equals(new WordFrequencyCombinerTask().combine(Arrays.asList("2", "1", "2").iterator())));
	}
	@Test
	public void tenValuesTest(){
		Assert.assertTrue("20".equals(new WordFrequencyCombinerTask().combine(Arrays.asList("2", "1", "2", "3", "2", "3", "2", "2", "2", "1").iterator())));
	}
	
}
