package ch.zhaw.dna.ssh.mapreduce.model.filter;

import static org.junit.Assert.*;

import org.junit.Test;

public class ImageFilterTest {
	
	private ImageFilter imageFilter = new ImageFilter();
	
	@Test
	public void shouldAcceptLocalRefs() {
		assertTrue(imageFilter.accept("#cite-see"));
	}

	@Test
	public void shouldAcceptAbsoluteUrls() {
		assertTrue(imageFilter.accept("www.google.com"));
		assertTrue(imageFilter.accept("http://www.google.com"));
		assertTrue(imageFilter.accept("http://www.google.com/index.html"));
		assertTrue(imageFilter.accept("http://www.google.com/index"));
		assertTrue(imageFilter.accept("http://www.google.com/jpeg"));
		assertTrue(imageFilter.accept("http://www.google.com?type=gif"));
	}

	@Test
	public void shouldAcceptRelativeUrls() {
		assertTrue(imageFilter.accept("index.html"));
		assertTrue(imageFilter.accept("index.php"));
		assertTrue(imageFilter.accept("index.HTML"));
		assertTrue(imageFilter.accept("index.anything"));
	}

	@Test
	public void shouldNotAcceptImages() {
		assertFalse(imageFilter.accept("hello.png"));
		assertFalse(imageFilter.accept("hello.ico"));
		assertFalse(imageFilter.accept("hello.ICO"));
		assertFalse(imageFilter.accept("hello.gif"));
		assertFalse(imageFilter.accept("hello.GIF"));
		assertFalse(imageFilter.accept("hello.jpg"));
		assertFalse(imageFilter.accept("hello.JPEG"));
		assertFalse(imageFilter.accept("http://www.google.com/hello.JPEG"));
		assertFalse(imageFilter.accept("www.google.com/search/find.svg"));
	}
	
	@Test
	public void shouldRejectWithParams() {
		assertFalse(imageFilter.accept("www.google.com/img.png?foo=bar"));
		assertFalse(imageFilter.accept("img.JPEG?foo=bar&bar=foo"));
	}

}
