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

public class DomParserFacade {

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
	
	private String rawText(Tag tag) {
		NodeList nodes = new NodeList();
		tag.collectInto(nodes, new NodeClassFilter(TextNode.class));
		StringBuilder sb = new StringBuilder();
		SimpleNodeIterator iter = nodes.elements();
		while (iter.hasMoreNodes()) {
			TextNode node = (TextNode) iter.nextNode();
			if (sb.length() != 0) {
				sb.append(' ');
			}
			sb.append(node.getText().trim());
		}
		return sb.toString();
	}

	class FlatTagFindingVisitor extends NodeVisitor {
		
		private final List<Tag> tags = new LinkedList<Tag>();
		
		private final Set<String> searched;
		
		private boolean recurse = true;
		
		public FlatTagFindingVisitor(Set<String> searched) {
			this.searched = searched;
		}
		
		@Override
		public void visitTag(Tag tag) {
			if (tag.isEndTag()) {
			// only evaluate start tags
				return;
			} else if (!searched.contains(tag.getTagName())) {
			// not what we're looking for
				return;
			} else {
				tags.add(tag);
				recurse = false;
			}
		}
		
		@Override
		public boolean shouldRecurseChildren() {
			if (!recurse) {
				recurse = true;
				return false;
			} else {
				return true;
			}
		}

		
		public List<Tag> getTags() {
			return tags;
		}
		
	}

}
