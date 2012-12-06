package ch.zhaw.dna.ssh.mapreduce.model;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;

public class URLInputReaderTest {

	static String url = "http://edu.panter.ch/MdpUebungDecorator";

	@Test
	public void testInputURL() throws IOException {
		URLInputReader uir = new URLInputReader();
		assertNotNull(uir.readURL(url));

	}

	public void lookForTestWord() throws IOException {
		URLInputReader uir = new URLInputReader();
		assertTrue(uir.readURL(url).toString().contains("Pattern"));
	}

}
