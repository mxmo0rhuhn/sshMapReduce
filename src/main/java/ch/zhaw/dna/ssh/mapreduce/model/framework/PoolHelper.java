package ch.zhaw.dna.ssh.mapreduce.model.framework;

/**
 * Stellt ein Singleton vom Pool zur Verfuegung.
 * 
 * @author Max, Reto
 * 
 */
public class PoolHelper {

	/**
	 * Gibt eine Instanz von einem Pool zurueck
	 * 
	 * @return einen Pool
	 */
	public static Pool getPool() {
		return SingletonHolder.pool;
	}

	/**
	 * Diese inere Klasse ist fuer einen Thread-Safe Singleton verantwortlich. Siehe hier fuer technische Details:
	 * http://en.wikipedia.org/wiki/Initialization-on-demand_holder_idiom
	 * 
	 */
	private static final class SingletonHolder {
		private static final Pool pool = new Pool();
	}
}