package ch.zhaw.dna.ssh.mapreduce.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * URL einlesen: Mit Angabe der URL müssen ganze Webseiten eingelesen werden
 * können Nach Tags soll automatisch gesucht und unterteilt werden können DOM
 * Struktur soll beachtet werden
 * 
 * Quelleninfos:
 * http://www.java2s.com/Code/Java/XML/Searchupthetreeforagivennode.htm
 * http://www.drdobbs.com/jvm/easy-dom-parsing-in-java/231002580
 * 
 * @author des
 * 
 */

public class URLInputReader {

	/**
	 * Methode StringBuilder liest von URL alles als String ein
	 * @param inputURL
	 * 				URL welche ausgelesen werden soll
	 * @return build
	 * 				zusammengebauter Input als String
	 * 
	 */
	public StringBuilder readURL(String inputURL) throws IOException {

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
		return build;
	}

}
