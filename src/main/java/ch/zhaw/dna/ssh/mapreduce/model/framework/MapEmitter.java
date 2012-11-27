package ch.zhaw.dna.ssh.mapreduce.model.framework;

/**
 * Bietet einer Map Task die Möglichkeit Zwischenergebnisse zu speichern 
 * 
 * @author Max
 *
 */
public interface MapEmitter {

	/**
	 * Möglichkeit Ein Zwischenergebnis aus der Ausführung eines MAP tasks heraus zu schreiben.
	 * 
	 * @param key
	 *            Key des Zwischenergebnisses.
	 * @param value
	 *            Value des Zwischenergebnisses.
	 */
	void emitIntermediateMapResult(String key, String value);
}
