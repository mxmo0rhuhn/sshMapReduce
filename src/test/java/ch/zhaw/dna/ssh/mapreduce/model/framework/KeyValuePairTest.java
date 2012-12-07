package ch.zhaw.dna.ssh.mapreduce.model.framework;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class KeyValuePairTest {
	
	@Test(expected=IllegalArgumentException.class)
	public void shouldNotAcceptNullKeys() {
		new KeyValuePair(null, "value");
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void shouldNotAcceptNullValues() {
		new KeyValuePair("key", null);
	}
	
	@Test
	public void shouldSetKey() {
		KeyValuePair kvp = new KeyValuePair("key", "value");
		assertEquals("key", kvp.getKey());
	}

	@Test
	public void shouldSetValue() {
		KeyValuePair kvp = new KeyValuePair("key", "value");
		assertEquals("value", kvp.getValue());
	}

	@Test
	public void differentKeyShouldGiveDifferentHashCode() {
		KeyValuePair kvp1 = new KeyValuePair("key", "value");
		KeyValuePair kvp2 = new KeyValuePair("key2", "value");
		assertFalse(kvp1.hashCode() == kvp2.hashCode());
	}
	
	@Test
	public void differentKeyShouldNotBeEqual() {
		KeyValuePair kvp1 = new KeyValuePair("key", "value");
		KeyValuePair kvp2 = new KeyValuePair("key2", "value");
		assertFalse(kvp1.equals(kvp2));
	}
	
	@Test
	public void differentValueShouldGiveDifferentHashCode() {
		KeyValuePair kvp1 = new KeyValuePair("key", "value");
		KeyValuePair kvp2 = new KeyValuePair("key", "value2");
		assertFalse(kvp1.hashCode() == kvp2.hashCode());
	}
	
	@Test
	public void differentValueShouldNotBeEqual() {
		KeyValuePair kvp1 = new KeyValuePair("key", "value");
		KeyValuePair kvp2 = new KeyValuePair("key", "value2");
		assertFalse(kvp1.equals(kvp2));
	}
	
	@Test
	public void shouldBeEqualWithSameKeyAndValue() {
		KeyValuePair kvp1 = new KeyValuePair("key", "value");
		KeyValuePair kvp2 = new KeyValuePair("key", "value");
		assertEquals(kvp1,kvp2);
	}
	
	@Test
	public void shouldHaveSameHashCodeWithSameKeyAndValue() {
		KeyValuePair kvp1 = new KeyValuePair("key", "value");
		KeyValuePair kvp2 = new KeyValuePair("key", "value");
		assertTrue(kvp1.hashCode() == kvp2.hashCode());
	}
	
	@Test
	public void shouldNotBeEqualsToOtherType() {
		KeyValuePair kvp = new KeyValuePair("key", "value");
		Object o = new Object();
		assertFalse(kvp.equals(o));
	}
}
