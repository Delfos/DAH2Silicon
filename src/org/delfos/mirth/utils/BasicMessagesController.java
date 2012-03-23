package org.delfos.mirth.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.delfos.io.DefaultFileTransformer;

/**
 * Implementación por defecto de la interfaz MessagesController.
 * <P>
 * Admisiones deja los mensajes HL7 en un directorio <code>srcDir</code> y este controlador los va dejando por
 * bloques en un otro directorio <code>procDir</code> para que sean procesados por el canal.
 * 
 * @author alopezg
 */
public class BasicMessagesController implements HL7MessagesController {
	
	
	private static final Logger log = Logger.getLogger(BasicMessagesController.class);
	
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
	 * Extensiones válidas para los ficheros de admisiones
	 */
	private String[] exts;
	
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
	//private static List<String> typeList;
	
	/**
	 * Lista de mensajes con errores
	 */
	private ArrayList<Integer> errList;
	
	/**
	 * Crea una nueva instancia del controlador de mensajes.
	 * 
	 * @param srcDir directorio donde admisiones deja los ficheros HL7
	 * @param procDir directorio de procesado de ficheros HL7
	 * @param hookFile ruta al fichero anzuelo para lanzar el canal del Mirth
	 * @param exts estensión de los ficheros que se envían a procesar
	 * @param maxFiles número máximo de ficheros que se envían a procesar 
	 */
	public BasicMessagesController(String srcDir, String procDir, String hookFile, String[] exts, int maxFiles){
		
		this.srcDir = new File(srcDir);
		this.procDir = new File(procDir);
		this.hookFile = new File(hookFile);
		this.exts = exts;
		this.maxFiles = maxFiles;
		this.counter = 0;
		this.actualFiles = 0;
		this.errList = new ArrayList<Integer>();		
		
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
	 * Realiza las siguientes operaciones:
	 * <ul>
	 * 	<li>Resetea los valores del controlador</li>
	 * 	<li>Actualiza el valor del número de ficheros que va a procesar el canal</li>
	 * 	<li>Copia el fichero anzuelo al directorio <code>procDir</code></li>
	 *	<li>En caso de que no haya ficheros para procesar en el directorio <code>procDir</code>, los mueve desde el 
	 *		directorio <code>srcDir</code>
	 *	</li> 
	 * </ul>
	 */
	//TODO - new test cases
	public void processMessages() throws MessageControllerException{

		this.reset();
		
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
	
	/**
	 * No hace comprobaciones sobre el contenido del los ficheros que se envían a procesar.
	 * 
	 * @return Siempre devuelve true.
	 */
	public boolean isValidType(String type){
		return true;		
	}
	
	private void moveFiles2Directory(int filesNumber) 
		throws Exception{		
		
		Collection<File> files = FileUtils.listFiles(srcDir, exts, false);
		Iterator<File> filesIterator = files.iterator();
		int maxFilesNumber = files.size();
		
		log.info("Ficheros pendientes de procesar: " + maxFilesNumber);
		
		//Si no se ha acotado el número de ficheros, se mueven todos
		filesNumber = filesNumber < 0 ? maxFilesNumber : filesNumber;		
		int number = maxFilesNumber > filesNumber ? filesNumber : maxFilesNumber;		
		
		log.info("Ficheros que se envían a procesar: " + number);
		
		DefaultFileTransformer ft = new DefaultFileTransformer();
		
		for (int i = 0; i < number; i++){
			
			File file = filesIterator.next();
			
			try{
				org.delfos.io.FileUtils.moveFile2Directory(file, procDir, file.getName(), ft);
			}catch(Exception ex){
				log.error("Error durante el envio del fichero " + 
						FilenameUtils.getName(file.getName()) +	" al directorio de procesamiento. ", ex);				
			}
			
		}
		
	}
	

}
