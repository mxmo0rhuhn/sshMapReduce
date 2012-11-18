package ch.zhaw.dna.ssh.mapreduce.model;

import ch.zhaw.dna.ssh.mapreduce.model.framework.MapRunner;
import ch.zhaw.dna.ssh.mapreduce.model.framework.MapTask;

public class WordFrequencyMapTask implements MapTask{

	@Override
	public void map(MapRunner mapRunner, String[] todo) {
		String[] text = todo;
		int n = todo.length;
		
		for (int i = 0; i < n;) {
			//Trim whitespaces
			text[i] = text[i].trim();
			System.out.println(text[i]);
			i++;
		}
		
		
		//Pool.registerMap(text[]);
		
	}

}
