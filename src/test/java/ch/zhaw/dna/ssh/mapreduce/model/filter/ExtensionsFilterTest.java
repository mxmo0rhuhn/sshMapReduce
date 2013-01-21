package ch.zhaw.dna.ssh.mapreduce.model.filter;

import static org.junit.Assert.*;

import org.junit.Test;

public class ExtensionsFilterTest {

	private ExtensionsFilter extFilter = new ExtensionsFilter();

	@Test
	public void shouldAcceptLocalRefs() {
		assertTrue(extFilter.accept("#cite-see"));
	}

	@Test
	public void shouldAcceptAbsoluteUrls() {
		assertTrue(extFilter.accept("www.google.com"));
		assertTrue(extFilter.accept("http://www.google.com"));
		assertTrue(extFilter.accept("http://www.google.com/index.html"));
		assertTrue(extFilter.accept("http://www.google.com/index"));
		assertTrue(extFilter.accept("http://www.google.com/jpeg"));
		assertTrue(extFilter.accept("http://www.google.com?type=gif"));
	}

	@Test
	public void shouldAcceptRelativeUrls() {
		assertTrue(extFilter.accept("index.html"));
		assertTrue(extFilter.accept("index.php"));
		assertTrue(extFilter.accept("index.HTML"));
		assertTrue(extFilter.accept("index.anything"));
	}

	@Test
	public void shouldNotAcceptImages() {
		assertFalse(extFilter.accept("hello.png"));
		assertFalse(extFilter.accept("hello.ico"));
		assertFalse(extFilter.accept("hello.ICO"));
		assertFalse(extFilter.accept("hello.gif"));
		assertFalse(extFilter.accept("hello.GIF"));
		assertFalse(extFilter.accept("hello.jpg"));
		assertFalse(extFilter.accept("hello.JPEG"));
		assertFalse(extFilter.accept("http://www.google.com/hello.JPEG"));
		assertFalse(extFilter.accept("www.google.com/search/find.svg"));
	}

	@Test
	public void shouldRejectWithParams() {
		assertFalse(extFilter.accept("www.google.com/img.png?foo=bar"));
		assertFalse(extFilter.accept("img.JPEG?foo=bar&bar=foo"));
	}

	@Test
	public void shouldRejectAnyCss() {
		assertFalse(extFilter.accept("www.google.com/style/style.css?foo=bar"));
		assertFalse(extFilter.accept("http://www.google.com/style/style.css?foo=bar"));
		assertFalse(extFilter.accept("www.google.com/style/style.css"));
		assertFalse(extFilter.accept("style/foo.css"));
	}

}
