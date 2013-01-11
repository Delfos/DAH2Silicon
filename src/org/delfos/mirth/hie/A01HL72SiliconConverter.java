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
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import com.sun.org.apache.xalan.internal.xsltc.DOM;

/**
 * Convierte los mensajas de tipo ADT_A01.
 * 
 * @author alopezg
 *
 */
public class A01HL72SiliconConverter extends AbstractHL7SiliconConverter implements HL72SiliconConverter {

	private static final Logger log = Logger.getLogger(A01HL72SiliconConverter.class);
	
	 
	@Override
	public String process(String msg) throws IllegalArgumentException{
		
		log.debug("Mensaje HL7-XML v2.3:\n" + msg);
		
		Source msgSource = super.dropNameSpace(msg);
		
		try{
	
			Result result = new StreamResult(new ByteArrayOutputStream());		
		
			Transformer xmlTrans = transformers.get(A01_TRANSFORMER);
			
			super.setParameters(((DOMSource)msgSource).getNode(), xmlTrans);
			
			xmlTrans.transform(msgSource, result);			
			ByteArrayOutputStream baos = (ByteArrayOutputStream)((StreamResult)result).getOutputStream();
			
			log.debug("Mensaje HL7_XML v2.5:\n" + baos.toString(UTF8));
			
			String strResult = baos.toString(UTF8);
			
			return strResult;
			
		}catch(Exception ex){
			log.error(ex);
			throw new IllegalArgumentException(ex);
		}			

	}
	
	public static void main(String[] args) throws Exception{
		
		A01HL72SiliconConverter a01Conv = new A01HL72SiliconConverter();
		
		String msg = IOUtils.toString(new FileInputStream("E:/tmp/AN1334170746_A01_H.ADM.xml"));
		
		System.out.println(a01Conv.convert(msg));
		
		
	}
	

}
