package ch.zhaw.dna.ssh.mapreduce.model;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import java.io.StringReader;
import java.util.Arrays;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class DomParserFacade {

	public String extractText(String content, String[] setTags) {

		Arrays.sort(setTags);

		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = dbf.newDocumentBuilder();
			Document doc = builder.parse(new InputSource(new StringReader(
					content)));
			return searchMatch(doc, setTags);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}

	public String searchMatch(Node node, String[] tags) {
		// DOCUMENT_NODE: prüft ob DOM Objekt = root, ELEMENT_NODE: prüft ob DOM Objekt child tag
		if (node.getNodeType() == Node.DOCUMENT_NODE || node.getNodeType() == Node.ELEMENT_NODE) {
			if (Arrays.binarySearch(tags, node.getNodeName()) >= 0) {
				return removeTags(node.getNodeValue());
			} else {
				NodeList nodeList = node.getChildNodes();
				String returnvalue = "";
				for (int i = 0; i < nodeList.getLength(); i++) {
					returnvalue += searchMatch(nodeList.item(i), tags);
				}
				return returnvalue;

			}
		} else {
			System.out.println(node.getNodeType());
			return "";			
		}

	}

	public String removeTags(String nodeValue) {
		// TODO Auto-generated method stub
		return nodeValue;
	}
}
