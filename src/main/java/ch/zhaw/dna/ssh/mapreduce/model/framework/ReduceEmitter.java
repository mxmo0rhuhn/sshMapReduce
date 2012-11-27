package ch.zhaw.dna.ssh.mapreduce.model.framework;

/**
 * Bietet einer Reduce Task die Möglichkeit Zwischenergebnisse zu speichern
 * 
 * @author Max
 * 
 */
public interface ReduceEmitter {

	/**
	 * Gibt einem Reduce Task die Möglichkeit ein Ergebnis ins Framework zu übergeben.
	 * 
	 * @param result
	 *            das Ergebnis das übergeben werden soll
	 */
	void emit(String result);
}
