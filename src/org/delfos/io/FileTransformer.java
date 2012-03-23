package org.delfos.io;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * An instance of this class can transform a source file into a result file.
 * 
 * @author alopezg
 */
public interface FileTransformer {

	/**
	 * TRansform de <code>fileSource</code> in the <code>fileResult</code>.
	 * 
	 * @param fileSource the input to transform
	 * @param fileResult the result of the transformation
	 * 
	 * @throws FileTransformException if an error occur.
	 */
	public void transform(InputStream fileSource, OutputStream fileResult)
		throws FileTransformException;
	
}
