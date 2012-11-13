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
	
	@Test
	public void testPopPool5Workers() {
		Pool pool = new Pool(5);
		pool.pop();
		assertEquals("Gibt Capacity - pop() = AnzVerfuegbare Worker", 4, pool.getFreeWorkers());
	}
	
	@Test
	public void testPopPool1Workers() {
		Pool pool = new Pool(1);
		pool.pop();
		assertEquals("Gibt Capacity - pop() = AnzVerfuegbare Worker", 0, pool.getFreeWorkers());
	}
	
	@Test
	public void testPopPoolWorkers() {
		Pool pool = new Pool(1);
		assertNotNull(pool.pop());
		assertNull(pool.pop());
	}
	
	@Test
	public void testPopPool2Workers() {
		Pool pool = new Pool(2);
		assertNotNull(pool.pop());
		assertNotNull(pool.pop());
		assertNull(pool.pop());
	}
	
	@Test
	public void testPopPool3Workers() {
		Pool pool = new Pool(3);
		assertNotNull(pool.pop());
		assertNotNull(pool.pop());
		assertNotNull(pool.pop());
		assertNull(pool.pop());
	}
	
	@Test
	public void testPopPoolLoopWorkers() {
		Pool pool = new Pool(1000);
		for (int i=0; i < 1000; i++) {
			assertNotNull(pool.pop());
		}
		assertNull(pool.pop());
	}
}
