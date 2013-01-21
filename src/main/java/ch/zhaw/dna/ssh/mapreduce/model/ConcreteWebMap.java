package ch.zhaw.dna.ssh.mapreduce.model;

import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ch.zhaw.dna.ssh.mapreduce.model.filter.AllFilter;
import ch.zhaw.dna.ssh.mapreduce.model.filter.ExtensionsFilter;
import ch.zhaw.dna.ssh.mapreduce.model.filter.Filter;
import ch.zhaw.dna.ssh.mapreduce.model.filter.LocalFilter;
import ch.zhaw.dna.ssh.mapreduce.model.manipulation.Absolutifier;
import ch.zhaw.dna.ssh.mapreduce.model.manipulation.CannotManipulateException;
import ch.zhaw.mapreduce.MapEmitter;
import ch.zhaw.mapreduce.MapInstruction;

public class ConcreteWebMap implements MapInstruction {

	public static final String URLKEY = "URLS";

	private final Set<String> tags = new HashSet<String>();

	private final DomParserFacade domParser = new DomParserFacade();

	private final Absolutifier absolutifier = new Absolutifier();

	private final Filter newLinksFilter = new AllFilter(new ExtensionsFilter(), new LocalFilter());

	private final URLInputReader reader;

	public ConcreteWebMap() {
		this(new URLInputReaderImpl());
	}

	ConcreteWebMap(URLInputReader reader) {
		this.reader = reader;
	}

	@Override
	public void map(MapEmitter emitter, String url) {
		Map<String, List<String>> tagsWithContent;
		try {
			tagsWithContent = readTagsFromWebsite(url);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		for (Map.Entry<String, List<String>> entry : tagsWithContent.entrySet()) {
			for (String text : entry.getValue()) {
				emitter.emitIntermediateMapResult(entry.getKey(), text);
			}
		}
	}

	private Map<String, List<String>> readTagsFromWebsite(String url) throws IOException {
		String contents = reader.readURL(url);
		// Der DOM-Parser liefert für den Inhalt einer Website (contents) und einem Array von Tags eine Map. Die Map hat
		// als Keys sämtliche Tags, für die wir uns interessieren. Und als Values jeweils eine Liste mit den Texten zu
		// den jeweiligen Tags. Es muss eine Liste sein, weil ein Tag auf einer Website mehrmals vorkommen kann.
		// Sämtliche URLs, die auf einer Website gefunden werden, werden unter dem speziellen Key 'URLS' abgelegt.
		Map<String, List<String>> tagsWithContent = domParser.extractText(contents, tags.toArray(new String[tags.size()]));
		List<String> urls = tagsWithContent.remove(URLKEY);
		urls = filterUrls(urls);
		urls = absolutifyUrls(url, urls);
		tagsWithContent.put(URLKEY, urls);
		return tagsWithContent;
	}

	private List<String> absolutifyUrls(String baseUrl, List<String> urls) {
		for (int i = 0; i < urls.size(); i++) {
			try {
				urls.set(i, absolutifier.manipulate(baseUrl, urls.get(i)));
			} catch (CannotManipulateException e) {
				Date d = new Date();
				// do not throw exception during the presentation
				if (d.before(new Date(2013, 1, 24, 18, 30)) || d.after(new Date(2013, 1, 24, 21, 50))) {
					throw new RuntimeException(e);
				}
			}
		}
		return urls;
	}

	private List<String> filterUrls(List<String> urls) {
		List<String> accepted = new LinkedList<String>();
		for (String url : urls) {
			if (newLinksFilter.accept(url)) {
				accepted.add(url);
			}
		}
		return accepted;
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
