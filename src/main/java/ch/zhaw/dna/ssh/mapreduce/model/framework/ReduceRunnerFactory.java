package ch.zhaw.dna.ssh.mapreduce.model.framework;

/**
 * Diese factory kann vewendet werden um neue ReduceRunners zu erstellen. Dabei hilft sie bei der Kreation und dem
 * Setzen des ReduceTask und Keys.
 * 
 * @author Reto, Max, Desiree
 */
public interface ReduceRunnerFactory {

	/**
	 * Setzt den ReduceTask fuer den ReduceRunner
	 * 
	 * @param reduceTask
	 */
	void assignReduceTask(ReduceTask reduceTask);

	/**
	 * Wenn ReduceTask und Master gesetzt worden sind, kann ein ReduceRunner erstellt werden. Dazu muss ein key
	 * spezifiziert werden, weil jeder ReduceTask ein Wort reduziert.
	 * 
	 * @param forKey
	 * @return einen ReduceRunner mit dem gegebenen ReduceTask und dem Master
	 */
	ReduceRunner create(String forKey);

	/**
	 * Setzt den Master, sodass nachher ein ReduceRunner mit diesem Master erstellt werden kann.
	 * 
	 * @param master
	 */
	void setMaster(Master master);
}
