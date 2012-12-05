package ch.zhaw.dna.ssh.mapreduce.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * URL einlesen: Mit Angabe der URL müssen ganze Webseiten eingelesen werden können
 * Nach Tags soll automatisch gesucht und unterteilt werden können
 * DOM Struktur soll beachtet werden
 * 
 * Quelleninfos:
 * http://www.java2s.com/Code/Java/XML/Searchupthetreeforagivennode.htm
 * http://www.drdobbs.com/jvm/easy-dom-parsing-in-java/231002580
 * 
 * @author des
 *
 */

public class URLInputReader {
	
	
	
	public void readURL(String newinput) throws IOException{
	
	URL input = new URL(newinput);
	BufferedReader in = new BufferedReader(
			new InputStreamReader(input.openStream()));
	
	 String inputLine;
     while ((inputLine = in.readLine()) != null)
         System.out.println(inputLine);
     in.close();
	
	}

}
