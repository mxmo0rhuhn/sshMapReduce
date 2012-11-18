package ch.zhaw.dna.ssh.mapreduce.model.framework;

import java.util.Iterator;

/**
 * Stellt eine ReduceTask dar. In der Reduce Task ist definiert wie die Zwischenergebnisse (Key Value Pairs) zu einzelenen Werten aggregiert
 * werden.
 * 
 * @author Reto
 * 
 */
public interface ReduceTask extends Task {

	/**
	 * Diese Methode muss überschrieben werden um die Aufgabe eines REDUCE Tasks zu beschreiben. Es muss eine gewisse Logik vorhanden sein
	 * mit der der gegebene Input Values zu einem Key in einen aggregierten Value gemapped werden.
	 * 
	 * @param reduceRunner
	 *            ist das runtime Environment einer jeden Reduce Task. Aus ihm können verschiedene Informationen bezogen werden und es
	 *            können Werte gespeichert werden.
	 * 
	 * @param key
	 *            der Key für den der derzeitige Reduce Task ausgeführt wird
	 * @param values
	 *            die Werte die verdichtet werden sollen
	 */
	void reduce(ReduceRunner reduceRunner, String key, Iterator<String> values);
}
