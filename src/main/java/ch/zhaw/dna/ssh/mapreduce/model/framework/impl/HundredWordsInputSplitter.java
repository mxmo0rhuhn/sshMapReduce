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
		int words = 0;
		while (words <= 100 && ++this.pos < this.input.length()) {
			if (this.input.charAt(this.pos) == ' ') {
				words++;
			}
		}
		return this.input.substring(start, this.pos);
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException("InputSplitter is readonly");
	}

}
