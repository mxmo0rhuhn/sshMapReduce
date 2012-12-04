/**
 * 
 */
package ch.zhaw.dna.ssh.mapreduce.model.framework;

import java.util.Iterator;

/**
 * Die nötigen Befehle um Zwischenergebnisse in einem Map Task zu aggregieren bevor diese vom Reduce abgefragt werden.
 * Dies könnte in konkreten Implementationen Netzwerktraffic sparen.
 * 
 * @author Max
 *
 */
public interface CombinerInstruction extends Instruction {

	/**
	 * Führt eine Liste mit Values des selben Keys zusammen
	 * 
	 * @param toCombine Die Liste mit values
	 * @return der aggregierte Wert.
	 */
	String combine(Iterator<String> toCombine);
}
