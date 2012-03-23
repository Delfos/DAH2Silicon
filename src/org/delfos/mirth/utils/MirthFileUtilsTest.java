package org.delfos.mirth.utils;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.io.FileUtils;

import junit.framework.TestCase;

public class MirthFileUtilsTest extends TestCase {

	private static final String srcFile = "prueba.txt";
	private static final String srcDir = "e:/novale";
	private static final String destDir = srcDir + "/out";
	private static final String emptyDir = srcDir + "/empty";
	private static final String purgedDir = srcDir + "/purged";
	
	public void setUp(){
		
		File srcDirFile = new File(srcDir);
		File destDirFile = new File(destDir);
		File emptyDirFile = new File(emptyDir);
		
		Iterator<File> destDirIterator = FileUtils.listFiles(destDirFile, null, false).iterator();
		Iterator<File> emptyDirIterator = FileUtils.listFiles(emptyDirFile, null, false).iterator();
		
		try{
		
			while(destDirIterator.hasNext()){
				FileUtils.copyFileToDirectory(destDirIterator.next(), srcDirFile);
			}
			
			while(emptyDirIterator.hasNext()){
				FileUtils.copyFile(emptyDirIterator.next(), srcDirFile);
			}			
			
		}catch(IOException ex){
			ex.printStackTrace();
		}		
		
	}
	
	public void tearDown(){
		this.setUp();
	}
	
	public void testMoveFileToDirectory_C1() {
		
		try{			
			
			MirthFileUtils.moveFileToDirectory(srcDir + "/" + srcFile, destDir);
			
			File destFile = new File(destDir + "/" + srcFile);			
			assertTrue(destFile.exists());		
			
		}catch(IOException ex){
			fail(ex.getMessage());			
		}	
		
	}
	
	public void testMoveFileToDirectory_C2() {
		
		try{			
			
			MirthFileUtils.moveFileToDirectory(srcDir + "/" + srcFile + "XXX", destDir);			
			fail();
			
		}catch(IOException ex){
			assertTrue(true);			
		}
		
	}
	
	/**
	 * Hace la comprobación en un directorio no vacio.
	 */
	public void testIsDirecotryEmpty_C1(){
		
		boolean isEmpty = MirthFileUtils.isDirectoryEmpty(srcDir);		
		assertFalse(isEmpty);
		
	}
	
	/**
	 * Hace la comprobación en un directorio vacio
	 */
	public void testIsDirectoryEmpty_C2(){
		
		boolean isEmpty = MirthFileUtils.isDirectoryEmpty(emptyDir);
		assertTrue(isEmpty);
		
	}
	
	/**
	 * Mueve tres ficheros a un directorio
	 */
	public void testMoveFilesToDirectory_C1(){
		
		try{
			
			MirthFileUtils.moveFilesToDirectory(srcDir, destDir, 3);
			String[] ext = {"ADM"}; 
			Collection<File> files = FileUtils.listFiles(new File(destDir), ext, false);
			
			assertEquals(files.size(), 3);
			
			
		}catch(IOException ex){
			fail(ex.getMessage());
		}
		
	}
	
	/**
	 * Mueve todos los ficheros a un directorio
	 */
	public void testMoveFilesToDirectory_C2(){
		
		try{
			
			String[] ext = {"ADM"};
			
			int filesNumber = FileUtils.listFiles(new File(srcDir), ext, false).size();
			
			MirthFileUtils.moveFilesToDirectory(srcDir, destDir, -1);			 
			Collection<File> files = FileUtils.listFiles(new File(destDir), ext, false);
			
			assertEquals(files.size(), filesNumber);		
			
		}catch(IOException ex){
			fail(ex.getMessage());
		}		
		
	}
	
	/**
	 * Trata de mover ficheros de un directorio vacio
	 */
	public void testMoveFilesToDirectory_C3(){
		
		try{
			
			MirthFileUtils.moveFilesToDirectory(destDir, srcDir, -1);
			MirthFileUtils.moveFilesToDirectory(destDir, srcDir, 3);

			assertTrue(true);		
			
		}catch(IOException ex){
			fail(ex.getMessage());
		}	
		
	}
	
	/**
	 * Cuenta el número de ficheros en un directorio con ficheros
	 */
	public void testCountFileTypes_C1(){
		
		String[] exts = {"ADM"};
		int files = MirthFileUtils.countFileTypes(srcDir, exts);
		int expected = 8;		
		assertEquals(expected, files);		
		
	}
	
	/**
	 * Cuenta el número de ficheros en un directorio vacio
	 */
	public void testCountFileTypes_C2(){
		
		String[] exts = {"ADM"};
		int files = MirthFileUtils.countFileTypes(destDir, exts);
		int expected = 0;		
		assertEquals(expected, files);		
		
	}	
	
	/**
	 * Borra un fichero que existe en un directorio.
	 */
	public void testDeleteFile_C1(){
		
		String dir = "E:/novale";
		String file = "ADT0000004.ADM.xml";
		
		assertTrue(MirthFileUtils.deleteFile(dir, file));
		
	}
	
	/**
	 * Borra un fichero que no existe en un directorio.
	 */
	public void testDeleteFile_C2(){
		
		String dir = "E:/novale";
		String file = "ADT0000004.ADM.xml";
		
		assertFalse(MirthFileUtils.deleteFile(dir, file));
		
	}
	
	/**
	 * Elimina del directorio todos los ficheros y directorios que no tengan las 
	 * extensiones ADM ni hook.
	 * 
	 * Esta prueba hay que hacerla de tres formas:
	 * 
	 * 		1.- Con ficheros válidos y no válidos en el directorio.
	 *		2.- Sin ficheros válidos y con ficheros no válidos en el directorio.
	 *		3.- Con ficheros válidos y sin ficheros no válidos en el directorio.
	 * 
	 */
	public void testPurgeDirectory_C1(){
		
		File dir = new File(purgedDir);
		
		String[] validExts = {"ADM", "hook"};
		Collection<File> initValidFiles = FileUtils.listFiles(dir, validExts, false);
		
		MirthFileUtils.purgeDirectory(this.purgedDir, validExts);
		
		Collection<File> finalValidFiles = FileUtils.listFiles(dir, validExts, false);
		
		//No se debe eliminar ningún fichero válido
		super.assertEquals(initValidFiles.size(), finalValidFiles.size());
			
		Collection<File> allFiles = FileUtils.listFiles(dir, null, false);		

		//Sólo deben quedar los ficheros válidos
		super.assertEquals(allFiles.size(), initValidFiles.size());
		
	}
	
	/**
	 * Elimina del directorio todos los ficheros.
	 */
	public void testPurgeDirectory_C2(){
		
		File dir = new File(this.purgedDir);
		String[] validExts = {};
		
		MirthFileUtils.purgeDirectory(this.purgedDir, validExts);
		
		Collection<File> files = FileUtils.listFiles(dir, null, false);
		
		super.assertEquals(0, files.size());
		
		MirthFileUtils.purgeDirectory(this.purgedDir, null);
		
		super.assertEquals(0, files.size());

	}
	
	/**
	 * El directorio a purgar está vacio.
	 */
	public void testPurgeDirectory_C3(){
		
		File dir = new File(this.purgedDir);
		String[] validExts = {"ADM", "hook"};
		
		MirthFileUtils.purgeDirectory(this.purgedDir, validExts);
		
		Collection<File> files = FileUtils.listFiles(dir, validExts, false);
		
		super.assertEquals(0, files.size());
		
	}
	
	/**
	 * El directorio a purgar no existe.
	 */
	public void testPurgeDirectory_C5(){
		
		try{
			MirthFileUtils.purgeDirectory("invalid", null);
		}catch(NullPointerException ex){
			super.assertTrue(true);
		}

	}	
	
}
