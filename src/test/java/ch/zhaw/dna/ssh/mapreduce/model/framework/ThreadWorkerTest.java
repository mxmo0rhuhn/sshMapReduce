package ch.zhaw.dna.ssh.mapreduce.model.framework;

import org.junit.Test;

public class ThreadWorkerTest {
	
	@Test
	public void test() {
		WorkerTask task = new WorkerTask() {
			
			private State currentState = State.IDLE;

			@Override
			public void doWork() {
				this.currentState = State.COMPLETED;
			}

			@Override
			public State getCurrentState() {
				return this.currentState;
			}
			
		};
		
	}

}
