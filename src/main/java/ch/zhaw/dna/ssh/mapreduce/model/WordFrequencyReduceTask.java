package ch.zhaw.dna.ssh.mapreduce.model;

import java.util.Iterator;
import java.util.List;

import ch.zhaw.dna.ssh.mapreduce.model.framework.MapRunner;
import ch.zhaw.dna.ssh.mapreduce.model.framework.ReduceRunner;
import ch.zhaw.dna.ssh.mapreduce.model.framework.ReduceTask;


public class WordFrequencyReduceTask implements ReduceTask {
//	
//	public void reduce(ReduceRunner reduceRunner, MapRunner list) {
//		//Iterate over all entries with the same key and add the values
//		long value = 0;
//		
//		for(String word : input) {
//			value +=  Long.parseLong(word);
//		}
//		System.out.println(Long.toString(value));
//		
//		//Pool.registerReduce(mapRunner);
//	}

	@Override
<<<<<<< HEAD
	public void reduce(ReduceRunner reduceRunner, String key,
			Iterator<String> values) {
		long value = 0;
		
		for(String v : values) {
			value += Long.toString(v);
		}
		
=======
	public void reduce(ReduceRunner reduceRunner, String key, Iterator<String> values) {
		// TODO Auto-generated method stub
>>>>>>> 13eb4ca90208e385db071fcd0cac9f381e1b108e
		
	}
	
	

}
