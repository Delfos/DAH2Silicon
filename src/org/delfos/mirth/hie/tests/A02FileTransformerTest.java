package org.delfos.mirth.hie.tests;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.net.URL;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

import junit.framework.TestCase;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.delfos.mirth.hie.A02FileTransformer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.parser.Parser;
import ca.uhn.hl7v2.parser.PipeParser;
import ca.uhn.hl7v2.util.EncodedMessageComparator;
import ca.uhn.hl7v2.util.Terser;
import ca.uhn.hl7v2.validation.impl.NoValidation;

public class A02FileTransformerTest {

	private static final Logger log = Logger.getLogger(A02FileTransformerTest.class);	
	private static final A02FileTransformer a02FT = new A02FileTransformer();
	
	private Parser parser;	
	
	private byte[] a02_c1;	
	private byte[] a02_c2;
	private byte[] a02_c3;
	
	@Before
	public void setUp() throws Exception {
		
		parser = new PipeParser();
		parser.setValidationContext(new NoValidation());
		
		InputStream a02_c1_IS = this.getClass().getClassLoader().getResourceAsStream("org/delfos/mirth/hie/tests/A02_C1.ADM");		
		a02_c1 = IOUtils.toByteArray(a02_c1_IS);
		
		InputStream a02_c2_IS = this.getClass().getClassLoader().getResourceAsStream("org/delfos/mirth/hie/tests/A02_C2.ADM");		
		a02_c2 = IOUtils.toByteArray(a02_c2_IS);
		
		InputStream a02_c3_IS = this.getClass().getClassLoader().getResourceAsStream("org/delfos/mirth/hie/tests/A02_C3.ADM");		
		a02_c3 = IOUtils.toByteArray(a02_c3_IS);				
		
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testTransformC1(){
		
		ByteArrayInputStream bais = new ByteArrayInputStream(a02_c1);		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
			
		
		try{		
			
			a02FT.transform(bais, baos);			
			
			//Para poder comparar la entrada con el resultado tenemos que usar el parseador
			String inputStr = new String(a02_c1);
			inputStr = parser.encode(parser.parse(inputStr)); 
			
			String outputStr = new String(baos.toString());
			
			TestCase.assertTrue(EncodedMessageComparator.equivalent(inputStr, outputStr));
			
		}catch(Exception ex){
			log.error(ex);
			TestCase.fail(ex.getMessage());
		}
		
	}
	
	@Test
	public void testTransformC2(){
		
		ByteArrayInputStream bais = new ByteArrayInputStream(a02_c2);		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
			
		
		try{		
			
			a02FT.transform(bais, baos);			
			
			//Para poder comparar la entrada con el resultado tenemos que usar el parseador
			String inputStr = new String(a02_c2);
			inputStr = parser.encode(parser.parse(inputStr)); 
			
			String outputStr = new String(baos.toString());
			
			TestCase.assertTrue(!EncodedMessageComparator.equivalent(inputStr, outputStr));
			
			Message outputMsg = parser.parse(outputStr);
			Terser terser = new Terser(outputMsg);
			
			TestCase.assertEquals("ADT", terser.get("MSH-9-1"));
			TestCase.assertEquals("A03", terser.get("MSH-9-2"));
			TestCase.assertEquals("ADT_A03", terser.get("MSH-9-3"));
			
			TestCase.assertEquals(terser.get("PV1-6-1"), terser.get("PV1-3-1"));
			TestCase.assertEquals(terser.get("PV1-6-2"), terser.get("PV1-3-2"));
			TestCase.assertEquals(terser.get("PV1-6-3"), terser.get("PV1-3-3"));
			TestCase.assertEquals(terser.get("PV1-6-4"), terser.get("PV1-3-4"));
			
		}catch(Exception ex){
			log.error(ex);
			TestCase.fail(ex.getMessage());
		}		
		
	}
	
	@Test
	public void testTransformC3(){
		
		ByteArrayInputStream bais = new ByteArrayInputStream(a02_c3);		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
			
		
		try{		
			
			a02FT.transform(bais, baos);			
			
			//Para poder comparar la entrada con el resultado tenemos que usar el parseador
			String inputStr = new String(a02_c3);
			inputStr = parser.encode(parser.parse(inputStr)); 
			
			String outputStr = new String(baos.toString());
			
			TestCase.assertTrue(!EncodedMessageComparator.equivalent(inputStr, outputStr));
			
			Message outputMsg = parser.parse(outputStr);
			Terser terser = new Terser(outputMsg);
			
			TestCase.assertEquals("ADT", terser.get("MSH-9-1"));
			TestCase.assertEquals("A01", terser.get("MSH-9-2"));
			TestCase.assertEquals("ADT_A01", terser.get("MSH-9-3"));
			
		}catch(Exception ex){
			log.error(ex);
			TestCase.fail(ex.getMessage());
		}
			
		
	}

	public static void main(String[] args) throws Exception{
		
		InputStream resource = 
			A02FileTransformer.class.getClassLoader().getResourceAsStream("org/delfos/mirth/hie/tests/A02_C1.ADM");
		
		System.out.println("Classpath: " + System.getProperty("java.class.path"));
		System.out.println(IOUtils.toString(resource, "UTF-8"));
		
	}

}
