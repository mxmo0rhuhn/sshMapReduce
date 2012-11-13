package ch.zhaw.dna.ssh.mapreduce;

/**
 * Pool kontrolliert Anzahl der Worker und liefert diese an Master
 * @author Desiree Sacher
 *
 */
public class Pool {

	private final int amountWorker;

	/**
	 * Setzt Pool Groesse = Anz Worker durch Uebergabewert fest
	 * 
	 * @throws IllegalArgumentException
	 *             Wenn Wert kleiner 1
	 * @param amountWorker
	 *            Groesse von Pool
	 */
	public Pool(int amountWorker) {
		if (amountWorker < 1) {
			throw new IllegalArgumentException("Falsche Eingabe, muss groesser 1 sein");
		}
		this.amountWorker = amountWorker;
	}

	/**
	 * Gibt Anzahl Worker zurück
	 * @return amountWorker
	 */
	public int getPoolSize() {
		return amountWorker;
	}

}
