package ch.zhaw.dna.ssh.mapreduce.model.filter;

import static org.junit.Assert.*;

import org.junit.Test;

public class LocalFilterTest {
	
	private LocalFilter localFilter = new LocalFilter();
	
	@Test
	public void shouldAcceptRealtiveUrls() {
		assertTrue(localFilter.accept("index.html"));
	}
	
	@Test
	public void shouldAcceptAbsoluteUrls() {
		assertTrue(localFilter.accept("http://www.google.com"));
	}
	
	@Test
	public void shouldAcceptAbsoluteUrlWithFile() {
		assertTrue(localFilter.accept("http://www.google.com/index.html"));
	}
	
	@Test
	public void shouldAcceptAbsoluteUrlLocalReference() {
		assertTrue(localFilter.accept("http://www.google.com/index.html#content"));
	}
	
	@Test
	public void shouldNotAcceptLocalReferences() {
		assertFalse(localFilter.accept("#ref-cite"));
	}

}
