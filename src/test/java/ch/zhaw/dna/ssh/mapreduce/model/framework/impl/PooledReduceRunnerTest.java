package ch.zhaw.dna.ssh.mapreduce.model.framework.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.zhaw.dna.ssh.mapreduce.model.framework.Pool;
import ch.zhaw.dna.ssh.mapreduce.model.framework.ReduceInstruction;

@RunWith(JMock.class)
public class PooledReduceRunnerTest {

	private Mockery context;

	private Pool p;

	private ReduceInstruction redInstr;

	@Before
	public void initMockery() {
		this.context = new JUnit4Mockery();
		this.p = this.context.mock(Pool.class);
		this.redInstr = this.context.mock(ReduceInstruction.class);
	}

	@Test
	public void shouldSetKey() {
		PooledReduceWorkerTask task = new PooledReduceWorkerTask(p, "mruid", "key", redInstr);
		assertEquals("key", task.getKey());
	}

	@Test
	public void shouldMrUid() {
		PooledReduceWorkerTask task = new PooledReduceWorkerTask(p, "mruid", "key", redInstr);
		assertEquals("mruid", task.getMapReduceTaskUUID());
	}

	@Test
	public void shouldSetReduceInstruction() {
		PooledReduceWorkerTask task = new PooledReduceWorkerTask(p, "mruid", "key", redInstr);
		assertSame(redInstr, task.getReduceTask());
	}

}
