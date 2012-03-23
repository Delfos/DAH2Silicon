package org.delfos.mirth.hie;

import java.io.ByteArrayOutputStream;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Logger;

public class A08HL72SiliconConverter extends AbstractHL7SiliconConverter implements HL72SiliconConverter {

	private static final Logger log = Logger.getLogger(A08HL72SiliconConverter.class);
	
	public String convert(String msg) throws IllegalArgumentException{
		
		log.debug("Mensaje HL7-XML v2.3:\n" + msg);
		
		Source msgSource = super.dropNameSpace(msg);		
		Result result = new StreamResult(new ByteArrayOutputStream());		
		
		try{
			transformers.get(A08_TRANSFORMER).transform(msgSource, result);
			ByteArrayOutputStream baos = (ByteArrayOutputStream)((StreamResult)result).getOutputStream();
			
			log.debug("Mensaje HL7_XML v2.5:\n" + baos.toString(UTF8));
			
			return baos.toString(UTF8);
		}catch(Exception ex){
			log.error(ex);
			throw new IllegalArgumentException(ex);
		}			

	}


}
