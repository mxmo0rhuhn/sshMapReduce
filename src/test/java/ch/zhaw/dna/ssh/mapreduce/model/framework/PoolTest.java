package ch.zhaw.dna.ssh.mapreduce.model.framework;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import ch.zhaw.dna.ssh.mapreduce.model.framework.Pool;
import ch.zhaw.dna.ssh.mapreduce.model.framework.Worker;
import ch.zhaw.dna.ssh.mapreduce.model.framework.WorkerFactory;

public class PoolTest {

	private static WorkerFactory factoryMock;
	
	@BeforeClass
	public static void initMock() {
		factoryMock = new MockFactory();
	}
	
	@Test
	public void testNumerOfPoolWorkers() {
		Pool pool = new Pool(5, factoryMock);
		assertEquals("Pruefen ob korrekte Anz Worker im Pool erstellt wurden: ", 5, pool.getCurrentPoolSize());
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void invalidNumerOfPoolWorkers() {
		new Pool(0, factoryMock);
	}
	
	@Test
	public void testPopPool5Workers() {
		Pool pool = new Pool(5, factoryMock);
		pool.pop();
		assertEquals("Gibt Capacity - pop() = AnzVerfuegbare Worker", 4, pool.getFreeWorkers());
	}
	
	@Test
	public void testPopPool1Workers() {
		Pool pool = new Pool(1, factoryMock);
		pool.pop();
		assertEquals("Gibt Capacity - pop() = AnzVerfuegbare Worker", 0, pool.getFreeWorkers());
	}
	
	@Test
	public void testPopPoolWorkers() {
		Pool pool = new Pool(1, factoryMock);
		assertNotNull(pool.pop());
		assertNull(pool.pop());
	}
	
	@Test
	public void testPopPool2Workers() {
		Pool pool = new Pool(2, factoryMock);
		assertNotNull(pool.pop());
		assertNotNull(pool.pop());
		assertNull(pool.pop());
	}
	
	@Test
	public void testPopPool3Workers() {
		Pool pool = new Pool(3, factoryMock);
		assertNotNull(pool.pop());
		assertNotNull(pool.pop());
		assertNotNull(pool.pop());
		assertNull(pool.pop());
	}
	
	@Test
	public void testPopPoolLoopWorkers() {
		Pool pool = new Pool(1000, factoryMock);
		for (int i=0; i < 1000; i++) {
			assertNotNull(pool.pop());
		}
		assertNull(pool.pop());
	}
	
	private static class MockFactory implements WorkerFactory{

		@Override
		public Worker createWorker() {
			
			return new Worker(){
				
			};
		}
		
	}
}
