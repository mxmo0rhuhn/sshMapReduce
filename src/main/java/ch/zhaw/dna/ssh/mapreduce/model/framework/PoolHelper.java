package ch.zhaw.dna.ssh.mapreduce.model.framework;

import ch.zhaw.dna.ssh.mapreduce.model.framework.impl.AsyncPool;

/**
 * Stellt ein Singleton vom Pool zur Verfuegung.
 * 
 * @author Max, Reto
 * 
 */
public class PoolHelper {

	/**
	 * Instanz wird nicht benotigt
	 */
	private PoolHelper() {
		
	}
	
	/**
	 * Gibt eine Instanz von einem Pool zurueck
	 * 
	 * @return einen Pool
	 */
	public static Pool getPool() {
		return SingletonHolder.POOL;
	}

	/**
	 * Diese inere Klasse ist fuer einen Thread-Safe Singleton verantwortlich. Siehe hier fuer technische Details:
	 * http://en.wikipedia.org/wiki/Initialization-on-demand_holder_idiom
	 * 
	 */
	private static final class SingletonHolder {
		private static final Pool POOL = createPool();

		/**
		 * Erstellt einen Pool und initialisiert ihn. Das darf nur einmal ausgefuert werden!
		 * 
		 * @return einen Pool
		 */
		private static Pool createPool() {
			AsyncPool pool = new AsyncPool();
			pool.init();
			return pool;
		}
	}
}