/**
 * 
 */
package ch.zhaw.dna.ssh.mapreduce.model.framework;

/**
 * Stellt eine MapTask dar. In dieser muss definiert werden was genau die Map-Aufgabe des MapReduce ist.
 * 
 * @author Max
 *
 */
public interface MapTask extends Task {
	
	/**
	 * Diese Methode muss überschrieben werden um die Aufgabe eines MAP Tasks zu beschreiben. 
	 * Es muss eine gewisse Logik vorhanden sein mit der der gegebene Input auf KeyValue Pairs gemapped wird.
	 * 
	 * @param mapRunner der MapRunner ist das runtime Environment einer jeden MapTask. Aus ihm können verschiedene Informationen bezogen werden und es können KeyValue-Pairs gespeichert werden.
	 * @param toDo ein Input als String dieser muss in irgendeiner Art in Key Value Pairs umgewandelt werden.
	 */
	public void map(MapRunner mapRunner, String toDo);
}
