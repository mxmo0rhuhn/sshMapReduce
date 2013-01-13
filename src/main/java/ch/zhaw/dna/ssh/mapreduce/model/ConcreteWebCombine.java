package ch.zhaw.dna.ssh.mapreduce.model;

import java.util.Iterator;
import java.util.List;

import ch.zhaw.mapreduce.CombinerInstruction;
import ch.zhaw.mapreduce.KeyValuePair;

public class ConcreteWebCombine implements CombinerInstruction {


	@Override
	public List<KeyValuePair> combine(Iterator<KeyValuePair> toCombine) {
		// TODO Auto-generated method stub
		return null;
	}

}
