package org.delfos.mirth.blue;

import java.io.FileInputStream;

import org.apache.commons.io.IOUtils;

import com.sun.org.apache.xalan.internal.xsltc.runtime.Hashtable;

import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.parser.Parser;
import ca.uhn.hl7v2.parser.PipeParser;
import ca.uhn.hl7v2.util.Terser;
import ca.uhn.hl7v2.validation.impl.NoValidation;

public class DAEMessagesUtils {
	
	private static Parser parser;
	
	static{
		parser = new PipeParser();
		parser.setValidationContext(new NoValidation());
	}
	
	public static String transformS058Message(String msg, String msh10Value) throws Exception{
		
		Message hl7Msg = parser.parse(msg);
		
		Terser terser = new Terser(hl7Msg);
		
		terser.set("MSH-10", msh10Value);
		
		//TODO - pendiente
		return parser.encode(hl7Msg);
		
	}
	
	public static void main(String[] args) throws Exception{
		
		String msg = IOUtils.toString(new FileInputStream("e:/tmp/prueba.txt"));
		
		System.out.println(transformS058Message(msg, "444"));		
		
	}

}
