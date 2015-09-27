package com.junit.sample;

import static org.junit.Assert.*;

import org.junit.Test;

public class AddTest {

	@Test
	public void test() {
		AddClass add = new AddClass();
		assertEquals(3, add.add(1, 2));
	}

}
