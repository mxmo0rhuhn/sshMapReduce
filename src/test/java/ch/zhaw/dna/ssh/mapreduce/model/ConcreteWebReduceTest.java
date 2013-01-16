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

import ch.zhaw.mapreduce.KeyValuePair;
import ch.zhaw.mapreduce.ReduceEmitter;

@RunWith(JMock.class)
public class ConcreteWebReduceTest {

	private Mockery context;

	@Before
	public void initMock() {
		this.context = new JUnit4Mockery();
	}

	@Test
	public void shouldConcatenate() {
		ConcreteWebReduce reducer = new ConcreteWebReduce();
		final ReduceEmitter emitter = this.context.mock(ReduceEmitter.class);
		this.context.checking(new Expectations() {
			{
				oneOf(emitter).emit("foo bar buz");
			}
		});
		Iterator<KeyValuePair> input = Arrays.asList(
				new KeyValuePair[] { new KeyValuePair("key", "foo bar"), new KeyValuePair("key", "buz") }).iterator();
		reducer.reduce(emitter, "key", input);
	}

	@Test
	public void shouldBeReusable() {
		ConcreteWebReduce reducer = new ConcreteWebReduce();
		final ReduceEmitter emitter = this.context.mock(ReduceEmitter.class);
		this.context.checking(new Expectations() {
			{
				oneOf(emitter).emit("foo bar buz");
				oneOf(emitter).emit("hello fucking world");
			}
		});
		Iterator<KeyValuePair> input1 = Arrays.asList(
				new KeyValuePair[] { new KeyValuePair("key", "foo bar"), new KeyValuePair("key", "buz") }).iterator();
		Iterator<KeyValuePair> input2 = Arrays
				.asList(new KeyValuePair[] { new KeyValuePair("key", "hello fucking"), new KeyValuePair("key", "world") })
				.iterator();
		reducer.reduce(emitter, "key1", input1);
		reducer.reduce(emitter, "key2", input2);
	}

}
