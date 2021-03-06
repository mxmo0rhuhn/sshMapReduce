package ch.zhaw.dna.ssh.mapreduce.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ch.zhaw.mapreduce.MapEmitter;
import ch.zhaw.mapreduce.MapInstruction;

public class ConcreteWebMap implements MapInstruction {

	public static final String URLKEY = "URLS";

	private final Set<String> tags = new HashSet<String>(5);

	// private final DomParserFacade domParser = new DomParserFacade();

	// private final NewUrlFilter filter = new NewUrlFilter();

	@Override
	public void map(MapEmitter emitter, String baseUrl) {
		String contents;
		try {
			contents = readURL(baseUrl);
			System.out.println("Downloaded: " + baseUrl);
		} catch (Exception e) {
			System.err.println(Thread.currentThread().getId() + ": Failed to download: " + baseUrl + " (" + e.getMessage() + ")");
			return;
		}
		// Der DOM-Parser liefert für den Inhalt einer Website (contents) und einem Array von Tags eine Map. Die Map hat
		// als Keys sämtliche Tags, für die wir uns interessieren. Und als Values jeweils eine Liste mit den Texten zu
		// den jeweiligen Tags. Es muss eine Liste sein, weil ein Tag auf einer Website mehrmals vorkommen kann.
		// Sämtliche URLs, die auf einer Website gefunden werden, werden unter dem speziellen Key 'URLS' abgelegt.
		//Map<String, List<String>> tagsWithContent = domParser.extractText(contents, tags.toArray(new String[tags.size()]));
		Map<String, List<String>> tagsWithContent = new HashMap<String, List<String>>();
		
		// Set<String> urls = filter.filterUrls(baseUrl, tagsWithContent.remove(URLKEY));
		List<String> urls = Arrays.asList(new String[]{"http://www.google.com", "http://www.wikipedia.org"});
		
		for (Map.Entry<String, List<String>> entry : tagsWithContent.entrySet()) {
			for (String text : entry.getValue()) {
				emitter.emitIntermediateMapResult(entry.getKey(), text);
			}
		}
		for(String url : urls) {
			emitter.emitIntermediateMapResult(URLKEY, url);
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
	
	private String readURL(String inputURL) throws IOException {

		StringBuilder build = new StringBuilder();

		URL input = new URL(inputURL);
		BufferedReader in = new BufferedReader(new InputStreamReader(
				input.openStream()));
		String inputLine;
		try {
			while ((inputLine = in.readLine()) != null) {
				build.append(inputLine);
			}
		} finally {
			in.close();
		}
		return build.toString();
	}
}
