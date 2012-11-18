package ch.zhaw.dna.ssh.mapreduce.model;

import ch.zhaw.dna.ssh.mapreduce.model.framework.*;
import org.junit.Test;

public class WordFrequencyMapTaskTest {
	
	@Test
	public void shouldMapInput(){
		String testString = "hallo welt ich teste dich hallo";
		
		MapRunner runner = new MapRunner();
		
		WordFrequencyMapTask test = new WordFrequencyMapTask();
		test.map(runner, testString);
	}

}
