package ch.zhaw.dna.ssh.mapreduce.model.framework.impl;

import ch.zhaw.dna.ssh.mapreduce.model.framework.InputSplitter;


public class HundredWordsInputSplitter implements InputSplitter {
	
	private int pos = 0;
	
	private final String input;
	
	public HundredWordsInputSplitter(String input) {
		this.input = input;
	}

	@Override
	public boolean hasNext() {
		return pos < input.length();
	}

	@Override
	public String next() {
		if (!hasNext()) {
			return null;
		}
		int start = this.pos;
		this.pos = Math.min(pos + 100, this.input.length());
		return input.substring(start, pos);
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException("InputSplitter is readonly");
	}

}
