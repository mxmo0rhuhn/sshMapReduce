package ch.zhaw.dna.ssh.mapreduce;

import static org.junit.Assert.*;

import org.junit.Test;

public class PoolTest {

	/**
	 * Pool verwaltet Ressourcen und vergibt Auftraege an worker
	 * 
	 * Konstruktor: erstellt Pool(AnzWorker)
	 */
	
	@Test
	public void testNumerOfPoolWorkers() {
		Pool pool = new Pool(5);
		assertEquals("Pruefen ob korrekte Anz Worker im Pool erstellt wurden: ", 5, pool.getCapacity());
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void invalidNumerOfPoolWorkers() {
		new Pool(0);
	}
}
