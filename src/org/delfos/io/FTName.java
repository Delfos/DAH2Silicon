package org.delfos.io;

/**
 * Couple of Transformation with the new name.
 * 
 * @author alopezg
 *
 */
public class FTName {

	private String name;
	private FileTransformer ft;
	
	public FTName(String name, FileTransformer ft){
		this.name = name;
		this.ft = ft;
	}
	
	public String getName(){
		return this.name;
	}
	
	public FileTransformer getFt(){
		return this.ft;
	}
	
}
