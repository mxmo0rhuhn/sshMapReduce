package ch.zhaw.dna.ssh.mapreduce.model;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.concurrent.Executors;

import org.junit.Test;

import ch.zhaw.mapreduce.Pool;
import ch.zhaw.mapreduce.impl.ThreadWorker;
import ch.zhaw.mapreduce.registry.Registry;

public class WebCrawlerTest {
	
	@Test
	public void shouldEmitContents() throws Exception {
		Pool pool = Registry.getComponent(Pool.class);
		pool.donateWorker(new ThreadWorker(pool, Executors.newSingleThreadExecutor()));
		WebCrawler crawlyCrawl = new WebCrawler();
		crawlyCrawl.setConsiderH2tags(true);
		long number = crawlyCrawl.searchTheWeb("http://de.wikipedia.org/wiki/Slayer", "Diskografie", 1);
		assertThat("Long Test", number, equalTo(1L));
	}

}