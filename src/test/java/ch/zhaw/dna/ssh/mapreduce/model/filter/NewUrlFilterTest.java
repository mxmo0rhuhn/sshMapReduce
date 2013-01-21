package ch.zhaw.dna.ssh.mapreduce.model.filter;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class NewUrlFilterTest {

	private NewUrlFilter filter = new NewUrlFilter();

	private List<String> accpected = Arrays.asList(new String[] { "index.html", "index.php?site=foo",
			"/interesting.html", "foo", "../index.php?site=foo&pic=.png", "../sub/hellp?site=ch.google",
			"../../subsub/about" });

	private List<String> notAcceped = Arrays.asList(new String[] { "index.php/pic.gif", "pic.gif", "pic.GIF",
			"pic.jpeg", "pic.SVG", "style.css", "/stylees/style.css", "../images/foo.PNG", "#local", "#foo-bar" });

	@Test
	public void absoluteUrlWithoutParam() {
		String baseUrl = "http://www.google.com/search";

		List<String> all = new ArrayList<String>(accpected);
		all.addAll(notAcceped);

		List<String> expected = new ArrayList<String>() {
			private static final long serialVersionUID = 1L;

			{
				add("http://www.google.com/index.html");
				add("http://www.google.com/index.php?site=foo");
				add("http://www.google.com/interesting.html");
				add("http://www.google.com/foo");
				add("http://www.google.com/../index.php?site=foo&pic=.png");
				add("http://www.google.com/../sub/hellp?site=ch.google");
				add("http://www.google.com/../../subsub/about");
			}
		};

		List<String> filtered = filter.filterUrls(baseUrl, all);

		System.out.println(filtered);

		assertTrue(filtered.containsAll(expected));
		assertEquals(expected.size(), filtered.size());
	}

	@Test
	public void absoluteUrlWithParam() {
		String baseUrl = "http://www.google.com/search?q=ch.google&asdf";

		List<String> all = new ArrayList<String>(accpected);
		all.addAll(notAcceped);

		List<String> expected = new ArrayList<String>() {
			private static final long serialVersionUID = 1L;

			{
				add("http://www.google.com/index.html");
				add("http://www.google.com/index.php?site=foo");
				add("http://www.google.com/interesting.html");
				add("http://www.google.com/foo");
				add("http://www.google.com/../index.php?site=foo&pic=.png");
				add("http://www.google.com/../sub/hellp?site=ch.google");
				add("http://www.google.com/../../subsub/about");
			}
		};

		List<String> filtered = filter.filterUrls(baseUrl, all);

		assertTrue(filtered.containsAll(expected));
		assertEquals(expected.size(), filtered.size());
	}

	@Test
	public void absoluteUrlWithSubdomain() {
		String baseUrl = "http://www.de.google.com/search?q=ch.google&asdf";

		List<String> all = new ArrayList<String>(accpected);
		all.addAll(notAcceped);

		List<String> expected = new ArrayList<String>() {
			private static final long serialVersionUID = 1L;

			{
				add("http://www.de.google.com/index.html");
				add("http://www.de.google.com/index.php?site=foo");
				add("http://www.de.google.com/interesting.html");
				add("http://www.de.google.com/foo");
				add("http://www.de.google.com/../index.php?site=foo&pic=.png");
				add("http://www.de.google.com/../sub/hellp?site=ch.google");
				add("http://www.de.google.com/../../subsub/about");
			}
		};

		List<String> filtered = filter.filterUrls(baseUrl, all);

		assertTrue(filtered.containsAll(expected));
		assertEquals(expected.size(), filtered.size());
	}

}
