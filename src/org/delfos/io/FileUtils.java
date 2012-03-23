package org.delfos.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.io.FileDeleteStrategy;
import org.apache.commons.io.output.ByteArrayOutputStream;

public class FileUtils {	

	/**
	 * Transform and copy the file to the specific directory.  
	 * 
	 * @param file file to copy
	 * @param dir destination directory
	 * @param destFileName end file name.
	 * @param ft transformation.
	 * @throws FileNotFoundException when the file doesn't exist
	 * @throws FileTransformException error transforming the file
	 * @throws IOException 
	 */
	//TODO - Test case. Priority: high
	public static void copyFile2Directory(File srcFile, File dir, String destFileName, 
			FileTransformer ft) throws FileNotFoundException, IOException, 
			FileTransformException {

		if(destFileName == null || ft == null)
			throw new FileTransformException("Ni el nombre del fichero de destino ni" +
					"el transformador puden ser nulos");
		
		FileInputStream fis = new FileInputStream(srcFile);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		FileOutputStream fos = null;
		
		try{
			
			ft.transform(fis, baos);			
			fos = new FileOutputStream(new File(dir, destFileName));
			baos.writeTo(fos);
			
			baos.flush();
			fos.flush();			
			
		}finally{
			
			if(fis != null)
				fis.close();
			
			
			if(baos != null)
				baos.close();
			
			
			if(fos != null)
				fos.close();			
			
		}		
		
	}
	
	/**
	 * Transform and copy the file to the specific directory making several transformations..  
	 * 
	 * @param file file to copy
	 * @param dir directory
	 * @param destFileName list of ended file names.
	 * @param ft transformations.
	 * without transformation. 	
	 * 
	 * @throws FileNotFoundException when the file doesn't exist
	 * @throws FileTransformException error transforming the file
	 * @throws IOException
	 */
	//TODO - Test case. Priority: Normal.
	public static void copyFile2Directory(File srcFile, File dir, FTName[] ftNames) 
		throws FileNotFoundException, IOException, FileTransformException {
		
		if(ftNames == null)
			throw new FileTransformException("La lista de transformadores no puede ser nula");
		
		for(FTName ftn : ftNames){
			copyFile2Directory(srcFile, dir, ftn.getName(), ftn.getFt());
		}
		
	}
	
	/**
	 * Transform and move the file to the specific directory.  
	 * 
	 * @param file file to copy
	 * @param dir directory
	 * @param destFileName end file name.
	 * @param ft transformation
	 * @throws FileNotFoundException when the file doesn't exist
	 * @throws FileTransformException error transforming the file
	 * @throws IOException 
	 */
	public static void moveFile2Directory(File srcFile, File dir, String destFileName,
			FileTransformer ft) throws FileNotFoundException, IOException, 
			FileTransformException{
		
		copyFile2Directory(srcFile, dir, destFileName, ft);
		FileDeleteStrategy.NORMAL.delete(srcFile);
		
	}
		
	
	
}
