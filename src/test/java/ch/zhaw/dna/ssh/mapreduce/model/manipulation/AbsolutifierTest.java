package ch.zhaw.dna.ssh.mapreduce.model.manipulation;

import static org.junit.Assert.*;

import org.junit.Test;

public class AbsolutifierTest {

	private Absolutifier abs = new Absolutifier();

	@Test
	public void shouldMakeRealtiveUrlsAbsolute() throws CannotManipulateException {
		String baseUrl = "http://www.google.com";
		String url = "index.html";
		String expected = "http://www.google.com/index.html";
		assertEquals(expected, abs.manipulate(baseUrl, url));
	}

	@Test
	public void shouldRemoveParameters() throws CannotManipulateException {
		String baseUrl = "http://www.google.com?foo=bar";
		String url = "index.html";
		String expected = "http://www.google.com/index.html";
		assertEquals(expected, abs.manipulate(baseUrl, url));
	}

	@Test
	public void shouldWorkWithAbsolutePathsInDomain() throws CannotManipulateException {
		String baseUrl = "http://www.google.com/foo/bar";
		String url = "/search";
		String expected = "http://www.google.com/search";
		assertEquals(expected, abs.manipulate(baseUrl, url));
	}

	@Test
	public void shouldAppendParameters() throws CannotManipulateException {
		String baseUrl = "http://www.google.com/foo/bar";
		String url = "/search.php?q=foo";
		String expected = "http://www.google.com/search.php?q=foo";
		assertEquals(expected, abs.manipulate(baseUrl, url));
	}

	@Test(expected = CannotManipulateException.class)
	public void shouldNotAcceptGarbage() throws CannotManipulateException {
		String baseUrl = "asdf";
		String url = "/search.php?q=foo";
		String expected = "http://www.google.com/search.php?q=foo";
		assertEquals(expected, abs.manipulate(baseUrl, url));
	}

	@Test(expected = CannotManipulateException.class)
	public void shouldNotAcceptWithoutProtocol() throws CannotManipulateException {
		String baseUrl = "www.google.com/foo/bar";
		String url = "/search.php?q=foo";
		String expected = "http://www.google.com/search.php?q=foo";
		assertEquals(expected, abs.manipulate(baseUrl, url));
	}
}
