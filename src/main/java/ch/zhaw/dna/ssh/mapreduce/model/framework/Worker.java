package ch.zhaw.dna.ssh.mapreduce.model.framework;

import java.util.List;

/**
 * Stellt einen generischen Worker der eine Aufgabe annehmen kann dar.
 * 
 * @author Max
 * 
 */
public interface Worker {

	/**
	 * Lässt den Worker seine derzeitige Aufgabe bearbeiten. Nach dem Ausführen der Aufgabe muss
	 * sich der Worker bei seinem Pool melden.
	 * 
	 * @param task
	 *            den WorkerTask, der ausgefuert werden soll
	 */
	void execute(WorkerTask task);

	/**
	 * Bietet die Möglichkeit ein - wie auch immer geartetes Key Value Pair auf dem derzeitigen
	 * Worker zu speichern. Dieser muss sich um die Persistierung kümmern.
	 * 
	 * @param mapReduceTaskUID
	 *            Die eindeutige ID des MapReduceTask desssen zugehörige Daten zurückgegeben werden
	 *            sollen.
	 * @param key
	 *            Nicht eindeutiger Schlüssel mit dem gespeichert wird.
	 * @param Value
	 *            Wert der dem Schlüssel zugeordnet wird.
	 */
	void storeKeyValuePair(String mapReduceTaskUID, String key, String value);

	/**
	 * Gibt die derzeit auf dem Worker gespeicherten KeyValue Pairs zurück
	 * 
	 * @param mapReduceTaskUID
	 *            Die eindeutige ID des MapReduceTask desssen zugehörige Daten zurückgegeben werden
	 *            sollen.
	 */
	List<KeyValuePair> getStoredKeyValuePairs(String mapReduceTaskUID);

	/**
	 * Ersetzt die Gespeicherten Key Value Pairs zu einer MapReduceTask.
	 * 
	 * @param mapReduceTaskUID
	 *            Die eindeutige ID des MapReduceTask desssen zugehörige Daten zurückgegeben werden
	 *            sollen.
	 * @param newList
	 *            die neue Liste mit KeyValuePairs
	 */
	void replaceStoredKeyValuePairs(String mapReduceTaskUID, List<KeyValuePair> newList);

}
