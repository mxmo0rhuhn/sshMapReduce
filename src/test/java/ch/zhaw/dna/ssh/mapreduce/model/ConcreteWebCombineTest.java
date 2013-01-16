package ch.zhaw.dna.ssh.mapreduce.model;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

import ch.zhaw.mapreduce.KeyValuePair;

public class ConcreteWebCombineTest {

	@Test
	public void shouldBeReusable() {
		ConcreteWebCombine combiner = new ConcreteWebCombine();
		Iterator<KeyValuePair> input1 = it(new KeyValuePair("key1", "val2"), new KeyValuePair("key1", "val2"));
		Iterator<KeyValuePair> input2 = it(new KeyValuePair("key2", "val2"), new KeyValuePair("key2", "val2"));
		List<KeyValuePair> result1 = combiner.combine(input1);
		List<KeyValuePair> result2 = combiner.combine(input2);
		assertEquals(li(new KeyValuePair("key1", "val2 val2")), result1);
		assertEquals(li(new KeyValuePair("key2", "val2 val2")), result2);
	}
	
	@Test
	public void shouldNotCombineDifferent() {
		ConcreteWebCombine combiner = new ConcreteWebCombine();
		Iterator<KeyValuePair> input = it(new KeyValuePair("key1", "val2"), new KeyValuePair("key2", "val2"));
		List<KeyValuePair> result = combiner.combine(input);
		assertEquals(li(new KeyValuePair("key1", "val2"), new KeyValuePair("key2", "val2")), result);
	}
	
	@Test
	public void shouldCombineSame() {
		ConcreteWebCombine combiner = new ConcreteWebCombine();
		Iterator<KeyValuePair> input = it(new KeyValuePair("key1", "val2"), new KeyValuePair("key1", "val2"));
		List<KeyValuePair> result = combiner.combine(input);
		assertEquals(li(new KeyValuePair("key1", "val2 val2")), result);
	}
	
	@Test
	public void shouldNotInsertSpaceForOne() {
		ConcreteWebCombine combiner = new ConcreteWebCombine();
		Iterator<KeyValuePair> input = it(new KeyValuePair("key1", "val2"));
		List<KeyValuePair> result = combiner.combine(input);
		assertEquals(li(new KeyValuePair("key1", "val2")), result);
	}
	
	@Test
	public void shouldInsertSpaceForThree() {
		ConcreteWebCombine combiner = new ConcreteWebCombine();
		Iterator<KeyValuePair> input = it(new KeyValuePair("key1", "val1"), new KeyValuePair("key1", "val2"), new KeyValuePair("key1", "val3"));
		List<KeyValuePair> result = combiner.combine(input);
		assertEquals(li(new KeyValuePair("key1", "val1 val2 val3")), result);
	}
	
	@Test
	public void shouldPartiallyCombine() {
		ConcreteWebCombine combiner = new ConcreteWebCombine();
		Iterator<KeyValuePair> input = it(new KeyValuePair("key1", "val1"), new KeyValuePair("key2", "val2"), new KeyValuePair("key1", "val3"));
		List<KeyValuePair> result = combiner.combine(input);
		assertEquals(li(new KeyValuePair("key1", "val1 val3"), new KeyValuePair("key2", "val2")), result);
	}
	
	private List<KeyValuePair> li(KeyValuePair ... pairs) {
		return Arrays.asList(pairs);
	}
	
	private Iterator<KeyValuePair> it(KeyValuePair ... pairs) {
		return Arrays.asList(pairs).iterator();
	}
}
