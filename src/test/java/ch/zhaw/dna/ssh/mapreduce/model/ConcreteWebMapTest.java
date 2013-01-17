package ch.zhaw.dna.ssh.mapreduce.model;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.zhaw.mapreduce.MapEmitter;

@RunWith(JMock.class)
public class ConcreteWebMapTest {

	private Mockery context;

	private URLInputReader reader;

	@Before
	public void initMock() {
		this.context = new JUnit4Mockery();
		this.reader = this.context.mock(URLInputReader.class);
	}

	@Test
	public void shouldEmitContents() throws IOException {
		final URLInputReader localReader = new URLInputReader() {
			@Override
			public String readURL(String url) throws IOException {
				return "<html><p>hello, world</p><a href=\"www.rethab.ch\">reto website</a></html>";
			}
		};
		ConcreteWebMap webMap = new ConcreteWebMap(localReader);
		webMap.setaIsSet(true);
		webMap.setpIsSet(true);
		final MapEmitter emitter = this.context.mock(MapEmitter.class);
		this.context.checking(new Expectations() {
			{
				oneOf(emitter).emitIntermediateMapResult("P", "hello, world");
				oneOf(emitter).emitIntermediateMapResult("A", "reto website");
				oneOf(emitter).emitIntermediateMapResult("URLS", "www.rethab.ch");
			}
		});
		webMap.map(emitter, "foo");
	}

	@Test
	public void shouldSetATags() {
		ConcreteWebMap webMap = new ConcreteWebMap(reader);
		assertFalse(webMap.isaIsSet());
		webMap.setaIsSet(true);
		assertTrue(webMap.isaIsSet());
	}

	@Test
	public void shouldSetPTags() {
		ConcreteWebMap webMap = new ConcreteWebMap(reader);
		assertFalse(webMap.ispIsSet());
		webMap.setpIsSet(true);
		assertTrue(webMap.ispIsSet());
	}

	@Test
	public void shouldSetH1Tags() {
		ConcreteWebMap webMap = new ConcreteWebMap(reader);
		assertFalse(webMap.isH1IsSet());
		webMap.setH1IsSet(true);
		assertTrue(webMap.isH1IsSet());
	}

	@Test
	public void shouldSetH2Tags() {
		ConcreteWebMap webMap = new ConcreteWebMap(reader);
		assertFalse(webMap.isH2IsSet());
		webMap.setH2IsSet(true);
		assertTrue(webMap.isH2IsSet());
	}

	@Test
	public void shouldSetH3Tags() {
		ConcreteWebMap webMap = new ConcreteWebMap(reader);
		assertFalse(webMap.isH3IsSet());
		webMap.setH3IsSet(true);
		assertTrue(webMap.isH3IsSet());
	}

	@Test
	public void shouldResetTags() {
		ConcreteWebMap webMap = new ConcreteWebMap(reader);
		webMap.setaIsSet(true);
		webMap.setaIsSet(true);
		assertTrue(webMap.isaIsSet());
		webMap.setaIsSet(false);
		webMap.setaIsSet(false);
		assertFalse(webMap.isaIsSet());
	}

}
