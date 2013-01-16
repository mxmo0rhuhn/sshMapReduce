package ch.zhaw.dna.ssh.mapreduce.model;

import static ch.zhaw.dna.ssh.mapreduce.model.ConcreteWebMap.URLKEY;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import java.io.StringReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class DomParserFacade {

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
	public Map<String, List<String>> extractText(String content, String[] setTags) {

		// binary search precondition
		Arrays.sort(setTags);

		Map<String, List<String>> collectedTags = new HashMap<String, List<String>>();
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = dbf.newDocumentBuilder();
			Document doc = builder.parse(new InputSource(new StringReader(content)));
			searchMatch(doc, setTags, collectedTags);
			return collectedTags;
		} catch (Exception e) {
			e.printStackTrace();
			return collectedTags;
		}

	}

	public void searchMatch(Node node, String[] tags, Map<String, List<String>> returnMap) {
		// DOCUMENT_NODE: prüft ob DOM Objekt = root, ELEMENT_NODE: prüft ob DOM Objekt child tag
		if (node.getNodeType() == Node.DOCUMENT_NODE || node.getNodeType() == Node.ELEMENT_NODE) {
			String tag = node.getNodeName();
			if (Arrays.binarySearch(tags, tag) >= 0) {
				addToMap(tag, node.getNodeValue(), returnMap);
			} else {
				NodeList nodeList = node.getChildNodes();
				for (int i = 0; i < nodeList.getLength(); i++) {
					searchMatch(nodeList.item(i), tags, returnMap);
				}

			}
		} else {
			System.out.println(node.getNodeType());

		}

	}

	void addToMap(String tag, String nodeValue, Map<String, List<String>> returnMap) {
		if (!returnMap.containsKey(tag)) {
			returnMap.put(tag, new LinkedList<String>());
		}
		addUrlsToMap(nodeValue, returnMap); // Verlustfreies suchen nach <a> Tags um URLs separat auszuweisen
		nodeValue = stripTagsFromContent(nodeValue);
		returnMap.get(tag).add(nodeValue);
	}

	String stripTagsFromContent(String nodeValue) {
		// TODO: real stripping
		return nodeValue;

	}

	void addUrlsToMap(String nodeValue, Map<String, List<String>> tagMap) {
		int startA = nodeValue.indexOf("<a");
		while (startA >= 0) {
			int closeA = nodeValue.indexOf(">", startA);
			int href = nodeValue.indexOf("href=", startA);

			// no href or closing a found in opening a tag
			if (href < 0 || closeA < 0) {
				return;
			} else if (href < closeA) {
				// href exists
				int endUrl = nodeValue.indexOf("\"", href + 6);
				if (endUrl < 0) {
					return;
				} else if (endUrl < closeA) {
					String url = nodeValue.substring(href + 6, endUrl).trim();
					if (url.charAt(0) != '#') {
						// ignore local url
						if (!tagMap.containsKey(URLKEY)) {
							tagMap.put(URLKEY, new LinkedList<String>());
						}
						tagMap.get(URLKEY).add(url.trim());
					}
				}
			}
			startA = nodeValue.indexOf("<a", closeA);

		}
	}

}
