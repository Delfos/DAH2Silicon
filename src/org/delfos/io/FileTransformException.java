package org.delfos.io;

/**
 * This exception specifies an problem that occured during the transformation process.
 * 
 * @author alopezg
 */
public class FileTransformException extends Exception{

	public FileTransformException(){}
	
	public FileTransformException(String message){
		super(message);
	}
	
	public FileTransformException(String message, Throwable cause){
		super(message, cause);
	}
	
	public FileTransformException(Throwable cause){
		super(cause);
	}
	
}
