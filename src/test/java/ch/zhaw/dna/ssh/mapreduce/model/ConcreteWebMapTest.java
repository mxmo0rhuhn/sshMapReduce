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

	@Before
	public void initMock() {
		this.context = new JUnit4Mockery();
	}

	@Test
	public void shouldEmitContents() throws IOException {
		ConcreteWebMap webMap = new ConcreteWebMap();
		webMap.setaIsSet(true);
		webMap.setpIsSet(true);
		final MapEmitter emitter = this.context.mock(MapEmitter.class);
		this.context.checking(new Expectations() {
			{
				oneOf(emitter).emitIntermediateMapResult("P", "hello, world");
				oneOf(emitter).emitIntermediateMapResult("A", "reto website");
				oneOf(emitter).emitIntermediateMapResult("URLS", "http://www.de.wikipedia.org/wiki/Slayer");
			}
		});
		webMap.map(emitter, "http://de.wikipedia.org/wiki/Metallica");
	}

	@Test
	public void shouldSetATags() {
		ConcreteWebMap webMap = new ConcreteWebMap();
		assertFalse(webMap.isaIsSet());
		webMap.setaIsSet(true);
		assertTrue(webMap.isaIsSet());
	}

	@Test
	public void shouldSetPTags() {
		ConcreteWebMap webMap = new ConcreteWebMap();
		assertFalse(webMap.ispIsSet());
		webMap.setpIsSet(true);
		assertTrue(webMap.ispIsSet());
	}

	@Test
	public void shouldSetH1Tags() {
		ConcreteWebMap webMap = new ConcreteWebMap();
		assertFalse(webMap.isH1IsSet());
		webMap.setH1IsSet(true);
		assertTrue(webMap.isH1IsSet());
	}

	@Test
	public void shouldSetH2Tags() {
		ConcreteWebMap webMap = new ConcreteWebMap();
		assertFalse(webMap.isH2IsSet());
		webMap.setH2IsSet(true);
		assertTrue(webMap.isH2IsSet());
	}

	@Test
	public void shouldSetH3Tags() {
		ConcreteWebMap webMap = new ConcreteWebMap();
		assertFalse(webMap.isH3IsSet());
		webMap.setH3IsSet(true);
		assertTrue(webMap.isH3IsSet());
	}

	@Test
	public void shouldResetTags() {
		ConcreteWebMap webMap = new ConcreteWebMap();
		webMap.setaIsSet(true);
		webMap.setaIsSet(true);
		assertTrue(webMap.isaIsSet());
		webMap.setaIsSet(false);
		webMap.setaIsSet(false);
		assertFalse(webMap.isaIsSet());
	}
}
