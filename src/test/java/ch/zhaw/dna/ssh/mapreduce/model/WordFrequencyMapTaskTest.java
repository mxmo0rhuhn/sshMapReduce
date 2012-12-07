package ch.zhaw.dna.ssh.mapreduce.model;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.Sequence;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.zhaw.dna.ssh.mapreduce.model.framework.MapEmitter;

@RunWith(JMock.class)
public class WordFrequencyMapTaskTest {

	/** jmock context */
	Mockery context;

	@Before
	public void setUp() {
		context = new JUnit4Mockery();
	}

	@Test
	public void shouldMapInput() {

		String testString = "hallo welt ich teste dich hallo";

		final MapEmitter emitter = context.mock(MapEmitter.class);
		final Sequence seq = context.sequence("runnerSqnc");
		context.checking(new Expectations() {
			{
				one(emitter).emitIntermediateMapResult("hallo", "1");
				inSequence(seq);
				one(emitter).emitIntermediateMapResult("welt", "1");
				inSequence(seq);
				one(emitter).emitIntermediateMapResult("ich", "1");
				inSequence(seq);
				one(emitter).emitIntermediateMapResult("teste", "1");
				inSequence(seq);
				one(emitter).emitIntermediateMapResult("dich", "1");
				inSequence(seq);
				one(emitter).emitIntermediateMapResult("hallo", "1");
				inSequence(seq);
			}
		});

		WordFrequencyMapInstruction test = new WordFrequencyMapInstruction();
		test.map(emitter, testString);
	}

}
