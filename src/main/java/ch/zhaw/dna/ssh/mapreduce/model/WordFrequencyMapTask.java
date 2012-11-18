package ch.zhaw.dna.ssh.mapreduce.model;

import ch.zhaw.dna.ssh.mapreduce.model.framework.MapRunner;
import ch.zhaw.dna.ssh.mapreduce.model.framework.MapTask;
/**
 * Splittet jedes Wort und Emit'ed es an MapRunner
 * @author des
 *
 */
public class WordFrequencyMapTask implements MapTask {

	@Override
	public void map(MapRunner mapRunner, String[] todo) {
		String[] text = todo;
		int n = todo.length;

		for (int i = 0; i < n;) {

			// Trim whitespaces
			text[i] = text[i].trim();
			if (text[i].contains(" ")) {
				// white space index = wsi
				int endIndex = text[i].indexOf(" ");
				int beginIndex = 0;
				int textLength;
				
				// Solange Whitespaces vorhanden, wird gesplittet
				while (text[i].contains(" ")) {
					textLength = text[i].length();
					String splitted = text[i].substring(beginIndex, endIndex);
					text[i] = text[i].substring(endIndex, textLength);
					beginIndex = endIndex;
					endIndex = text[i].length();
					text[i] = text[i].trim();
					mapRunner.emitIntermediateMapResult(splitted, "1");
				}
				// nach letztem Split noch letztes Wort hinzufuegen
				mapRunner.emitIntermediateMapResult(text[i], "1");

			} else {
				// es hatte keine whitespaces, deswegen nur emiten
				mapRunner.emitIntermediateMapResult(text[i], "1");
			}
			i++;
		}

	}

}
