package ch.zhaw.dna.ssh.mapreduce;

/**
 * Pool kontrolliert Anzahl der Worker und liefert diese an Master
 * 
 * @author Desiree Sacher
 * 
 */
public class Pool {

	private final Worker[] workerList;
	private int pos = 0;

	/**
	 * Setzt Pool Groesse = Anz Worker durch Uebergabewert fest
	 * 
	 * @throws IllegalArgumentException
	 *             Wenn Wert kleiner 1
	 * @param capacity
	 *            Groesse von Pool
	 * @param factory
	 *            Neue Worker werden durch Factory Pattern erstellt
	 */
	public Pool(int capacity, WorkerFactory factory) {
		if (capacity < 1) {
			throw new IllegalArgumentException("Falsche Eingabe, muss groesser 1 sein");
		}
		workerList = new Worker[capacity];
		for (int i = 0; i < workerList.length; i++) {
			workerList[i] = factory.createWorker();
		}
	}

	/**
	 * Gibt Totale Anzahl Worker zurueck
	 * 
	 * @return amountWorker
	 */
	public int getCapacity() {
		return workerList.length;
	}

	/**
	 * Gibt Anzahl Verfuegbare Workers zurueck
	 * @return Anz Verfuegbare Workers
	 */
	public int getFreeWorkers() {
		return workerList.length - pos;
	}

	/**
	 * Weist Worker zu und entfernt diesen aus verfuegbare Workers
	 * @return Worker wenn noch verfuegbar, sonst null
	 */
	public Worker pop() {
		if (getFreeWorkers() < 1) {
			return null;
		} else {
			Worker w = workerList[pos];
			workerList[pos] = null;
			pos++;
			return w;
		}
	}

}
