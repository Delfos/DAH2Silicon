package org.delfos.mirth.blue;

import java.io.FileInputStream;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import com.sun.org.apache.xalan.internal.xsltc.runtime.Hashtable;

import ca.uhn.hl7v2.model.Group;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.Segment;
import ca.uhn.hl7v2.parser.Parser;
import ca.uhn.hl7v2.parser.PipeParser;
import ca.uhn.hl7v2.util.SegmentFinder;
import ca.uhn.hl7v2.util.Terser;
import ca.uhn.hl7v2.validation.impl.DefaultValidation;
import ca.uhn.hl7v2.validation.impl.NoValidation;

public class DAEMessagesUtils {
	
	private static Logger log = Logger.getLogger(DAEMessagesUtils.class);
	
	private static Parser parser;
	
	private static int msgId = 0;
	
	static{
		parser = new PipeParser();
		parser.setValidationContext(new NoValidation());
		//parser.setValidationContext(new DefaultValidation());
	}
	
	/**
	 * Procesa el mensaje de modificación de datos de pacientes que envía DAE (servicio S058) para que sea procesado por
	 * Blue.
	 * 
	 * @param msg mensaje de modificación de datos de pacientes de DAE.
	 * 
	 * @return mensaje de moficación de datos de pacientes de Blue.
	 * @throws Exception
	 */
	public static String transformS058Message(String msg) throws Exception{
		
		msgId = ++msgId;		
		
		log.info("Mensaje inicial: " + msg);
		log.info("Id. mensaje: " + msgId);
		
		try{
			Message hl7Msg = parser.parse(msg);		
			Terser terser = new Terser(hl7Msg);		
			
			terser.set("MSH-3-1", "EXTERNO");
			terser.set("MSH-3-2", "");
			terser.set("MSH-4-1", "CENSO");
			terser.set("MSH-4-2", "");
			terser.set("MSH-5", "GRIFOLS");
			terser.set("MSH-6", "BLUE");
			terser.set("MSH-8", "");
			terser.set("MSH-10", Integer.toString(msgId));			
			terser.set("MSH-11", "P");
			terser.set("MSH-15", "");
			terser.set("MSH-16", "");
			terser.set("MSH-17", "");
			terser.set("MSH-18", "");
			
			
			if(terser.get("MSH-9-2").equals("A40")) //Mensaje de fusión de pacientes				
				terser.set("MSH-9-2", "A34");
			
			terser.set("MSH-9-3", "");
			
			terser.set("EVN-1", terser.get("MSH-9-2"));
			
			if(terser.get("MSH-9-2").equals("A31")){
				
				terser.set("PID-3(0)-5", "NHC");				
				terser.set("PID-3(2)-5", "DNI");
				
				terser.set("PID-5-3", terser.get("PID-5-2"));
				terser.set("PID-5-2", terser.get("PID-6-1"));
				
				String sex = terser.get("PID-8");
				
				if(sex.equals("0"))
					terser.set("PID-8", "M");
				else if(sex.equals("1"))
					terser.set("PID-8", "F");
				else
					terser.set("PID-8", "O");
				
				
			}else if(terser.get("MSH-9-2").equals("A34")){
				
				terser.set("PATIENT/PID-3(0)-4", "");
				terser.set("PATIENT/PID-3(0)-5", "NHC");
				terser.set("PATIENT/PID-3(1)", "");
				
				terser.set("PATIENT/PID-5-3", terser.get("PATIENT/PID-5-2"));
				terser.set("PATIENT/PID-5-2", terser.get("PATIENT/PID-6-1"));
				
				terser.set("PATIENT/MRG-1(0)-4", "");
				terser.set("PATIENT/MRG-1(0)-5", "");
				terser.set("PATIENT/MRG-1(1)", "");
				terser.set("PATIENT/MRG-1(1)-1", "");
				terser.set("PATIENT/MRG-1(1)-4", "");
				terser.set("PATIENT/MRG-1(1)-5", "");
				terser.set("PATIENT/MRG-1(1)-9-1", "");
				terser.set("PATIENT/MRG-1(1)-9-3", "");
				
			}		
			
			return parser.encode(hl7Msg);
		
		}catch(Exception ex){
			
			log.error(ex);
			throw ex;
			
		}
		
	}
	
	
	
	public static void main(String[] args) throws Exception{
		
		String msg = IOUtils.toString(new FileInputStream("e:/tmp/AN1342305814.A31.adm"));
		System.out.println(msg);
		System.out.println();
		
		//String result = msg.replaceAll("ADT_A30", "ADT_34");
		//System.out.println(result);
		
		//System.out.println();
		
		/*
		Message hl7Msg = parser.parse(msg);
		
		Terser terser = new Terser(hl7Msg);
		
		System.out.println(terser.get("PATIENT/PID-3(0)-1"));
		System.out.println();
		
		System.out.println(terser.get("PID-3(0)-1"));
		System.out.println();
		
		
		SegmentFinder segFinder = terser.getFinder();
		
		
		Group grp = segFinder.findGroup("P*", 0);
		System.out.println(grp.getName());
		System.out.println();
		
		
		Segment seg = segFinder.findSegment("*PID", 0);
		System.out.println(seg.getName());
		
		System.out.println();
		
		Group grpParent = seg.getParent();
		System.out.println("PID parent: " + grpParent.getName());
		*/
		
		System.out.println(transformS058Message(msg));		
		
	}

}
