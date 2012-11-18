/**
 * 
 */
package ch.zhaw.dna.ssh.mapreduce.model.framework;

import java.util.Map;

/**
 * Bietet die Möglichkeit Zwischenergebnisse in einem Map Task zu aggregieren.
 * 
 * @author Max
 *
 */
public interface CombinerTask extends Task {

	Map<String, String> combine(Map<String, String> results);

}
