/**
 * 
 */
package ch.zhaw.dna.ssh.mapreduce.model.framework;

/**
 * Stellt eine MapTask dar
 * 
 * @author Max
 *
 */
public interface MapTask extends Task {
	
	public void map(MapRunner mapRunner, String toDo);
}
