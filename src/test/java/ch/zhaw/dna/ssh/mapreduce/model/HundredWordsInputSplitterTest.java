package ch.zhaw.dna.ssh.mapreduce.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;

import org.junit.Test;


public class HundredWordsInputSplitterTest {
	
	@Test
	public void shouldHandleEmptyInput() {
		Iterator<String> splitter = new HundredWordsInputSplitter(createStringWith(0));
		assertFalse(splitter.hasNext());
	}
	
	@Test
	public void shouldReturnNullWhenEmpty() {
		Iterator<String> splitter = new HundredWordsInputSplitter(createStringWith(0));
		assertNull(splitter.next());
	}
	
	@Test
	public void shouldSplitIntoOneSplit() {
		Iterator<String> splitter = new HundredWordsInputSplitter(createStringWith(1));
		assertTrue(splitter.hasNext());
		assertEquals(createStringWith(1), splitter.next());
		assertFalse(splitter.hasNext());
		assertNull(splitter.next());
	}
	
	@Test
	public void shouldSplitIntoOneSplit2() {
		Iterator<String> splitter = new HundredWordsInputSplitter(createStringWith(2));
		assertTrue(splitter.hasNext());
		assertEquals(createStringWith(2), splitter.next());
		assertFalse(splitter.hasNext());
	}
	
	@Test
	public void shouldSplitIntoOneSplit3() {
		String ninetyNineWords = createStringWith(99);
		Iterator<String> splitter = new HundredWordsInputSplitter(ninetyNineWords);
		assertTrue(splitter.hasNext());
		assertEquals(ninetyNineWords, splitter.next());
		assertFalse(splitter.hasNext());
		assertNull(splitter.next());
	}
	
	@Test
	public void shouldSplitIntoOneSplit4() {
		String hundredWords = createStringWith(100);
		Iterator<String> splitter = new HundredWordsInputSplitter(hundredWords);
		assertTrue(splitter.hasNext());
		assertEquals(hundredWords, splitter.next());
		assertFalse(splitter.hasNext());
	}
	
	@Test
	public void shouldSplitIntoTwoSplits() {
		String hundredAndOneWords = createStringWith(101);
		Iterator<String> splitter = new HundredWordsInputSplitter(hundredAndOneWords);
		assertTrue(splitter.hasNext());
		assertEquals(createStringWith(100), splitter.next());
		assertTrue(splitter.hasNext());
		assertEquals(createStringWith(1), splitter.next());
		assertFalse(splitter.hasNext());
	}
	
	@Test
	public void shouldSplitIntoTwoSplits2() {
		Iterator<String> splitter = new HundredWordsInputSplitter(createStringWith(102));
		assertTrue(splitter.hasNext());
		assertEquals(createStringWith(100), splitter.next());
		assertTrue(splitter.hasNext());
		assertEquals(createStringWith(2), splitter.next());
		assertFalse(splitter.hasNext());
	}
	
	@Test
	public void shouldSplitIntoTwoSplits3() {
		Iterator<String> splitter = new HundredWordsInputSplitter(createStringWith(199));
		assertTrue(splitter.hasNext());
		assertEquals(createStringWith(100), splitter.next());
		assertTrue(splitter.hasNext());
		assertEquals(createStringWith(99), splitter.next());
		assertFalse(splitter.hasNext());
	}
	
	@Test
	public void shouldSplitIntoTwoSplits4() {
		Iterator<String> splitter = new HundredWordsInputSplitter(createStringWith(200));
		assertTrue(splitter.hasNext());
		assertEquals(createStringWith(100), splitter.next());
		assertTrue(splitter.hasNext());
		assertEquals(createStringWith(100), splitter.next());
		assertFalse(splitter.hasNext());
	}
	
	@Test
	public void shouldSplitIntoThreeSplits() {
		Iterator<String> splitter = new HundredWordsInputSplitter(createStringWith(201));
		assertTrue(splitter.hasNext());
		assertEquals(createStringWith(100), splitter.next());
		assertTrue(splitter.hasNext());
		assertEquals(createStringWith(100), splitter.next());
		assertTrue(splitter.hasNext());
		assertEquals(createStringWith(1), splitter.next());
		assertFalse(splitter.hasNext());
	}
	
	@Test
	public void shouldSplitIntoFourSplits() {
		Iterator<String> splitter = new HundredWordsInputSplitter(createStringWith(400));
		assertTrue(splitter.hasNext());
		assertEquals(createStringWith(100), splitter.next());
		assertTrue(splitter.hasNext());
		assertEquals(createStringWith(100), splitter.next());
		assertTrue(splitter.hasNext());
		assertEquals(createStringWith(100), splitter.next());
		assertTrue(splitter.hasNext());
		assertEquals(createStringWith(100), splitter.next());
		assertFalse(splitter.hasNext());
	}
	
	
	private static String createStringWith(int numberOfWords) {
		StringBuilder sb = new StringBuilder(numberOfWords * 6);
		for (int i = 0; i < numberOfWords; i ++) {
			if (sb.length() != 0) {
				sb.append(' ');
			}
			sb.append("hello");
		}
		return sb.toString();
	}

}
