package ch.zhaw.dna.ssh.mapreduce.model.framework;

/**
 * Stellt einen generischen Worker der eine Aufgabe annehmen kann dar.
 * 
 * @author Max
 * 
 */
public interface Worker {

	/**
	 * Lässt den Worker seine derzeitige Aufgabe bearbeiten. Nach dem Ausführen der Aufgabe muss sich der Worker bei seinem Pool melden.
	 * 
	 * @param task
	 *            den WorkerTask, der ausgefuert werden soll
	 */
	void execute(WorkerTask task);
}
