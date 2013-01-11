package org.delfos.mirth.hie;

import java.io.ByteArrayOutputStream;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Logger;

public class A12HL72SiliconConverter extends AbstractHL7SiliconConverter {

	private static final Logger log = Logger.getLogger(A12HL72SiliconConverter.class);
	
	public String process(String msg) throws IllegalArgumentException{
		
		log.debug("Mensaje HL7-XML v2.3:\n" + msg);
		
		Source msgSource = super.dropNameSpace(msg);
		
		try{
	
			Result result = new StreamResult(new ByteArrayOutputStream());		
		
			Transformer xmlTrans = transformers.get(A12_TRANSFORMER);
			
			super.setParameters(((DOMSource)msgSource).getNode(), xmlTrans);
			
			xmlTrans.transform(msgSource, result);
			ByteArrayOutputStream baos = (ByteArrayOutputStream)((StreamResult)result).getOutputStream();
			
			log.debug("Mensaje HL7_XML v2.5:\n" + baos.toString(UTF8));
			
			return baos.toString(UTF8);
			
		}catch(Exception ex){
			log.error(ex);
			throw new IllegalArgumentException(ex);
		}			

	}


}
