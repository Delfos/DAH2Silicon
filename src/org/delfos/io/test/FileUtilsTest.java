package org.delfos.io.test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Hashtable;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

import org.apache.commons.io.FilenameUtils;
import org.delfos.io.DefaultFileTransformer;
import org.delfos.io.FTName;
import org.delfos.io.FileTransformException;
import org.delfos.io.FileTransformer;
import org.delfos.io.FileUtils;
import org.delfos.mirth.hie.A17Step01FileTransformer;
import org.delfos.mirth.hie.A17Step02FileTransformer;
import org.junit.Test;

import ca.uhn.hl7v2.HL7Exception;


public class FileUtilsTest {
	
	private static File dir;
	
	//Valid hl7 ADT_A17 file
	private static File file1;
	
	//Invalid hl7 file
	private static File file2;
	
	
	
	
	static{
		
		dir = new File("E:/novale/procdir");
		file1 = new File("E:/novale/ADT0000117.ADM");
		file2 = new File("E:/novale/invalid.txt");
		
	}
	
	/**
	 * Copy with null values of parameters destFileName and ft. 
	 */
	@Test
	public void testCopyFile2Directory_C1() throws Exception {
				
		try{
			
			FileUtils.copyFile2Directory(file1, dir, null, null);
			
			fail();
			
		}catch(FileTransformException ex){		
			assertTrue(file1.exists());						
		}		

	}
	
	/**
	 * srcFile doesn't exist.
	 */
	@Test	
	public void testCopyFile2Directory_C2() throws Exception{
		
		try{			
			
			FileTransformer ft = new DefaultFileTransformer();
			
			FileUtils.copyFile2Directory(new File("Invalid"), dir, "Invalid", ft);		
			fail();
			
		}catch(FileNotFoundException ex){
			assertTrue(true);
		}
		
	}
	
	/**
	 * dir doesn't exist.
	 */
	@Test	
	public void testCopyFile2Directory_C3() throws Exception{
		
		try{
			
			FileTransformer ft = new DefaultFileTransformer();
			
			FileUtils.copyFile2Directory(file1, new File("invalid"), "Invalid", ft);		
			fail();
			
		}catch(IOException ex){
			assertTrue(true);
		}
		
	}
	
	/**
	 * Valid transformation
	 */
	@Test	
	public void testCopyFile2Directory_C4() throws Exception{
		
		DefaultFileTransformer dft = new DefaultFileTransformer();
		String fileName = FilenameUtils.getName(file1.getAbsolutePath());
		
		FileUtils.copyFile2Directory(file1, dir, fileName, dft);
		
		String dirPath = dir.getAbsolutePath();
		fileName = file1.getName();
		
		assertTrue(file1.exists());
		assertTrue( new File(dirPath + "/" +  fileName).exists() );
		
		A17Step01FileTransformer a17ft = new A17Step01FileTransformer();
		String endName = fileName + ".trans";
		
		FileUtils.copyFile2Directory(file1, dir, endName, a17ft);
		
		assertTrue(file1.exists());
		assertTrue( new File(dirPath + "/" +  endName).exists() );	
		
	}
	
	/**
	 * Invalid transformation
	 */
	@Test	
	public void testCopyFile2Directory_C5() throws Exception{
		
		try{
			
			A17Step01FileTransformer ft = new A17Step01FileTransformer();			
			
			FileUtils.copyFile2Directory(file2, dir, "invalid", ft);
			fail();
			
		}catch(FileTransformException ex){
			assertTrue(true);
		}
		
	}	
	
	/**
	 * Move with null values of parameters destFileName and ft. 
	 */
	@Test
	public void testMoveFile2Directory_C1() throws Exception {
	
		try{
		
			FileUtils.moveFile2Directory(file1, dir, null, null);
			fail();
		
		}catch(FileTransformException ex){
			
			assertTrue(file1.exists());
			
			
		}
		
	
		
		
	}
	
	/**
	 * srcFile doesn't exist.
	 */
	@Test
	public void testMoveFile2Directory_C2() throws Exception {
		
		try{
			
			A17Step01FileTransformer ft = new A17Step01FileTransformer();
			
			FileUtils.moveFile2Directory(new File("Invalid"), dir, "invalid", ft);		
			fail();
			
		}catch(FileNotFoundException ex){
			assertTrue(true);
		}
		
	}
	
	/**
	 * dir doesn't exist.
	 */	
	@Test
	public void testMoveFile2Directory_C3() throws Exception {
		
		try{
			
			A17Step01FileTransformer ft = new A17Step01FileTransformer();
			
			FileUtils.moveFile2Directory(file1, new File("invalid"), "invalid", ft);		
			fail();
			
		}catch(IOException ex){
			assertTrue(true);
		}
		
	}
	
	/**
	 * Valid transformation
	 */
	@Test
	public void testMoveFile2Directory_C4() throws Exception {
		
		long file2ChkSum = org.apache.commons.io.FileUtils.checksumCRC32(file2);
		DefaultFileTransformer dft = new DefaultFileTransformer();
		
		String dirPath = dir.getAbsolutePath();
		String fileName = file2.getName();
		
		FileUtils.moveFile2Directory(file2, dir, FilenameUtils.getName(fileName), dft);
		
		assertTrue(!file2.exists());
		
		File resFile = new File(dirPath + "/" +  fileName);
		long resFileChkSum = org.apache.commons.io.FileUtils.checksumCRC32(resFile);
		
		assertEquals(file2ChkSum, resFileChkSum);		
		
		long file1ChkSum = org.apache.commons.io.FileUtils.checksumCRC32(file1);
		A17Step01FileTransformer a17ft = new A17Step01FileTransformer();
		fileName = file1.getName();
		String endName = fileName + ".trans";
		
		FileUtils.moveFile2Directory(file1, dir, endName, a17ft);
		
		assertTrue(!file1.exists());
		
		resFile = new File(dirPath + "/" +  endName);
		resFileChkSum = org.apache.commons.io.FileUtils.checksumCRC32(resFile);
		
		assertFalse(resFileChkSum == file1ChkSum);
		
	}
	
	/**
	 * Invalid transformation
	 */	
	@Test
	public void testMoveFile2Directory_C5() throws Exception {
		
		try{
			
			A17Step01FileTransformer ft = new A17Step01FileTransformer();			
			FileUtils.moveFile2Directory(file2, dir, "invalid", ft);
			fail();
			
		}catch(FileTransformException ex){
			
			assertTrue(true);			
			assertTrue(file2.exists());
			
			String dirPath = dir.getAbsolutePath();
			String fileName = file2.getName();
			
			File f = new File(dirPath + "/" + fileName);
			
			assertTrue(!f.exists());

		}		

	}		

	/**
	 * Move with null value of the parameter ftnames.
	 */
	@Test
	public void testCopyFile2Directory_C2$1() throws Exception {
		
		try{
		
			FileUtils.copyFile2Directory(file1, dir, null);
			fail();
		
		}catch(FileTransformException ex){
			assertTrue(file1.exists());	
		}
		
		
		
		
	}
	
	/**
	 * Move with the file with several transformations.
	 */
	@Test
	public void testCopyFile2Directory_C2$2() throws Exception {
		
		long file1ChkSum = org.apache.commons.io.FileUtils.checksumCRC32(file1);
		
		String dirPath = dir.getAbsolutePath();
		String fileName = file1.getName();
		
		//Transformations
		String fileName_01 = FilenameUtils.getBaseName(fileName) + "A02_01." + 
			FilenameUtils.getExtension(fileName);
		String fileName_02 = FilenameUtils.getBaseName(fileName) + "A02_02." + 
			FilenameUtils.getExtension(fileName);
		FileTransformer ft01 = new A17Step01FileTransformer();
		FileTransformer ft02 = new A17Step02FileTransformer();
		
		FTName[] ftNames = {new FTName(fileName_01, ft01), new FTName(fileName_02, ft02)};
		
		FileUtils.copyFile2Directory(file1, dir, ftNames);
		
		assertTrue(file1.exists());
		
		File resFile_01 = new File(dirPath + "/" +  fileName_01);
		long resFileChkSum_01 = org.apache.commons.io.FileUtils.checksumCRC32(resFile_01);
		
		File resFile_02 = new File(dirPath + "/" +  fileName_02);
		long resFileChkSum_02 = org.apache.commons.io.FileUtils.checksumCRC32(resFile_02);		
		
		assertFalse(file1ChkSum == resFileChkSum_01);		
		assertFalse(file1ChkSum == resFileChkSum_02);
		
	}
	
	
	

}
