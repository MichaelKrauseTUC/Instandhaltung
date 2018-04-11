package com.krause.instandhaltung;

import static org.junit.Assert.*;

import org.junit.Test;

public class MyClassTest {

	@Test
	public void testMultiply() {
		MyClass tester = new MyClass();
		assertEquals(50, tester.multiply(10, 5));
	}

}
