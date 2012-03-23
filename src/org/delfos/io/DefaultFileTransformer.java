package org.delfos.io;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;

public class DefaultFileTransformer implements FileTransformer {

	/**
	 * Copy the source to the result with no transformation.
	 */
	public void transform(InputStream fileSource,
			OutputStream fileResult) throws FileTransformException {

		try{
			IOUtils.copy(fileSource, fileResult);
		}catch(Exception ex){
			throw new FileTransformException(ex);
		}		

	}

}
