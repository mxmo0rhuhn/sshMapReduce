package ch.zhaw.dna.ssh.mapreduce.model.framework;

import org.junit.Ignore;

public class MapReduceTaskTest {

	@Ignore
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

			@Override
			public void reduce(ReduceRunner reduceRunner, String[] input) {
				// TODO Auto-generated method stub
				
			}
		};
		MapReduceTask mrTask = new MapReduceTask(mapTask, reduceTask);
		mrTask.compute("asdf");
	}

}
