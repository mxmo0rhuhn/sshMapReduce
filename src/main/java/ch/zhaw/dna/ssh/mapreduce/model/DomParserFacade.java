package ch.zhaw.dna.ssh.mapreduce.model;

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
	 * Map gibt Content der gesuchten Tags, inkl. Tag Typ zurück, so dass bekannt ist, welche Information aus welchem Tag kommt
	 * Bsp: content = <pre><html><h1>Ich bin ein Titel</h1></html></pre>
	 * wird erkannt als: 'h1','Ich bin ein Titel'
	 * @param content übergibt den Inhalt einer URL (vorgelesen von URLInputReader)
	 * @param setTags übergibt ein StringArray mit den gesetzten Tags (ein Tag pro Eintrag)
	 * @return Map 
	 */
	public Map<String, List<String>> extractText(String content, String[] setTags) {

		Arrays.sort(setTags);

		try {
			
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = dbf.newDocumentBuilder();
			Document doc = builder.parse(new InputSource(new StringReader(
					content)));
			Map<String, List<String>> returnMap = new HashMap<String, List<String>>();
			
			searchMatch(doc, setTags, returnMap);
			return returnMap;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
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
				String returnvalue = "";
				for (int i = 0; i < nodeList.getLength(); i++) {
					returnvalue += searchMatch(nodeList.item(i), tags, );
				}
				return returnvalue;

			}
		} else {
			System.out.println(node.getNodeType());
			return "";			
		}

	}



	void addToMap(String tag, String nodeValue,
			Map<String, List<String>> returnMap) {
		if(!returnMap.containsKey(tag)){
			returnMap.put(tag, new LinkedList<String>());
		}
		addTagsToMap(nodeValue, returnMap);
		nodeValue = stripTagsFromContent(nodeValue);
		returnMap.get(tag).add(nodeValue);
		
		
	}

	String stripTagsFromContent(String nodeValue) {
		//TODO: real stripping
		return nodeValue;
		
		
	}

	String addTagsToMap(String nodeValue, Map<String, List<String>> returnMap) {
		// TODO Auto-generated method stub
		return null;
	}

}
