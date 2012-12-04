package ch.zhaw.dna.ssh.mapreduce.model;

 
import java.io.IOException;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.zhaw.dna.ssh.mapreduce.model.URLInputReader;

@RunWith(JMock.class)
public class URLInputReaderTest {
	
	String newinput = "http://edu.panter.ch/MdpUebungDecorator";
	
	Mockery context;
	
	@Before
	public void setUp() {
		context = new JUnit4Mockery();
	}
	
	
	@Test
	public void testInputURL() throws IOException {
		
		final URLInputReader testRun = context.mock(URLInputReader.class);
		
		context.checking(new Expectations() {
			{
			one(testRun).readURL(newinput);
			}
		});
		URLInputReader test = new URLInputReader();
		test.readURL("hallo welt ich teste dich hallo");
		
	}

}
