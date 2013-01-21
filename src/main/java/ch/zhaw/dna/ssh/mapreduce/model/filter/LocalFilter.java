package ch.zhaw.dna.ssh.mapreduce.model.filter;

/**
 * Filter lokale Referenzen innherhalb einer Seite heraus. z.B.
 * 
 * <pre>
 * &lt;a href="#content"&gt;Content&lt;/a&gt;
 * </pre>
 * 
 * @author Reto
 * 
 */
public class LocalFilter implements Filter {

	@Override
	public boolean accept(String url) {
		return url.charAt(0) != '#';
	}

}
