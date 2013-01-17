package ch.zhaw.dna.ssh.mapreduce.model;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.Executors;

import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.zhaw.mapreduce.Pool;
import ch.zhaw.mapreduce.impl.ThreadWorker;
import ch.zhaw.mapreduce.registry.Registry;

@RunWith(JMock.class)
public class WebCrawlerTest {
	
	private Mockery context;

	private static final String googleURL = "http:\\www.google.ch";
	private static final String twitterURL = "http:\\www.twitter.com";
	private static final String wikipediaURL = "http:\\www.wikipedia.ch";
	private static final String facebookURL = "http:\\www.facebook.com";
	private static final String githubURL = "http:\\www.github.com";
	private static final String minURL = "http:\\www.20min.ch";
	private static final String SPONURL = "http:\\www.SPON.de";
	private static final String lolURL = "http:\\www.lol.com";
	private static final String ZHAWURL = "http:\\www.ZHAW.ch";
	private static final String asdfURL = "http:\\www.asdf.com";
	
	private String zeroTest ="Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor";
	private String oneBIGTest = "Lorem ipsum dolor sit amet, consetetur TEST sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut";
	private String oneSMALLTest = "Lorem ipsum dolor sit amet, consetetur test sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut";
	private String twoTest = "TEST Lorem ipsum dolor sit amet, consetetur test sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut";
	private String threeTest = "test Lorem ipsum dolor sit amet, consetetur test sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut TEST";
	private String fiveTest = "test Lorem ipsum dolor stestit amet, cotestnsetetur test sadipscing TEST elitr, teSt sed diam nonumy eirmod tempor invidunt ut TEST";
	
	@Before
	public void initMock() {
		this.context = new JUnit4Mockery();
	}
	
	@Test
	public void shouldEmitContents() throws Exception {
		
		Pool pool = Registry.getComponent(Pool.class);
		pool.donateWorker(new ThreadWorker(pool, Executors.newSingleThreadExecutor()));
		WebCrawler crawlyCrawl = new WebCrawler();
		crawlyCrawl.setConsiderH2tags(true);
		int number = crawlyCrawl.searchTheWeb("http://de.wikipedia.org/wiki/Slayer", "Diskografie", 1);
		assertEquals(1, number);
	}

}