/**
 * 
 */
package ch.zhaw.dna.ssh.mapreduce.model.framework;

import java.util.Iterator;

/**
 * Ein Combiner Task bietet die Möglichkeit Zwischenergebnisse in einem Map Task zu aggregieren bevor diese vom Reduce Task abgefragt werden.
 * Dies könnte in konkreten Implementationen Netzwerktraffic sparen.
 * 
 * @author Max
 *
 */
public interface CombinerTask extends Task {

	/**
	 * Führt eine Liste mit Values des selben Keys zusammen
	 * 
	 * @param toCombine Die Liste mit values
	 * @return der aggregierte Wert.
	 */
	String combine(Iterator<String> toCombine);
}
