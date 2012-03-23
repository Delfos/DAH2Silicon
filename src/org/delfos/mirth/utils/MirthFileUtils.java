package org.delfos.mirth.utils;

import java.io.IOException;
import java.io.File;

import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.io.FileDeleteStrategy;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.AndFileFilter;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.OrFileFilter;
import org.apache.log4j.Logger;
import org.delfos.io.FileTransformerFactory;

/**
 * Utilidades para manipular ficheros desde Mirth.
 * 
 * @author Antonio M. Lopez
 * @version 1.0
 */
public class MirthFileUtils {
	
	private static final Logger log = Logger.getLogger(MirthFileUtils.class);

	/**
	 * No se puede crear una instancia de esta clase	
	 */
	private MirthFileUtils(){}
	
	/**
	 * Mueve un fichero a un directorio. En caso de no existir el directorio, lo crea.
	 * 
	 * @param srcFile el fichero que va a ser movido
	 * @param destDir el directorio destino del fichero
	 * 
	 * @throws IOException si el fichero no es válido o se produce un error al moverlo. 
	 */
	public static void moveFileToDirectory (String srcFile, String destDir) throws IOException {
		
		File move = new File(srcFile);
		
		FileUtils.copyFileToDirectory(move, new File(destDir), true);
		FileDeleteStrategy.NORMAL.delete(move);		
		
	}
	
	/**
	 * Mueve el número de ficheros indicado de un directorio a otro.
	 * 
	 * @param srcDir directorio origen
	 * @param destDir directorio destino
	 * @param filesNumber número máximo de ficheros a mover. Si es menor que cero se 
	 * 		mueven todos los ficheros
	 *  
	 * @throws IOException si se produce algún error al mover los ficheros
	 */
	public static void moveFilesToDirectory(String srcDir, String destDir, int filesNumber) 
		throws IOException{
		
		Collection<File> files = FileUtils.listFiles(new File(srcDir), null, false);
		Iterator<File> filesIterator = files.iterator();
		int maxFilesNumber = files.size();
		
		filesNumber = filesNumber < 0 ? maxFilesNumber : filesNumber;		
		int number = maxFilesNumber > filesNumber ? filesNumber : maxFilesNumber;
		
		for (int i = 0; i < number; i++){			
			moveFileToDirectory(filesIterator.next().getAbsolutePath(), destDir);			
		}		
		
	}
	
	/**
	 * Comprueba si un directorio está vacio.
	 * 
	 * @param dir directorio que se va a comprobar
	 * 
	 * @return true si el directorio está vacio false en caso contratio
	 */
	public static boolean isDirectoryEmpty (String dir){
	
		File dirFile = new File(dir);
		Collection<File> files = FileUtils.listFiles(dirFile, null, false);
		
		if(files.size() == 0)
			return true;
		else 
			return false;
		
	}
	
	/**
	 * Devuelve el número de ficheros con las extensiones indicadas que hay en un directorio
	 *  dado.
	 * 	
	 * @param dir directorio
	 * @param extension extensiones de los dicheros
	 * 
	 * @return número de ficheros que cumplen con las condiciones
	 */
	public static int countFileTypes(String dir, String[] extension){
		
		File dirFile = new File(dir);
		Collection<File> files = FileUtils.listFiles(dirFile, extension, false);
		
		return files.size();
		
	}
	
	/**
	 * Borra el fichero indicado.
	 * 
	 * @param dir directorio en que se encuentra el fichero
	 * @param file nombre del fichero
	 * 
	 * @return <code>true</code> cuando el fichero se ha borrado con éxito; 
	 * <code>false</code> en cualquier otro caso.
	 */
	public static boolean deleteFile(String dir, String file){
		
		try{
			
			return new File(dir, file).delete();
		
		}catch(Exception ex){
		
			return false;
		
		}		

	}
	
	/**
	 * Borra todos los ficheros del directorio cuyas extensiones sean distintas a las 
	 * indicadas. 
	 * 
	 * @param dir directorio a purgar
	 * @param validExt extensiones válidas. Si no se pone ninguna extensión se borran
	 * todos los ficheros y directorios del directorio <code>dir</code> 
	 */
	public static void purgeDirectory(String dir, String[] validExts){
		
		File dirFile = new File(dir);
		AndFileFilter andFileFilter = new AndFileFilter();
		
		if(validExts == null || validExts.length == 0){ //Borra todo el contenido del directorio
			
			File[] files = dirFile.listFiles();
			
			for(File f : files){
				
				try{
					
					FileUtils.forceDelete(f);
					
					log.info("Borrado fichero: " + f.getName());
				
				}catch(IOException ex){ //No se ha podido borrar el fichero o directorio
					log.warn("Error al borrar el fichero o directorio: " + ex.getMessage());
				}
				
			}
			
		}else{
			
			for(String ext : validExts){			
				
				andFileFilter.addFileFilter(
						FileFilterUtils.notFileFilter(FileFilterUtils.suffixFileFilter(ext)));
				
			}
			
			Iterator<File> files = FileUtils.iterateFiles(dirFile, andFileFilter, 
					FileFilterUtils.trueFileFilter());
			
			while(files.hasNext()){
				
				try{
					
					File f = files.next();
					FileUtils.forceDelete(f);
					
					log.info("Borrado fichero: " + f.getName());
					
				}catch(IOException ex){
					log.warn("Error al borrar el fichero o directorio: "+ ex.getMessage());					
				}
			}
			
		}
		
	}
	
	
}
