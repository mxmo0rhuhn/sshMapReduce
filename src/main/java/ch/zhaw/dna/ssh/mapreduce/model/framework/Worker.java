/**
 * 
 */
package ch.zhaw.dna.ssh.mapreduce.model.framework;

/**
 * Stellt einen generischen Worker der eine Aufgabe annehmen kann dar.
 * 
 * @author Max
 *
 */
public interface Worker {
	
	/**
	 * Übergibt dem Worker seine nächste Aufgabe
	 */
	public void assignNextTask(WorkerTask doThis);
	
	/**
	 * Lässt den Worker seine derzeitige Aufgabe bearbeiten.
	 * Nach dem Ausführen der Aufgabe muss sich der Worker bei seinem Pool melden.
	 */
	public void work();
}
