package ch.zhaw.dna.ssh.mapreduce.model;

import ch.zhaw.dna.ssh.mapreduce.model.framework.MapRunner;
import ch.zhaw.dna.ssh.mapreduce.model.framework.MapTask;

/**
 * Splittet jedes Wort und Emit'ed es an MapRunner
 * 
 * @author des
 * 
 */
public class WordFrequencyMapTask implements MapTask {

	@Override
	public void map(MapRunner mapRunner, String input) {
		String text = input;

		// Trim whitespaces
		text = text.trim();
		if (text.contains(" ")) {
			// white space index = wsi
			int endIndex = text.indexOf(" ");
			int beginIndex = 0;
			int textLength;

			// Solange Whitespaces vorhanden, wird gesplittet
			while (text.contains(" ")) {
				textLength = text.length();
				String splitted = text.substring(beginIndex, endIndex);
				text = text.substring(endIndex, textLength);
				beginIndex = endIndex;
				endIndex = text.indexOf(" ");
				text = text.trim();
				mapRunner.emitIntermediateMapResult(splitted, "1");
			}
			// nach letztem Split noch letztes Wort hinzufuegen
			mapRunner.emitIntermediateMapResult(text, "1");

		} else {
			// es hatte keine whitespaces, deswegen nur emiten
			mapRunner.emitIntermediateMapResult(text, "1");
		}

	}

}
