package org.delfos.io.test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import junit.framework.TestCase;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.delfos.io.DefaultFileTransformer;
import org.delfos.io.FileTransformException;
import org.junit.Before;
import org.junit.Test;

public class DefaultFileTransformerTest {

	private File srcFile;
	private File destFile;
	
	@Before
	public void setUp() throws Exception{
		
		srcFile = new File("E:/novale/initfile.txt");
		destFile = new File("E:/novale/endfile.txt");
		
		String content = new String("Esto es una prueba");
		
		FileOutputStream fos = new FileOutputStream(srcFile);
		
		IOUtils.write(content, fos);
		
	}
	
	/**
	 * Transform a file.
	 */
	@Test
	public void testTransform_C01() throws Exception{

		DefaultFileTransformer dft = new DefaultFileTransformer();
		
		FileInputStream fis = new FileInputStream(srcFile);
		FileOutputStream fos = new FileOutputStream(destFile);
		
		dft.transform(fis, fos);
		
		assertTrue(FileUtils.contentEquals(this.srcFile, this.destFile));
		
	}
	
	/**
	 * The source is null, then the result is null and finally both are null.
	 */
	@Test
	public void testTransform_C02() throws Exception{
		
		DefaultFileTransformer dft = new DefaultFileTransformer();
		
		FileInputStream fis = new FileInputStream(srcFile);
		FileOutputStream fos = new FileOutputStream(destFile);
		
		try{
			dft.transform(null, fos);
		}catch(FileTransformException ex){
			assertTrue(true);
		}
		
		try{
			dft.transform(fis, null);
		}catch(FileTransformException ex){
			assertTrue(true);
		}
		
		try{
			dft.transform(null, null);
		}catch(FileTransformException ex){
			assertTrue(true);
		}
		
	}

}
