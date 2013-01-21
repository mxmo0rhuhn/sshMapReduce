package ch.zhaw.dna.ssh.mapreduce.model.filter;

/**
 * Entscheidet fuer eine URL, ob sie sinnvoll ist, weiter zu verfolgen.
 * 
 * @author Reto
 * 
 */
public interface Filter {

	/**
	 * @param url
	 * @return true, wenn die URL akzeptiert wird, sonst false
	 */
	boolean accept(String url);

}
