/**
 * 
 */
package ch.zhaw.dna.ssh.mapreduce.model.framework;

import java.util.HashMap;

/**
 * Bietet die Möglichkeit Zwischenergebnisse in einem Map Task zu aggregieren.
 * 
 * @author Max
 *
 */
public interface CombinerTask extends Task {

	HashMap<String, String> combine(HashMap<String, String> results);

}
