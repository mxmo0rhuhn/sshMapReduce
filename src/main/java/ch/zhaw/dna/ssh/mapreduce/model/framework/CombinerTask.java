/**
 * 
 */
package ch.zhaw.dna.ssh.mapreduce.model.framework;

import java.util.Iterator;

/**
 * Bietet die Möglichkeit Zwischenergebnisse in einem Map Task zu aggregieren.
 * 
 * @author Max
 *
 */
public interface CombinerTask extends Task {

	String combine(Iterator<String> toCombine);

}
