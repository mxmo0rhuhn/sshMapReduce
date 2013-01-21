package ch.zhaw.dna.ssh.mapreduce.model.filter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.junit.Test;

public class NewUrlFilterTest {

	private NewUrlFilter filter = new NewUrlFilter();

	private List<String> accpected = Arrays.asList(new String[] { "index.html", "index.php?site=foo",
			"/wiki/interesting.html", "foo", "/wiki/index.php?site=foo&pic=.png", "/wiki/sub/hellp?site=ch.google" });

	private List<String> notAcceped = Arrays.asList(new String[] { "index.php/pic.gif", "pic.gif", "pic.GIF",
			"pic.jpeg", "pic.SVG", "style.css", "/stylees/style.css", "../images/foo.PNG", "#local", "#foo-bar" });

	@Test
	public void absoluteUrlWithoutParam() {
		String baseUrl = "http://de.wikipedia.org/wiki/search";

		List<String> all = new ArrayList<String>(accpected);
		all.addAll(notAcceped);

		List<String> expected = new ArrayList<String>() {
			private static final long serialVersionUID = 1L;

			{
				add("http://de.wikipedia.org/wiki/index.php?site=foo");
				add("http://de.wikipedia.org/wiki/interesting.html");
				add("http://de.wikipedia.org/wiki/foo");
				add("http://de.wikipedia.org/wiki/index.php?site=foo&pic=.png");
				add("http://de.wikipedia.org/wiki/index.html");
				add("http://de.wikipedia.org/wiki/sub/hellp?site=ch.google");
			}
		};

		Set<String> filtered = filter.filterUrls(baseUrl, all);

		assertTrue(filtered.containsAll(expected));
		assertEquals(expected.size(), filtered.size());
	}

	@Test
	public void absoluteUrlWithParam() {
		String baseUrl = "http://de.wikipedia.org/wiki/search?q=ch.google&asdf";

		List<String> all = new ArrayList<String>(accpected);
		all.addAll(notAcceped);

		List<String> expected = new ArrayList<String>() {
			private static final long serialVersionUID = 1L;

			{
				add("http://de.wikipedia.org/wiki/index.php?site=foo");
				add("http://de.wikipedia.org/wiki/interesting.html");
				add("http://de.wikipedia.org/wiki/foo");
				add("http://de.wikipedia.org/wiki/index.php?site=foo&pic=.png");
				add("http://de.wikipedia.org/wiki/sub/hellp?site=ch.google");
				add("http://de.wikipedia.org/wiki/index.html");
			}
		};

		Set<String> filtered = filter.filterUrls(baseUrl, all);

		assertTrue(filtered.containsAll(expected));
		assertEquals(expected.size(), filtered.size());
	}

	@Test
	public void shouldAcceptAbsoluteUrls() {
		Set<String> filtered = filter.filterUrls("http://de.wikipedia.org/wiki", Arrays.asList(new String[] {"http://www.de.wikipedia.org/wiki/Slayer"}));
		assertEquals(1, filtered.size());
		assertTrue(filtered.contains("http://www.de.wikipedia.org/wiki/Slayer"));
	}

	@Test
	public void shouldOnlyAcceptHttpOrHttps() {
		Set<String> filtered = filter.filterUrls("http://de.wikipedia.org/wiki", Arrays.asList(new String[] {"https://www.de.wikipedia.org/wiki/Slayer", "ftp://ftp.yahoo.com"}));
		assertEquals(1, filtered.size());
		assertTrue(filtered.contains("https://www.de.wikipedia.org/wiki/Slayer"));
	}

	@Test
	public void shouldNotAcceptRedlinks() {
		Set<String> filtered = filter.filterUrls("http://de.wikipedia.org/wiki", Arrays.asList(new String[] {"index.php?redlink=1"}));
		assertTrue(filtered.isEmpty());
	}
	
	@Test
	public void shouldNotAcceptJavaScript() {
		Set<String> filtered = filter.filterUrls("http://de.wikipedia.org/wiki", Arrays.asList(new String[] {"javascript:open_window('subscribe.html')"}));
		assertTrue(filtered.isEmpty());
	}
	
	@Test
	public void shouldNotAcceptDuplicates() {
		Set<String> filtered = filter.filterUrls("http://de.wikipedia.org/wiki/", Arrays.asList(new String[] {"index.html", "index.html"}));
		assertEquals(1, filtered.size());
	}
}
