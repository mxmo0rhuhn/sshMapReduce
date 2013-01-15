package ch.zhaw.dna.ssh.mapreduce.model;

import java.io.IOException;

/**
 * Interface zum Lesen von URLS
 * @author Reto
 *
 */
public interface URLInputReader {
	
	/**
	 * Nimmt eine URL entgegeben und liest deren Inhalt aus
	 * @param url
	 * @return den Inhalt der Website
	 * @throws IOException wenn die URL nicht gelesen werden kann
	 */
	String readURL(String url) throws IOException;

}
