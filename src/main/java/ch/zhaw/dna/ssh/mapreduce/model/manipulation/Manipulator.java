package ch.zhaw.dna.ssh.mapreduce.model.manipulation;

/**
 * Nimmt eine URL (url) und die URL, auf der die andere gefunden wurde (baseUrl) und gibt eine manipuliert URL zurueck.
 * 
 * @author Reto
 * 
 */
public interface Manipulator {

	/**
	 * z.B. baseUrl=www.google.com, url=index.html ==&gt; www.google.com/index.html
	 * 
	 * @param baseUrl
	 * @param url
	 * @return brauchbare URL
	 */
	String manipulate(String baseUrl, String url);

}
