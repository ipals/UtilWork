package com.util;

import static org.junit.Assert.assertEquals;

import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

public class MySetTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testSize() {
		MySet set = new MySet(100);
		assertEquals("Size implementation failed", 1, set.size());
	}
	
	@Test
	public void testIsEmpty() {
		MySet set = new MySet(100);
		assertEquals("isEmpty implementation failed", true, set.isEmpty());
	}
	
	

}
