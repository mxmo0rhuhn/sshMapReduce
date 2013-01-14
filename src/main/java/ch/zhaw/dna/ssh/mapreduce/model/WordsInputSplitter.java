package ch.zhaw.dna.ssh.mapreduce.model;

import java.util.Iterator;

/**
 * Splittet ein langer String in 100 Zeichen lange Stuecke, die dann via Iterator zur Verfuegung stehen.
 * 
 * @author Reto
 * 
 */
public class WordsInputSplitter implements Iterator<String> {
	
	private final int length;

	/**
	 * Position im input, wo als naechstes gelesen werden kann.
	 */
	private int pos = 0;

	/**
	 * Gesamter input string. Dieser String wird nie modifziert, sondern es wird lediglich per Index ein substring
	 * produziert.
	 */
	private final String input;

	/**
	 * Erstellt einen neune 100-Wort Splitter.
	 * 
	 * @param input
	 *            darf nicht null sein
	 */
	public WordsInputSplitter(String input, int length) {
		if (input == null) {
			throw new IllegalArgumentException("Input darf nicht null sein.");
		}
		this.input = input;
		this.length = length;
	}

	/**
	 * Solange diese Methode true returniert, kann weiter konsumiert werden.
	 */
	@Override
	public boolean hasNext() {
		return pos < input.length();
	}

	/**
	 * Gibt den naechsten Teil-String zurueck. Wenn weniger als 100 Worte uebrig sind, werden weniger retourniert. Der
	 * Zeitaufwand ist konstant, genauso wie der Speicher, da nur ein substring vom initialen input zurueckgegeben wird.
	 */
	@Override
	public String next() {
		if (!hasNext()) {
			return null;
		}
		int start = this.pos;
		int words = 0;
		while (words < length && ++this.pos < this.input.length()) {
			if (this.input.charAt(this.pos) == ' ') {
				words++;
			}
		}
		return this.input.substring(start, this.pos++); // consume last trailing space
	}

	/**
	 * Dieser Iterator ist read-only.
	 */
	@Override
	public void remove() {
		throw new UnsupportedOperationException("InputSplitter is readonly");
	}

}
