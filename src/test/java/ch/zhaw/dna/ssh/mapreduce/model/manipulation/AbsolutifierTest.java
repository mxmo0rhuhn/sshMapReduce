package ch.zhaw.dna.ssh.mapreduce.model.manipulation;

import org.junit.Test;

public class AbsolutifierTest {

	private Absolutifier abs = new Absolutifier();

	@Test
	public void shouldMakeRealtiveUrlsAbsolute() {
		abs.manipulate("http://www.google.com", "index.html");
	}
}
