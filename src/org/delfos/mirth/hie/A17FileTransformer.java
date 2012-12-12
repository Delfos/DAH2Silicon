package org.delfos.mirth.hie;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.delfos.io.AbstractFileTransformer;
import org.delfos.io.FileTransformException;
import org.delfos.io.FileTransformer;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.DataTypeException;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.v231.message.ADT_A02;
import ca.uhn.hl7v2.model.v231.message.ADT_A17;
import ca.uhn.hl7v2.parser.Parser;
import ca.uhn.hl7v2.parser.PipeParser;
import ca.uhn.hl7v2.util.Terser;
import ca.uhn.hl7v2.validation.impl.NoValidation;

public abstract class A17FileTransformer extends AbstractFileTransformer {
	
	private static final Logger log = Logger.getLogger(A17FileTransformer.class);
	
	public void transform(InputStream fileSource,
			OutputStream fileResult) throws FileTransformException {
		
		try{
			
			String hl7Str = this.msgPreprocesor(IOUtils.toString(fileSource));						
			Message hl7Msg = parser.parse(hl7Str);	
			
			log.info("Message parser correctly");			
			
			//ADT_A17 adt_a17 = (ADT_A17)hl7Msg;
			
			//Create the ADT_A02 message
			ADT_A02 adt_a02 = new ADT_A02();
			
			Terser a17Terser = new Terser(hl7Msg);
			Terser a02Terser = new Terser(adt_a02);			
			
			this.copyMSH(a17Terser, a02Terser);
			this.copyEVN(a17Terser, a02Terser);			
			this.copyPID(a17Terser, a02Terser);
			this.copyPV1(a17Terser, a02Terser);
			
			
			//Flush out to file
			String adt_a02_str = parser.encode(adt_a02);
			
			log.info("Created ADT_A02 message");
			log.debug("ADT_A02 message: \n" + adt_a02_str);			
			
			fileResult.write(adt_a02_str.getBytes());			
			
		
		}catch(Exception ex){
			log.error(ex.getMessage(), ex);
			throw new FileTransformException(ex);
		}

	}	

	@Override
	protected void copyMSH(Terser src, Terser dest) throws DataTypeException, HL7Exception{
		
		for(String spec : mshSpec){
			
			if(spec.equals("MSH-9-1")){
				dest.set(spec, "ADT");
			}else if(spec.equals("MSH-9-2")){
				dest.set(spec, "A02");
			}else if(spec.equals("MSH-9-3")){
				dest.set(spec, "ADT_A02");
			}else{			
				dest.set(spec, src.get(spec));
			}
			
		}		
		
	}
	
	@Override
	protected void copyEVN(Terser src, Terser dest) throws HL7Exception{

		for(String spec : evnSpec){
			
			if(spec.equals("EVN-1")){
				dest.set(spec, "A02");
			}else{
				dest.set(spec, src.get(spec));
			}	
			
		}
		
	}
	
	/**
	 * This is necessary because the ADT_A17 we received don't have standard behavior.
	 * This is a shoddy piece of work :(.
	 * NOTE: con los mensajes de DAE no es necesario este m�todo
	 */
	private String msgPreprocesor(String msg){
		
		//String[] msgLines = msg.split(System.getProperty("line.separator"));
		
		//log.debug("Original ADT_A17 message: \n" + msg);
		
		//String validMsg = msgLines[0] + "\n" + msgLines[1] + "\n" + msgLines[2] + "\n" +
			//msgLines[4] + "\n" + msgLines[3] + "\n" + msgLines[5];
		
		//log.debug("Valid ADT_A17 message: \n" + validMsg);
		
		String validMsg = msg;
		
		return validMsg;		
		
	}	
	
}
