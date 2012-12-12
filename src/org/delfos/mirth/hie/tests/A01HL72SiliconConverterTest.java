package org.delfos.mirth.hie.tests;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.delfos.mirth.hie.HL72SiliconConverter;
import org.delfos.mirth.hie.HL72SiliconFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import junit.framework.TestCase;

public class A01HL72SiliconConverterTest extends TestCase {

	private static final File A01_FILE_00 = new File("E:/tmp/1350403355953_A01.ADM.xml");
	
	private static BeanFactory beanFtc;
	
	static {
		
		beanFtc = new FileSystemXmlApplicationContext("E:/Grifols/projects/java/mirth/mirth_utils/spring/spring_mirth_hie.xml");		
		
	}

	
	public void testConvert_C01() {
		
		try{
			
			String a01Msg = FileUtils.readFileToString(A01_FILE_00);
			HL72SiliconConverter converter = 
				((HL72SiliconFactory)beanFtc.getBean("hl72SiliconFactory")).getHL72SiliconConverter("ADT^A01");
			
			String a01Silicon = converter.convert(a01Msg);
			
			File rsFile = new File("E:/tmp/result.xml");			
			FileUtils.writeStringToFile(rsFile, a01Silicon);
			
			assertTrue(true);
			
		}catch(Exception ex){
			ex.printStackTrace();
			fail(ex.getMessage());
		}	
		

	}
	
	//Comprueba si se convierten bien m�s de un mensaje
	public void testConvert_C02() {
		
		try{
			
			String a01Msg = FileUtils.readFileToString(A01_FILE_00);
			HL72SiliconConverter converter = 
				HL72SiliconFactory.getHL72SiliconConverter("ADT^A01");
			
			String a01Silicon01 = converter.convert(a01Msg);			
			File rsFile01 = new File("E:/novale/ADT_A01_v2.3.1.silicon.01.xml");			
			FileUtils.writeStringToFile(rsFile01, a01Silicon01);
			
			String a01Silicon02 = converter.convert(a01Msg);
			File rsFile02 = new File("E:/novale/ADT_A01_v2.3.1.silicon.02.xml");
			FileUtils.writeStringToFile(rsFile02, a01Silicon02);
			
			assertTrue(true);
			
		}catch(Exception ex){
			fail(ex.getMessage());
		}	
		

	}

}
