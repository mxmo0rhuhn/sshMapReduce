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

	private String searchedWord;
	
	/**
	 * @param searchedWord the searchedWord to set
	 */
	public void setSearchedWord(String searchedWord) {
		this.searchedWord = searchedWord;
	}

	/** {@inheritDoc} */
	@Override
	public void map(MapEmitter emitter, String input) {
		for (String s : input.trim().split(" ")) {
			if(s.trim().toUpperCase().equals(searchedWord)) {
				emitter.emitIntermediateMapResult(searchedWord, "1");
			}
		}
	}

}
