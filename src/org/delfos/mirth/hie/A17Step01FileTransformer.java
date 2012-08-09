package org.delfos.mirth.hie;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.apache.log4j.Logger;


import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.parser.PipeParser;
import ca.uhn.hl7v2.util.Terser;
import ca.uhn.hl7v2.validation.ValidationContext;
import ca.uhn.hl7v2.validation.impl.NoValidation;

/**
 * Transform an A17 HL7 message in a A02 HL7 message with the first PID and PV1 of the 
 * original message.
 * 
 * @author alopezg
 */
public class A17Step01FileTransformer extends A17FileTransformer {

	public static final Logger log = Logger.getLogger(A17Step01FileTransformer.class);
	
	@Override
	protected void copyPID(Terser src, Terser dest) throws HL7Exception{
		
		for(String spec : pidSpec){
			dest.set(spec, src.get(spec));
		}
		
	}
	
	@Override
	protected void copyPV1(Terser src, Terser dest) throws HL7Exception{		

		for(String spec : pv1Spec){
			dest.set(spec, src.get(spec));			
		}
		
	}

	
	public static void main(String[] args) throws Exception{
		
		
		File hl7File = new File("e:/tmp/1344440074853_A17.ADM");
		File result = new File("e:/tmp/result.txt");		
		
		FileInputStream fis = new FileInputStream(hl7File);
		FileOutputStream fos = new FileOutputStream(result);		
		
		new A17Step01FileTransformer().transform(fis, fos);		
		
	}

}
