package ch.zhaw.dna.ssh.mapreduce.model;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ch.zhaw.mapreduce.MapEmitter;
import ch.zhaw.mapreduce.MapInstruction;


public class ConcreteWebMap implements MapInstruction {

	public static final String URLKEY = "URLS";


	private final Set<String> tags = new HashSet<String>(5);

	private final DomParserFacade domParser = new DomParserFacade();
	
	private final URLInputReader reader;
	
	ConcreteWebMap(URLInputReader reader) {
		this.reader = reader;
	}
	
	public static final String URLKey = "URLS";
	
	public ConcreteWebMap() {
		this(new URLInputReaderImpl());
	}

	@Override
	public void map(MapEmitter emitter, String url) {
		String contents;
		try {
			contents = reader.readURL(url);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		// Der DOM-Parser liefert für den Inhalt einer Website (contents) und einem Array von Tags eine Map. Die Map hat
		// als Keys sämtliche Tags, für die wir uns interessieren. Und als Values jeweils eine Liste mit den Texten zu
		// den jeweiligen Tags. Es muss eine Liste sein, weil ein Tag auf einer Website mehrmals vorkommen kann.
		// Sämtliche URLs, die auf einer Website gefunden werden, werden unter dem speziellen Key 'URLS' abgelegt.
		Map<String, List<String>> tagsWithContent = domParser.extractText(contents,
				tags.toArray(new String[tags.size()]));
		for (Map.Entry<String, List<String>> entry : tagsWithContent.entrySet()) {
			for (String text : entry.getValue()) {
				emitter.emitIntermediateMapResult(entry.getKey(), text);
			}
		}
	}

	public boolean isH1IsSet() {
		return hasTag("H1");
	}

	public void setH1IsSet(boolean h1IsSet) {
		setTag("H1", h1IsSet);
	}

	public boolean isH2IsSet() {
		return hasTag("H2");
	}

	public void setH2IsSet(boolean h2IsSet) {
		setTag("H2", h2IsSet);
	}

	public boolean isH3IsSet() {
		return hasTag("H3");
	}

	public void setH3IsSet(boolean h3IsSet) {
		setTag("H3", h3IsSet);
	}

	public boolean ispIsSet() {
		return hasTag("P");
	}

	public void setpIsSet(boolean pIsSet) {
		setTag("P", pIsSet);
	}

	public boolean isaIsSet() {
		return hasTag("A");
	}

	public void setaIsSet(boolean aIsSet) {
		setTag("A", aIsSet);
	}

	private boolean setTag(String tagName, boolean set) {
		if (set) {
			return this.tags.add(tagName);
		} else {
			return this.tags.remove(tagName);
		}
	}

	private boolean hasTag(String tagName) {
		return this.tags.contains(tagName);
	}

}
