package org.delfos.mirth.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Vector;

import org.apache.commons.io.FileDeleteStrategy;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.delfos.io.DefaultFileTransformer;
import org.delfos.io.FTName;
import org.delfos.io.FileTransformer;
import org.delfos.io.FileTransformerFactory;
import org.delfos.mirth.hie.A17Step01FileTransformer;
import org.delfos.mirth.hie.A17Step02FileTransformer;

import sun.security.krb5.internal.PAEncTSEnc;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.parser.Parser;
import ca.uhn.hl7v2.parser.PipeParser;
import ca.uhn.hl7v2.preparser.PreParser;
import ca.uhn.hl7v2.util.Terser;
import ca.uhn.hl7v2.validation.impl.NoValidation;

/**
 * Controla los mensajes del censo procesados por el canal Mirth del HIE.
 * <P>
 * Admisiones deja los mensajes HL7 en un directorio <code>srcDir</code> y este controlado los va dejando por
 * bloques en un otro directorio <code>procDir</code> para que sean procesados por el canal.
 * 
 * @author alopezg
 */
public class HIEMessagesController implements HL7MessagesController {
	
	
	private static final Logger log = Logger.getLogger(HIEMessagesController.class);
	
	/**
	 * Directorio donde admisiones deja los ficheros HL7
	 */
	private File srcDir;
	
	/**
	 * Directorio de procesado de ficheros HL7
	 */
	private File procDir;
	
	/**
	 * Fichero anzuelo para lanzar el canal del Mirth
	 */
	private File hookFile;
	
	/**
	 * Número de ficheros procesados por el canal de Mirth
	 */
	private int counter;

	/**
	 * Número máximo de ficheros que se envían a procesar
	 */
	private int maxFiles;
	
	/**
	 * Número de mensajes que procesa el canal
	 */
	private int actualFiles;
	
	/**
	 * Tipos de mensajes válidos
	 */
	private static List<String> typeList;
	
	/**
	 * Lista de mensajes con errores
	 */
	private ArrayList<Integer> errList;
	
	/**
	 * File transformer factory.
	 */
	private static FileTransformerFactory ftf;
	
	/**
	 * HL7 parser
	 */
	private static PreParser parser;
	
	private static final String[] hl7Specs = {"MSH-9-2"};
	
	private static final String ADT_A17 = "A17";
	private static final String DEFAULT = "default";
	
	static{
		
		//Valid type messages
		typeList = new ArrayList<String>(7);
		
		typeList.add("ADT-A01");
		typeList.add("ADT-A02");
		typeList.add("ADT-A03");
		typeList.add("ADT-A08");
		typeList.add("ADT-A11");
		typeList.add("ADT-A13");		
		
		//TODO - Establecer en el parseador el juego de caracteres que se va a utilizar
		parser = new PreParser();		
		
		//Build file transformer factory
		Hashtable<String, FileTransformer[]> transformers = 
			new Hashtable<String, FileTransformer[]>();
			
		
		FileTransformer[] fts = {new DefaultFileTransformer(), new A17Step01FileTransformer(), 
				new A17Step02FileTransformer()};
		FileTransformer[] dft = {new DefaultFileTransformer()};
		
		transformers.put(ADT_A17, fts);
		transformers.put(DEFAULT, dft);
		
		ftf = FileTransformerFactory.getFileTransformerFactory(transformers);	
		
	}
	
	/**
	 * Crea una nueva instancia del controlador de mensajes.
	 * 
	 * @param srcDir directorio donde admisiones deja los ficheros HL7
	 * @param procDir directorio de procesado de ficheros HL7
	 * @param hookFile fichero anzuelo para lanzar el canal del Mirth
	 * @param maxFiles número máximo de ficheros que se envían a procesar 
	 */
	public HIEMessagesController(String srcDir, String procDir, String hookFile, int maxFiles){
		
		this.srcDir = new File(srcDir);
		this.procDir = new File(procDir);
		this.hookFile = new File(hookFile);
		this.maxFiles = maxFiles;
		this.counter = 0;
		this.actualFiles = 0;
		this.errList = new ArrayList<Integer>(maxFiles);		
		
	}	
	

	public boolean hasErrors() {
		return this.errList.size() == 0 ? false : true; 
	}

	public int next() throws NoSuchElementException {

		this.counter = this.counter + 1;
		
		if(this.counter >= this.actualFiles)
			throw new NoSuchElementException("No quedan elementos por procesar");
						
		return this.counter;
	}

	public void reset() {		
		this.counter = 0;
		this.actualFiles = 0;
		this.errList.clear();
	}

	public Collection<Integer> getErrors() {
		return this.errList;
	}


	public void addError(int errorMsg) {
		this.errList.add(new Integer(errorMsg));		
	}

	/**
	 * Resetea los valores del controlador y actualiza el valor del número de ficheros que va a procesar el canal. En caso de que no haya ficheros
	 * para procesar en el directorio <code>procDir</code>, los mueve desde el directorio <code>srcDir</code>
	 */
	//TODO - new test cases
	public void processMessages() throws MessageControllerException{

		this.reset();
		
		String[] exts = {"ADM"};
		int procFiles = FileUtils.listFiles(this.procDir, exts, false).size();
				
		if(procFiles == 0){ //No quedan ficheros para procesar
			
			log.info("No quedan ficheros pendientes de procesar.");
			
			try {				
				
				//Copia el fichero anzuelo en procDir
				log.debug("Se copia el fichero anzuelo");
				FileUtils.copyFileToDirectory(this.hookFile, this.procDir);
				
				//Mueve los ficheros del censo a procDir
				log.debug("Se mueven los ficheros del directorio pendiente de procesar al " +
						"directorio de procesados");
				moveFiles2Directory(this.maxFiles);
			
			} catch (Exception e) {				
				MessageControllerException ex = new MessageControllerException();
				ex.initCause(e);
				log.error("Error al procesar mensajes.", ex);
				throw ex;
			}			 
			
		}		
			
		this.actualFiles = FileUtils.listFiles(this.procDir, null, false).size();		
		
	}
	
	public int getCounter() {
		return counter;
	}
	
	//TODO - Test. 
	public boolean isValidType(String type){
		
		if(typeList.contains(type.toUpperCase())){
			log.debug("El mensaje tipo: " + type + ", es válido");
			return true;
		}else{
			log.debug("El mensaje tipo: " + type + ", no es válido");
			return false;
		}		

	}
	
	private void moveFiles2Directory(int filesNumber) 
		throws Exception{		
		
		Collection<File> files = FileUtils.listFiles(srcDir, null, false);
		Iterator<File> filesIterator = files.iterator();
		int maxFilesNumber = files.size();
		
		log.info("Ficheros pendientes de procesar: " + maxFilesNumber);
		
		filesNumber = filesNumber < 0 ? maxFilesNumber : filesNumber;		
		int number = maxFilesNumber > filesNumber ? filesNumber : maxFilesNumber;		
		
		log.info("Ficheros que se envían a procesar: " + number);
		
		for (int i = 0; i < number; i++){
			
			File file = filesIterator.next();
			
			try{
				moveHL7File2Directory(file);
			}catch(Exception ex){
				log.error("Error durante el envio del fichero " + 
						FilenameUtils.getName(file.getName()) +	"al directorio de procesamiento. ", ex);				
			}
			
		}
		
	}
	
	//This isn't my best job :(
	private void moveHL7File2Directory(File file) throws Exception{		
		
		FileInputStream fis = new FileInputStream(file);
		String fileName = FilenameUtils.getName(file.getName());
		
		String fisString = IOUtils.toString(fis);
		
		fis.close(); //It's very important to close this FileInputStream
		
		log.info("Se envia a procesar. Fichero: " + fileName + " . Contenido:\n" + fisString);
		
		try{			
						
			String[] hl7_type = parser.getFields(fisString, hl7Specs);
			
			log.debug("Se envia a procesar mensaje tipo: " + hl7_type[0]);
			
			//The file is not a ADT_A17 message
			if(hl7_type == null || !hl7_type[0].equals(ADT_A17)){						
				
				FileTransformer[] ft = ftf.getFileTransformers(DEFAULT);
				
				org.delfos.io.FileUtils.moveFile2Directory(file, procDir, fileName, ft[0]);				
			
			}else{ //The file is a ADT_A17 message
				
				String baseFileName = FilenameUtils.getBaseName(file.getAbsolutePath());
				String[] newFN = {fileName, baseFileName + "A02_01." + 
						FilenameUtils.getExtension(fileName), baseFileName + "A02_02." + 
						FilenameUtils.getExtension(fileName)};
				FileTransformer[] fts = ftf.getFileTransformers(hl7_type[0]);
				FTName[] FTNames = new FTName[newFN.length];;
				
				for(int i = 0; i < newFN.length; i++){
					FTNames[i] = new FTName(newFN[i], fts[i]);
				}
				
				org.delfos.io.FileUtils.copyFile2Directory(file, procDir, FTNames);
				
			}		
			
			log.info("Fichero " + fileName + " enviado al directorio de procesamiento.");
			
		}finally{
			
			if(fis != null)
				fis.close();
			
			if(file.exists()){
				FileDeleteStrategy.NORMAL.delete(file);				
				
			}
			
		}

	}

}
