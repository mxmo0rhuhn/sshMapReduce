package ch.zhaw.dna.ssh.mapreduce.model.framework;

import static org.junit.Assert.assertSame;

import org.junit.Test;

public class PoolHelperTest {

	@Test
	public void shouldReturnSameInstanceTwice() {
		assertSame(PoolHelper.getPool(), PoolHelper.getPool());
	}

	@Test
	public void shouldReturnSameInstanceForMultipleThreads() throws InterruptedException {
		final Pool[] pools = new Pool[3];
		pools[0] = PoolHelper.getPool();
		Thread t1 = new Thread(new Runnable() {

			@Override
			public void run() {
				pools[1] = PoolHelper.getPool();
			}

		});
		t1.start();
		t1.join();

		Thread t2 = new Thread(new Runnable() {

			@Override
			public void run() {
				pools[2] = PoolHelper.getPool();
			}

		});
		t2.start();
		t2.join();

		assertSame(pools[0], pools[1]);
		assertSame(pools[1], pools[2]);
	}

}
