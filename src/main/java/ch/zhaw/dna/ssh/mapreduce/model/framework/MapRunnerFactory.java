/**
 * 
 */
package ch.zhaw.dna.ssh.mapreduce.model.framework;


/**
 * @author Max
 * 
 */
public interface MapRunnerFactory {

	public void assignMapTask(MapTask task);

	public void assignCombineTask(CombinerTask task);

	public MapRunner getMapRunner();
}
