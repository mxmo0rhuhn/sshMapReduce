package ch.zhaw.dna.ssh.mapreduce.model;

import java.util.Arrays;
import java.util.Iterator;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

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
		
		WordFrequencyReduceTask test = new WordFrequencyReduceTask();
		test.reduce(runner, "hallo", createIterator("1","1"));
		test.reduce(runner, "welt", createIterator("1"));
		test.reduce(runner, "ich", createIterator("1"));
		test.reduce(runner, "teste", createIterator("1"));
		test.reduce(runner, "dich", createIterator("1"));
		
	}

	
	private Iterator<String> createIterator(String...values){
		return Arrays.asList(values).iterator();
		
	}
}
