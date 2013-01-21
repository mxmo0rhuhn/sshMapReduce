package ch.zhaw.dna.ssh.mapreduce.model.filter;

import static org.junit.Assert.*;

import org.junit.Test;

public class AllFilterTest {
	
	@Test
	public void shouldAcceptNeither() {
		AllFilter andFilter = new AllFilter(new ImageFilter(), new LocalFilter());
		
		assertFalse(andFilter.accept("image.png"));
		assertFalse(andFilter.accept("www.google.com/page/sub/favicon.ico"));
		assertFalse(andFilter.accept("#cite-ref"));
		
		assertTrue(andFilter.accept("http://www.google.com"));
	}

}
