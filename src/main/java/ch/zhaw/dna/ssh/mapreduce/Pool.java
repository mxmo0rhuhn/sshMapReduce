package ch.zhaw.dna.ssh.mapreduce;

/**
 * Pool kontrolliert Anzahl der Worker und liefert diese an Master
 * @author Desiree Sacher
 *
 */
public class Pool {

	private final int capacity;

	/**
	 * Setzt Pool Groesse = Anz Worker durch Uebergabewert fest
	 * 
	 * @throws IllegalArgumentException
	 *             Wenn Wert kleiner 1
	 * @param capacity
	 *            Groesse von Pool
	 */
	public Pool(int capacity) {
		if (capacity < 1) {
			throw new IllegalArgumentException("Falsche Eingabe, muss groesser 1 sein");
		}
		this.capacity = capacity;
	}

	/**
	 * Gibt Totale Anzahl Worker zurück
	 * @return amountWorker
	 */
	public int getCapacity() {
		return capacity;
	}

}
