package ch.zhaw.dna.ssh.mapreduce.model;

import static ch.zhaw.dna.ssh.mapreduce.model.ConcreteWebMap.URLKEY;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.Tag;
import org.htmlparser.filters.AndFilter;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.nodes.TextNode;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.htmlparser.util.SimpleNodeIterator;
import org.htmlparser.visitors.NodeVisitor;

/**
 * Diese Fassade kapselt die komplexe Logik zum parsen des DOM.
 * 
 * @author Desiree, Reto
 * 
 */
public class DomParserFacade {

	/**
	 * Filter, der nach A Tags sucht, welche ein HREF Attribut haben
	 */
	private static final NodeFilter LINK_FILTER = new AndFilter(new TagNameFilter("A"), new HasAttributeFilter("HREF"));

	/***
	 * Map gibt Content der gesuchten Tags, inkl. Tag Typ zurück, so dass bekannt ist, welche Information aus welchem
	 * Tag kommt Bsp: content =
	 * 
	 * <pre>
	 * <html><h1>Ich bin ein Titel</h1></html>
	 * </pre>
	 * 
	 * wird erkannt als: 'h1','Ich bin ein Titel'
	 * 
	 * @param content
	 *            übergibt den Inhalt einer URL (vorgelesen von URLInputReader)
	 * @param setTags
	 *            übergibt ein StringArray mit den gesetzten Tags (ein Tag pro Eintrag)
	 * @return Map
	 */
	public Map<String, List<String>> extractText(String content, String[] desiredTags) {
		Set<String> searched = new HashSet<String>(Arrays.asList(desiredTags));
		Map<String, List<String>> foundTags = new HashMap<String, List<String>>();
		try {
			searchLinks(content, foundTags);
			searchTags(content, searched, foundTags);
			return foundTags;
		} catch (ParserException e) {
			e.printStackTrace();
			return Collections.emptyMap();
		}
	}

	/**
	 * Durchsucht den Inhalt des Strings nach Links mit dem {@link #LINK_FILTER}.
	 * 
	 * @param content
	 *            string/html
	 * @param tags
	 *            resultate
	 * @throws ParserException
	 */
	private void searchLinks(String content, Map<String, List<String>> tags) throws ParserException {
		Parser parser = Parser.createParser(content, null);
		List<String> urls = new LinkedList<String>();
		SimpleNodeIterator nodes = parser.extractAllNodesThatMatch(LINK_FILTER).elements();
		while (nodes.hasMoreNodes()) {
			Node node = nodes.nextNode();
			if (!(node instanceof LinkTag)) {
				System.err.println("Der LINK_FILTER sollte nur LinkTags zulassen");
			} else {
				LinkTag anker = (LinkTag) node;
				urls.add(anker.extractLink());
			}
		}
		if (!urls.isEmpty()) {
			tags.put(URLKEY, urls);
		}
	}

	/**
	 * Durchsucht den String nach den gesuchten Tags und gibt von denen den Text-Inhalt zurueck.
	 * 
	 * @param content
	 *            komplette Website/HTML
	 * @param searched
	 *            gesuchte Tags
	 * @param foundTags
	 *            resultate/ gefundene Tags
	 * @throws ParserException
	 */
	private void searchTags(String content, Set<String> searched, Map<String, List<String>> foundTags)
			throws ParserException {
		Parser parser = Parser.createParser(content, null);
		FlatTagFindingVisitor tagFinder = new FlatTagFindingVisitor(searched);
		parser.visitAllNodesWith(tagFinder);
		for (Tag tag : tagFinder.getTags()) {
			String tagName = tag.getTagName();
			if (!foundTags.containsKey(tagName)) {
				foundTags.put(tagName, new LinkedList<String>());
			}
			foundTags.get(tagName).add(rawText(tag));
		}
	}

	/**
	 * Ein Knoten kann als Kind wieder Tags haben (Verschachtelung). Wenn wir aber einen Tag gefunden haben, der uns
	 * interessiert, wollen wir nur seinen Text-Inhalt.
	 * 
	 * Bsp:
	 * 
	 * <pre>
	 * Tag = '<p>foo <i>bar</i></p>'
	 * </pre>
	 * 
	 * resultiert in 'foo bar'.
	 * 
	 * @param tag
	 *            der Tag, desssen text ausgewertet werden soll
	 * @return Text-Inhalt vom Tag
	 */
	private String rawText(Tag tag) {
		NodeList nodes = new NodeList();
		// lese alle nodes vom type TextNode in die 'nodes' liste
		tag.collectInto(nodes, new NodeClassFilter(TextNode.class));
		StringBuilder sb = new StringBuilder();
		SimpleNodeIterator iter = nodes.elements();
		while (iter.hasMoreNodes()) {
			TextNode node = (TextNode) iter.nextNode();
			if (sb.length() != 0) {
				sb.append(' '); // einzelne texte durch leerzeichen trennen
			}
			sb.append(node.getText().trim());
		}
		return sb.toString();
	}

	/**
	 * Ein Visitor zum Durchsuchen der Tags. Von der org.htmlparser Bibliothek gibt es schon einen TagFindingVisitor,
	 * dieser hat aber einen Nachteil: Es gibt keine Moelgichkeit nach einem gefundenen Tag die Kinder nicht mehr zu
	 * durchsuchen
	 * 
	 * @author Reto
	 * 
	 */
	class FlatTagFindingVisitor extends NodeVisitor {

		/**
		 * gefundenen Tags
		 */
		private final List<Tag> tags = new LinkedList<Tag>();

		/**
		 * gesuchte Tags
		 */
		private final Set<String> searched;

		/**
		 * soll in der Tiefe weitergesucht werden?
		 * 
		 * @see DomParserFacade#shouldRecurseChildren()
		 */
		private boolean recurse = true;

		/**
		 * @param searched
		 *            gesuchte Tags
		 */
		public FlatTagFindingVisitor(Set<String> searched) {
			this.searched = searched;
		}

		/**
		 * Wird fuer jeden Tag aufgerufen. Wir interessieren und nur fuer Start-Tags, die im Set der gesuchten sind.
		 */
		@Override
		public void visitTag(Tag tag) {
			if (!tag.isEndTag() && searched.contains(tag.getTagName())) {
				tags.add(tag);
				recurse = false;
			}
		}

		/**
		 * Nach jedem 'visit' wird geprueft, ob die Kinder auch noch visited werden sollen.
		 */
		@Override
		public boolean shouldRecurseChildren() {
			/*
			 * Es gibt hier im wesentlichen zwei Faelle. Wenn recurse FALSE ist, heisst das, dass wir gerade vorhin
			 * einen gesuchten Tag gefunden habe. In diesem Fall wollen wir die Kinder nicht mehr weiterdurchsuchen,
			 * weil wir fuer gefundenen Kinder einfach alle Text-Inhalte konsolidieren. Dann wird recurse aber sofort
			 * wieder auf true gesetzt, weil beim naechsten Aufruf von dieser Methode wollen wir wieder tauchen
			 * (Geschwister vom gefundenen). Wenn recurse TRUE ist, heisst das, dass wir in diesem Ast noch nichts
			 * gefunden haben, also wollen wir auch fuer folgende weiter tauchen.
			 */
			if (!recurse) {
				recurse = true;
				return false;
			} else {
				return true;
			}
		}

		/**
		 * Liefert alle gefundenen Tags
		 * 
		 * @return liste aller Tags
		 */
		public List<Tag> getTags() {
			return tags;
		}

	}

}
