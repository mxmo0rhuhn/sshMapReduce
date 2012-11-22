/**
 * 
 */
package ch.zhaw.dna.ssh.mapreduce.view.util;

/**
 * Interface zur Ausgabe von Output für den User
 * 
 * @author Max
 * 
 */
public interface OutputInterface {

	/**
	 * Bietet die Möglichkeit eine Zeile in ein User-Interface zu schreiben
	 * 
	 * @param line
	 *            der Text für die Zeile
	 */
	void println(String line);
}
