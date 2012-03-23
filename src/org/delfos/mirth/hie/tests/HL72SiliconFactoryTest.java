package org.delfos.mirth.hie.tests;

import org.delfos.mirth.hie.*;

import junit.framework.TestCase;

public class HL72SiliconFactoryTest extends TestCase {

	/**
	 * Obtiene un convertidor de un tipo de mensaje válido.
	 */
	public void testGetHL72SiliconConverter_C1() {
		
		HL72SiliconConverter conv = HL72SiliconFactory.getHL72SiliconConverter("ADT^A01");
		assertTrue(conv instanceof HL72SiliconConverter);
		
	}
	
	/**
	 * Obtiene un convertidor de un tipo de mensaje no válido
	 */
	public void testGetHL72SiliconConverter_C2() {
				
		try{
			HL72SiliconConverter conv = HL72SiliconFactory.getHL72SiliconConverter("error");
			fail();
		}catch(IllegalArgumentException ex){
			assertTrue(true);
		}		
		
	}

}
