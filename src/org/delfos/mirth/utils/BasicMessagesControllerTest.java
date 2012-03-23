package org.delfos.mirth.utils;

import static org.junit.Assert.*;

import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Properties;
import java.util.zip.Checksum;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.NameFileFilter;
import org.junit.BeforeClass;
import org.junit.Test;

public class BasicMessagesControllerTest {

	//Directorio donde se cogen los ficheros
	private static File srcDir;
	
	//Directorio donde se envían los ficheros
	private static File sinkDir;
	
	//Fichero anzuelo
	private static File hook;
	
	//Extensión de los ficheros que se envían a procesar
	private static String[] exts;
	
	//Número máximo de ficheros que se envían a procesar
	private static int maxFiles;
	
	private static BasicMessagesController controller;
	
	//Ficheros contenidos inicialmente en el directorio srcDir
	private static Hashtable<String, Long> initSrcDirFiles;
	
	static{
		
		Properties prop = new Properties();
		
		try{
			prop.load(BasicMessagesControllerTest.class.getClassLoader().getResourceAsStream(
				"org/delfos/mirth/utils/tests.properties"));
		}catch(Exception ex){
			throw new ExceptionInInitializerError(ex);
		}
		
		srcDir = new File(prop.getProperty("BasicMessagesControllerTest.srcDir"));
		sinkDir = new File(prop.getProperty("BasicMessagesControllerTest.sinkDir"));
		hook = new File(prop.getProperty("BasicMessagesControllerTest.hookFile"));
		exts = prop.getProperty("BasicMessagesControllerTest.exts").split(",");
		maxFiles = Integer.valueOf(prop.getProperty("BasicMessagesControllerTest.maxFiles")).intValue();
		
		controller = new BasicMessagesController(srcDir.getPath(), sinkDir.getPath(), hook.getPath(), exts, maxFiles);
		
	}
	
	//Copia el contenido inicial del srcDir
	@BeforeClass
	public static void loadInitFiles() throws Exception{
		
		Enumeration<File> initFilesEnumeration = Collections.enumeration(FileUtils.listFiles(srcDir, null, false));		
		initSrcDirFiles = new Hashtable<String, Long>();
		
		File file = null;
		
		while(initFilesEnumeration.hasMoreElements()){
			file = initFilesEnumeration.nextElement();
			
			initSrcDirFiles.put(file.getName(), FileUtils.checksumCRC32(file));			
		}
		
	}
	
	/**
	 * Verifica que los ficheros copiados al directorio destino se encontraban en el directorio inicial.
	 * Para realizar este test, el directorio inicial debe contener ficheros con la extensión de los ficheros que se
	 * van  a enviar a procesar. 
	 */
	@Test
	public void testProcessMessagesC1() throws Exception{				
		
		try{
			controller.processMessages();
		}catch(Exception ex){
			ex.printStackTrace();
			fail(ex.getMessage());
		}
		
		//Lista todos los ficheros que se han movido al directorio destino
		Enumeration<File> sinkFiles = Collections.enumeration(FileUtils.listFiles(sinkDir, null, false));
		
		while(sinkFiles.hasMoreElements()){
			
			File sinkFile = sinkFiles.nextElement();			
			
			if(initSrcDirFiles.containsKey(sinkFile.getName())){
				assertEquals(initSrcDirFiles.get(sinkFile.getName()).longValue(), FileUtils.checksumCRC32(sinkFile));
			}else{
				fail("El fichero " + sinkFile.getName() + " no se encontraba en el directorio inicial");
			}
			
		}		
		
	}
	
	//Verifica que los ficheros movidos al directorio destino se han eliminado del directorio inicial
	@Test
	public void testProcessMessagesC2() throws Exception{
		
		Iterator<File> sinkDirFiles = FileUtils.listFiles(sinkDir, exts, false).iterator();
		
		while(sinkDirFiles.hasNext()){
			
			File file = sinkDirFiles.next();
			File checkedFile = new File(srcDir, file.getName());
			
			if(checkedFile.exists()){
				fail("El fichero " + checkedFile.getName() + " sigue existiendo en el directorio origen");
			}
			
		}
		
	}
	
	//Verifica que los ficheros que se han copiado tienen la extensión correcta
	@Test
	public void testProcessMessagesC3() throws Exception{
		
		Collection<File> sinkDirFiles = FileUtils.listFiles(sinkDir, exts, false);
		Collection<File> allSinkDirFiles = FileUtils.listFiles(sinkDir, null, false);
		
		//Eliminamos el fichero hook de la lista total de ficheros
		assertEquals(sinkDirFiles.size(), allSinkDirFiles.size() - 1);
		
	}
	
	//Verifica que no se supera el número máximo de ficheros movidos
	//Para realizar esta prueba, el número máximo de ficheros candidatos a ser movidos tiene que ser mayor que
	//él número máximo de ficheros que se pueden mover
	@Test
	public void testProcessMessagesC4() throws Exception{
		
		int countSinkFiles = FileUtils.listFiles(sinkDir, exts, false).size();		
		assertTrue(countSinkFiles <= this.maxFiles);
	
	}
	
	//Verifica que si quedan ficheros en el directorio de destino no se envían nuevos ficheros
	@Test
	public void testProcessMessagesC5() throws Exception{
		
		int countSrcFiles = FileUtils.listFiles(srcDir, exts, false).size();
		
		// Precondición: se comprueba que hay ficheros en el directorio de destino
		if( FileUtils.listFiles(sinkDir, exts, false).size() == 0 ){
			fail("Precondición violada. Tiene que haber ficheros en el directorio destino");
		}
		
		//Se vuelven a procesar los mensajes
		this.controller.processMessages();
		
		assertEquals(countSrcFiles, FileUtils.listFiles(srcDir, exts, false).size());
		
	}
	
	//Verifica que si el número máximo de ficheros es negativo, se envían todos los ficheros
	@Test
	public void testProcessMessagesC6() throws Exception {
		
		//Se limpia el directorio destino para que el controllador envía los nuevos ficheros
		FileUtils.cleanDirectory(sinkDir);
		
		//Precondición: se comprueba que quedan ficheros pendientes de enviar en el directorio fuente
		if( FileUtils.listFiles(srcDir, exts, false).size() == 0 ){
			fail("Precondición violada. Tienen que quedar ficheros pendientes de enviar al destino");
		}
		
		//Se crea una nueva instalacion de la clase BasicMessagesController con el núemro máximo de ficheros negativo
		BasicMessagesController bmc = new BasicMessagesController(srcDir.getPath(), sinkDir.getPath(), hook.getPath(), exts, 
				-1);
		
		bmc.processMessages();
		
		//Verifica que se han movido todos los ficheros de las extensiones indicadas
		assertTrue(FileUtils.listFiles(srcDir, exts, false).size() == 0);
		
		//Verifica que sólo se han movido los ficheros de las extensiones indicadas
		assertEquals(FileUtils.listFiles(sinkDir, null, false).size() - 1, FileUtils.listFiles(sinkDir, exts, false).size());
		
	}

}
