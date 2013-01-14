package ch.zhaw.dna.ssh.mapreduce.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;

import org.junit.Test;

public class WordsInputSplitterTest {

	@Test
	public void shouldHandleEmptyInput() {
		Iterator<String> splitter = new WordsInputSplitter(createStringWith(0),100);
		assertFalse(splitter.hasNext());
	}

	@Test
	public void shouldReturnNullWhenEmpty() {
		Iterator<String> splitter = new WordsInputSplitter(createStringWith(0),1000);
		assertNull(splitter.next());
	}

	@Test
	public void shouldSplitIntoOneSplit() {
		Iterator<String> splitter = new WordsInputSplitter(createStringWith(1),1000);
		assertTrue(splitter.hasNext());
		assertEquals(createStringWith(1), splitter.next());
		assertFalse(splitter.hasNext());
		assertNull(splitter.next());
	}

	@Test
	public void shouldSplitIntoOneSplit2() {
		Iterator<String> splitter = new WordsInputSplitter(createStringWith(2), 2000);
		assertTrue(splitter.hasNext());
		assertEquals(createStringWith(2), splitter.next());
		assertFalse(splitter.hasNext());
	}

	@Test
	public void shouldSplitIntoOneSplit3() {
		String ninetyNineWords = createStringWith(99);
		Iterator<String> splitter = new WordsInputSplitter(ninetyNineWords,100);
		assertTrue(splitter.hasNext());
		assertEquals(ninetyNineWords, splitter.next());
		assertFalse(splitter.hasNext());
		assertNull(splitter.next());
	}

	@Test
	public void shouldSplitIntoOneSplit4() {
		String hundredWords = createStringWith(100);
		Iterator<String> splitter = new WordsInputSplitter(hundredWords,100);
		assertTrue(splitter.hasNext());
		assertEquals(hundredWords, splitter.next());
		assertFalse(splitter.hasNext());
	}

	@Test
	public void shouldSplitIntoTwoSplits() {
		String hundredAndOneWords = createStringWith(101);
		Iterator<String> splitter = new WordsInputSplitter(hundredAndOneWords, 100);
		assertTrue(splitter.hasNext());
		assertEquals(createStringWith(100), splitter.next());
		assertTrue(splitter.hasNext());
		assertEquals(createStringWith(1), splitter.next());
		assertFalse(splitter.hasNext());
	}

	@Test
	public void shouldSplitIntoTwoSplits2() {
		Iterator<String> splitter = new WordsInputSplitter(createStringWith(102),100);
		assertTrue(splitter.hasNext());
		assertEquals(createStringWith(100), splitter.next());
		assertTrue(splitter.hasNext());
		assertEquals(createStringWith(2), splitter.next());
		assertFalse(splitter.hasNext());
	}

	@Test
	public void shouldSplitIntoTwoSplits3() {
		Iterator<String> splitter = new WordsInputSplitter(createStringWith(199),100);
		assertTrue(splitter.hasNext());
		assertEquals(createStringWith(100), splitter.next());
		assertTrue(splitter.hasNext());
		assertEquals(createStringWith(99), splitter.next());
		assertFalse(splitter.hasNext());
	}

	@Test
	public void shouldSplitIntoTwoSplits4() {
		Iterator<String> splitter = new WordsInputSplitter(createStringWith(200),100);
		assertTrue(splitter.hasNext());
		assertEquals(createStringWith(100), splitter.next());
		assertTrue(splitter.hasNext());
		assertEquals(createStringWith(100), splitter.next());
		assertFalse(splitter.hasNext());
	}

	@Test
	public void shouldSplitIntoThreeSplits() {
		Iterator<String> splitter = new WordsInputSplitter(createStringWith(201),100);
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
		Iterator<String> splitter = new WordsInputSplitter(createStringWith(400),100);
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
		for (int i = 0; i < numberOfWords; i++) {
			if (sb.length() != 0) {
				sb.append(' ');
			}
			sb.append("hello");
		}
		return sb.toString();
	}

}
