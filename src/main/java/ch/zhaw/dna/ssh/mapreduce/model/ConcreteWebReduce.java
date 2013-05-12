package ch.zhaw.dna.ssh.mapreduce.model;

import java.util.Iterator;


import ch.zhaw.mapreduce.KeyValuePair;
import ch.zhaw.mapreduce.ReduceEmitter;
import ch.zhaw.mapreduce.ReduceInstruction;

/**
 * Reduziert einen Iterator von String values zu einem einzigen String (Konkatenation).
 * 
 * @author Des
 * 
 */
public class ConcreteWebReduce implements ReduceInstruction {

	@Override
	public void reduce(ReduceEmitter myEmitter, String key, Iterator<KeyValuePair> values) {
		StringBuilder sb = new StringBuilder();
		while (values.hasNext()) {
			if (sb.length() != 0) {
				sb.append(' ');
			}
			sb.append(values.next().getValue());
		}
		myEmitter.emit(sb.toString());
	}
}
