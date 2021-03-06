package ch.zhaw.dna.ssh.mapreduce.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Set;

import ch.zhaw.mapreduce.MapReduce;
import ch.zhaw.mapreduce.MapReduceFactory;

/**
 * Diese Klasse durchsucht die Tags einer gegebenen Website nach einem bestimmten Wort. Dabei werden die einzelnen
 * Schritte in MapReduceTasks ausgeführt.
 * 
 * @author Max
 * 
 */
public class WebCrawler extends Observable {

	private int result;
	private String word;

	private boolean considerH1tags = false;
	private boolean considerH2tags = false;
	private boolean considerH3tags = false;
	private boolean considerPtags = false;
	private boolean considerAtags = false;

	private final SpecificWordFrequencyMapInstruction countMapInstruction;
	private final WordFrequencyReduceInstruction countReduceInstruction;
	private final WordFrequencyCombinerInstruction countCombinerInstruction;

	private final ConcreteWebMap webSearchMapInstruction;
	private final ConcreteWebReduce webSearchReduceInstruction;
	private final ConcreteWebCombine webSearchCombineInstruction;

	private int fDepth;
	private int searchedSides;

	WebCrawler(SpecificWordFrequencyMapInstruction countMapInstruction,
			WordFrequencyCombinerInstruction countCombinerInstruction,
			WordFrequencyReduceInstruction countReduceInstruction, ConcreteWebMap webSearchMapInstruction,
			ConcreteWebCombine webSearchCombineInstruction, ConcreteWebReduce webSearchReduceInstruction) {

		this.countMapInstruction = countMapInstruction;
		this.countCombinerInstruction = countCombinerInstruction;
		this.countReduceInstruction = countReduceInstruction;

		this.webSearchMapInstruction = webSearchMapInstruction;
		this.webSearchCombineInstruction = webSearchCombineInstruction;
		this.webSearchReduceInstruction = webSearchReduceInstruction;
	}

	public WebCrawler() {

		this(new SpecificWordFrequencyMapInstruction(), null, new WordFrequencyReduceInstruction(),
				new ConcreteWebMap(), null, new ConcreteWebReduce());
	}

	/**
	 * Durchsucht von der Website ausgehend in einer gewissen Tiefe weitere Seiten auf das Vorkommen eines bestimmten
	 * Wortes
	 * 
	 * @param URL
	 *            Eine valide URL von der aus gesucht werden soll
	 * @param word
	 *            Das wort nachdem gesucht werden soll
	 * @param depth
	 *            Die Tiefe an Links denen gefolgt werden soll.
	 * @return die anzahl an Vorkommen des Wortes auf den durchsuchten Websites.
	 */
	public long searchTheWeb(String URL, String word, int depth) {
		result = 0;

		this.word = word.toUpperCase();

		return getWebsiteContent(URL, depth);
	}

	/**
	 * Zählt die Vorkommen eines speziellen Wortes in einem Text
	 * 
	 * @param toCount
	 *            der Text der durchsucht werden soll
	 * @param word
	 *            das Wort nach dem gesucht werden soll
	 * @return die Anzahl an Vorkommen dieses Wortes in dem Text
	 */
	private long countTheWord(String toCount, String word) {

		countMapInstruction.setSearchedWord(word);

		MapReduce counter = MapReduceFactory.getMapReduce().newMRTask(countMapInstruction, countReduceInstruction,
				countCombinerInstruction, null);

		Map<String, List<String>> results = counter.runMapReduceTask(new WordsInputSplitter(toCount, 50000));
		List<String> counts = results.get(word);
		if (counts != null) {
			long sum = 0;
			for (String count : counts) {
				sum += Long.parseLong(count);
			}
			return sum;
		}

		return 0;
	}

	/**
	 * Gibt den Inhalt bestimmter Tags aller Websites in einer gewissen Tiefe zurück
	 * 
	 * @param url
	 *            die Url von der aus gesucht werden soll
	 * @param depth
	 *            die Tiefe in der gesucht werden soll
	 * @return aller Text, der auf diesen Websites zu finden ist.
	 */
	private int getWebsiteContent(String url, int depth) {

		Set<String> alreadySearchedURLS = new HashSet<String>();
		Collection<String> toSearchURLS = new LinkedList<String>();

		webSearchMapInstruction.setaIsSet(considerAtags);
		webSearchMapInstruction.setpIsSet(considerPtags);
		webSearchMapInstruction.setH1IsSet(considerH1tags);
		webSearchMapInstruction.setH2IsSet(considerH2tags);
		webSearchMapInstruction.setH3IsSet(considerH3tags);

		toSearchURLS.add(url);

		MapReduce searchTask = MapReduceFactory.getMapReduce().newMRTask(webSearchMapInstruction,
				webSearchReduceInstruction, webSearchCombineInstruction, null);

		for (int i = 1; i <= depth; i++) {

			StringBuilder allTheWords = new StringBuilder();

			Map<String, List<String>> results = searchTask.runMapReduceTask(toSearchURLS.iterator());
			List<String> links = results.get(ConcreteWebMap.URLKEY);

			alreadySearchedURLS.addAll(toSearchURLS);
			toSearchURLS.clear();

			if (links != null) {
				for (String s : links) {
					if (!alreadySearchedURLS.contains(s)) {

						toSearchURLS.add(s);
					}
					results.remove(ConcreteWebMap.URLKEY);
				}
			}

			System.out.println("Found " + toSearchURLS.size() + " Links..");

			// alle gefundenen Wörter ins Ergebnis!

			for (String key : results.keySet()) {
				allTheWords.append(results.get(key));
			}
			fDepth = i;
			searchedSides = alreadySearchedURLS.size();
			result += countTheWord(allTheWords.toString(), word);

			setChanged();
			notifyObservers();
		}
		return result;
	}

	/**
	 * Setzt den Wert des Feldes considerH1tags;
	 * 
	 * @param considerH1tags
	 *            Der neue Wert des Feldes considerH1tags
	 */
	public void setConsiderH1tags(boolean considerH1tags) {
		this.considerH1tags = considerH1tags;
	}

	/**
	 * Setzt den Wert des Feldes considerH2tags;
	 * 
	 * @param considerH2tags
	 *            Der neue Wert des Feldes considerH2tags
	 */
	public void setConsiderH2tags(boolean considerH2tags) {
		this.considerH2tags = considerH2tags;
	}

	/**
	 * Setzt den Wert des Feldes considerH3tags;
	 * 
	 * @param considerH3tags
	 *            Der neue Wert des Feldes considerH3tags
	 */
	public void setConsiderH3tags(boolean considerH3tags) {
		this.considerH3tags = considerH3tags;
	}

	/**
	 * Setzt den Wert des Feldes considerPtags;
	 * 
	 * @param considerPtags
	 *            Der neue Wert des Feldes considerPtags
	 */
	public void setConsiderPtags(boolean considerPtags) {
		this.considerPtags = considerPtags;
	}

	/**
	 * Setzt den Wert des Feldes considerAtags;
	 * 
	 * @param considerAtags
	 *            Der neue Wert des Feldes considerAtags
	 */
	public void setConsiderAtags(boolean considerAtags) {
		this.considerAtags = considerAtags;
	}

	/**
	 * Gibt den Wert des Feldes depth zurück.
	 * 
	 * @return derzeitiger Wert des Feldes depth
	 */
	public int getDepth() {
		return fDepth;
	}

	/**
	 * Gibt den Wert des Feldes result zurück.
	 * 
	 * @return derzeitiger Wert des Feldes result
	 */
	public int getResult() {
		return result;
	}

	/**
	 * Gibt den Wert des Feldes searchedSides zurück.
	 * 
	 * @return derzeitiger Wert des Feldes searchedSides
	 */
	public int getSearchedSides() {
		return searchedSides;
	}
}
