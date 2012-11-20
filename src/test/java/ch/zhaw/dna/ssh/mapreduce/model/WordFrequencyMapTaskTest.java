package ch.zhaw.dna.ssh.mapreduce.model;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.Sequence;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.zhaw.dna.ssh.mapreduce.model.framework.MapRunner;

@RunWith(JMock.class)
public class WordFrequencyMapTaskTest {

	/** jmock context */
	Mockery context;

	/** instance under test */

	@Before
	public void setUp() {
		context = new JUnit4Mockery();
	}

	@Test
	public void shouldMapInput() {

		String testString = "hallo welt ich teste dich hallo";

		final MapRunner runner = context.mock(MapRunner.class);
		final Sequence seq = context.sequence("runnerSqnc");
		context.checking(new Expectations() {
			{
				one(runner).emitIntermediateMapResult("hallo", "1");
				inSequence(seq);
				one(runner).emitIntermediateMapResult("welt", "1");
				inSequence(seq);
				one(runner).emitIntermediateMapResult("ich", "1");
				inSequence(seq);
				one(runner).emitIntermediateMapResult("teste", "1");
				inSequence(seq);
				one(runner).emitIntermediateMapResult("dich", "1");
				inSequence(seq);
				one(runner).emitIntermediateMapResult("hallo", "1");
				inSequence(seq);
			}
		});

		WordFrequencyMapTask test = new WordFrequencyMapTask();
		test.map(runner, testString);
	}

}
