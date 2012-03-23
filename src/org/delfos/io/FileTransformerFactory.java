package org.delfos.io;

import java.util.Hashtable;

public class FileTransformerFactory {

	private Hashtable<String, FileTransformer[]> transformers;
	 
	private FileTransformerFactory(Hashtable<String, FileTransformer[]> transfomers){
		this.transformers = transfomers;		
	}
	
	public synchronized static FileTransformerFactory getFileTransformerFactory(
			Hashtable<String, FileTransformer[]> transformers){
		
		return new FileTransformerFactory(transformers);		
		
	}
	
	public FileTransformer[] getFileTransformers(String transGroup){
		return transformers.get(transGroup);
	}
	
}
