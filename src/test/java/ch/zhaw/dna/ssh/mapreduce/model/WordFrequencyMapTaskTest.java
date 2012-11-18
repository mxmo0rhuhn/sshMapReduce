package ch.zhaw.dna.ssh.mapreduce.model;


import static org.junit.Assert.assertEquals;
import ch.zhaw.dna.ssh.mapreduce.model.framework.*;


import org.junit.Before;
import org.junit.Test;

public class WordFrequencyMapTaskTest {
	
    /** jmock context */

    /** instance under test */

    
    @Before
    public void setUp() {

    	String testString = "hallo welt ich teste dich hallo";
    	
    	
    	
    }
	
	@Test
	public void shouldMapInput(){

		
		/*	
		MapRunner runner = new MapRunner();
		*/
		 
		WordFrequencyMapTask test = new WordFrequencyMapTask();
		//test.map(runner, testString); 
		
		//erwartet Strings (keys) und "1" zurück für jedes Wort
		//assertEquals("Ergibt Ausgabe", 'hallo, "1"', );

	}

}
