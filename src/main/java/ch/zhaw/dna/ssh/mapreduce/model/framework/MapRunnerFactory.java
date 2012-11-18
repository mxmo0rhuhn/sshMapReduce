/**
 * 
 */
package ch.zhaw.dna.ssh.mapreduce.model.framework;

/**
 * Eine MapRunner Factory muss MapRunner einer konkreten Implementation erstellen.
 * 
 * @author Max
 * 
 */
public interface MapRunnerFactory {

	/**
	 * Die Factory erstellt immer nur Runner für genau eine Task.
	 * 
	 * @param task
	 *            die Task für die Runner erstellt werden sollen.
	 */
	void assignMapTask(MapTask task);

	/**
	 * Optional ist es möglich combine Tasks zu definieren, die periodisch von den erstellten MapRunnern ausgeführt werden.
	 * @param task eine CombineTask die regelmässig ausgführt wird.
	 * 
	 */
	void assignCombineTask(CombinerTask task);

	/**
	 * Gibt einen neuen MapRunner zurück der bereit für Arbeit ist.
	 * @return ein MapRunner mit den der Factory zugewiesenen Tasks
	 */
	MapRunner getMapRunner();
}
