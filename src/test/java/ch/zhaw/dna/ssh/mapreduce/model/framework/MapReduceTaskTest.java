package ch.zhaw.dna.ssh.mapreduce.model.framework;

import org.junit.Test;

public class MapReduceTaskTest {

	@Test
	public void testIntergration() {
		// tested, ob von A-Z alle Resultate propagiert werden
		MapTask mapTask = new MapTask() {
			@Override
			public void map(MapRunner mapRunner, String[] strings) {
				for (String s : strings) {
					mapRunner.emitIntermediateMapResult("numberOfLetters", "" + s.length());
				}
			}
		};
		ReduceTask reduceTask = new ReduceTask() {
		};
		MapReduceTask mrTask = new MapReduceTask(mapTask, reduceTask);
		mrTask.compute("asdf");
	}

}
