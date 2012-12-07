package ch.zhaw.dna.ssh.mapreduce.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.zhaw.dna.ssh.mapreduce.model.framework.KeyValuePair;
import ch.zhaw.dna.ssh.mapreduce.model.framework.ReduceEmitter;

@RunWith(JMock.class)
public class WordFrequencyReduceTaskTest {
	
	/** jmock context */
	Mockery context;

	@Before
	public void setUp() {
		context = new JUnit4Mockery();
	}

	
	@Test
	public void shouldReduceInput() {
		
		final ReduceEmitter runner = context.mock(ReduceEmitter.class);
		
		context.checking(new Expectations() {
			{
				one(runner).emit("2");
				one(runner).emit("1");
				one(runner).emit("1");
				one(runner).emit("1");
				one(runner).emit("1");
			}
			
		});
		
		WordFrequencyReduceInstruction test = new WordFrequencyReduceInstruction();
		test.reduce(runner, "hallo", createIterator("hallo", "1","1"));
		test.reduce(runner, "welt", createIterator("welt", "1"));
		test.reduce(runner, "ich", createIterator("ich", "1"));
		test.reduce(runner, "teste", createIterator("test", "1"));
		test.reduce(runner, "dich", createIterator("dich", "1"));
		
	}

	
	private Iterator<KeyValuePair> createIterator(String key, String...values){
		List<KeyValuePair> pairs = new ArrayList<KeyValuePair>();
		for (String val : values) {
			pairs.add(new KeyValuePair(key, val));
		}
		return pairs.iterator();
	}
}
