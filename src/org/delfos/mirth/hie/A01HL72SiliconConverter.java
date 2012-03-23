package org.delfos.mirth.hie;

import java.io.*;
import java.nio.charset.Charset;

import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.xpath.*;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

/**
 * Convierte los mensajas de tipo ADT_A01.
 * 
 * @author alopezg
 *
 */
public class A01HL72SiliconConverter extends AbstractHL7SiliconConverter implements HL72SiliconConverter {

	private static final Logger log = Logger.getLogger(A01HL72SiliconConverter.class);
	
	public String convert(String msg) throws IllegalArgumentException{
		
		log.debug("Mensaje HL7-XML v2.3:\n" + msg);
		
		Source msgSource = super.dropNameSpace(msg);		
		Result result = new StreamResult(new ByteArrayOutputStream());		
		
		try{
			transformers.get(A01_TRANSFORMER).transform(msgSource, result);
			ByteArrayOutputStream baos = (ByteArrayOutputStream)((StreamResult)result).getOutputStream();
			
			log.debug("Mensaje HL7_XML v2.5:\n" + baos.toString(UTF8));
			
			return baos.toString(UTF8);
		}catch(Exception ex){
			log.error(ex);
			throw new IllegalArgumentException(ex);
		}			

	}
	

}
