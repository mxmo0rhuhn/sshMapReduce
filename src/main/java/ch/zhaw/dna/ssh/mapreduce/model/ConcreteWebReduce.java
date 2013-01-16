package ch.zhaw.dna.ssh.mapreduce.model;

import java.util.Iterator;

import ch.zhaw.mapreduce.KeyValuePair;
import ch.zhaw.mapreduce.ReduceEmitter;
import ch.zhaw.mapreduce.ReduceInstruction;

public class ConcreteWebReduce implements ReduceInstruction {

	private final StringBuilder concatenatedString = new StringBuilder();

	@Override
	public void reduce(ReduceEmitter myEmitter, String key,
			Iterator<KeyValuePair> values) {
		while (values.hasNext()) {
			
			concatenatedString.append(' ');
			concatenatedString.append(values.next().getValue());
		}
		myEmitter.emit(concatenatedString.toString());
	}
}
