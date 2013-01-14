package ch.zhaw.dna.ssh.mapreduce.model;

import ch.zhaw.mapreduce.MapEmitter;
import ch.zhaw.mapreduce.MapInstruction;

/**
 * Durchsucht den Input nach einem spezifischen Wort
 * 
 * @author Max
 * 
 */
public class SpecificWordFrequencyMapInstruction implements MapInstruction {

	private final String searchedWord;
	
	public SpecificWordFrequencyMapInstruction(String searchedWord) {
		this.searchedWord = searchedWord.toUpperCase();
	}
	
	/** {@inheritDoc} */
	@Override
	public void map(MapEmitter emitter, String input) {
		for (String s : input.trim().split(" ")) {
			if(s.toUpperCase().equals(searchedWord)) {
				emitter.emitIntermediateMapResult(searchedWord, "1");
			}
		}
	}

}