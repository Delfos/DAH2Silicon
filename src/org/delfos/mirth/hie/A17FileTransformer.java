package org.delfos.mirth.hie;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
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

public abstract class A17FileTransformer implements FileTransformer {
	
	protected String[] pidSpec = {"PID(0)-1", "PID(0)-2-1", "PID(0)-2-1", "PID(0)-2-2",
			"PID(0)-2-3", "PID(0)-2-4-1", "PID(0)-2-4-2", "PID(0)-2-4-3",
			"PID(0)-2-5", "PID(0)-2-6-1", "PID(0)-2-6-2", "PID(0)-2-6-3", 
			"PID(0)-3-1", "PID(0)-3-2", "PID(0)-3-3", "PID(0)-3-4-1", "PID(0)-3-4-2", 
			"PID(0)-3-4-3", "PID(0)-3-5", "PID(0)-3-6-1", "PID(0)-3-6-2", 
			"PID(0)-3-6-3", "PID(0)-4(0)-1", "PID(0)-4(0)-2",
			"PID(0)-4(0)-3", "PID(0)-4(0)-4-1", "PID(0)-4(0)-4-2", "PID(0)-4(0)-4-3",
			"PID(0)-4(0)-5", "PID(0)-4(0)-6-1", "PID(0)-4(0)-6-2", "PID(0)-4(0)-6-3",
			"PID(0)-5(0)-1-1", "PID(0)-5(0)-1-2", "PID(0)-5(0)-2", "PID(0)-5(0)-3",
			"PID(0)-5(0)-4", "PID(0)-5(0)-5", "PID(0)-5(0)-6", "PID(0)-5(0)-7",
			"PID(0)-5(0)-8", "PID(0)-6(0)-1-1", "PID(0)-6(0)-1-2", "PID(0)-6(0)-2", 
			"PID(0)-6(0)-3", "PID(0)-6(0)-4", "PID(0)-6(0)-5", "PID(0)-6(0)-6", 
			"PID(0)-6(0)-7", "PID(0)-6(0)-8", "PID(0)-7-1", "PID(0)-7-2",
			"PID(0)-8", "PID(0)-9(0)-1-1", "PID(0)-9(0)-1-2", "PID(0)-9(0)-2", 
			"PID(0)-9(0)-3", "PID(0)-9(0)-4", "PID(0)-9(0)-5", "PID(0)-9(0)-6", 
			"PID(0)-9(0)-7", "PID(0)-9(0)-8", "PID(0)-10-1", "PID(0)-10-2", 
			"PID(0)-10-3", "PID(0)-10-4", "PID(0)-10-5", "PID(0)-10-6", "PID(0)-11(0)-1",
			"PID(0)-11(0)-2", "PID(0)-11(0)-3", "PID(0)-11(0)-4", "PID(0)-11(0)-5",
			"PID(0)-11(0)-6", "PID(0)-11(0)-7", "PID(0)-11(0)-8", "PID(0)-11(0)-9",
			"PID(0)-11(0)-10", "PID(0)-11(0)-11", "PID(0)-12", "PID(0)-13(0)-1",
			"PID(0)-13(0)-2", "PID(0)-13(0)-3", "PID(0)-13(0)-4", "PID(0)-13(0)-5",
			"PID(0)-13(0)-6", "PID(0)-13(0)-7", "PID(0)-13(0)-8", "PID(0)-13(0)-9",
			"PID(0)-14(0)-1", "PID(0)-14(0)-2", "PID(0)-14(0)-3", "PID(0)-14(0)-4", 
			"PID(0)-14(0)-5", "PID(0)-14(0)-6", "PID(0)-14(0)-7", "PID(0)-14(0)-8", 
			"PID(0)-14(0)-9", 
			"PID(0)-15-1", "PID(0)-15-2", "PID(0)-15-3","PID(0)-15-4", "PID(0)-15-5", 
			"PID(0)-15-6",
			"PID(0)-16-1", "PID(0)-16-2", 
			"PID(0)-16-3", "PID(0)-16-4", "PID(0)-16-5", "PID(0)-16-6", "PID(0)-17-1", 
			"PID(0)-17-2", "PID(0)-17-3", "PID(0)-17-4", "PID(0)-17-5", "PID(0)-17-6",
			"PID(0)-18-1", "PID(0)-18-1", "PID(0)-18-2",
			"PID(0)-18-3", "PID(0)-18-4-1", "PID(0)-18-4-2", "PID(0)-18-4-3",
			"PID(0)-18-5", "PID(0)-18-6-1", "PID(0)-18-6-2", "PID(0)-18-6-3",
			"PID(0)-19", 
			"PID(0)-20-1", "PID(0)-20-2", "PID(0)-20-3",
			"PID(0)-21-1", "PID(0)-21-1", "PID(0)-21-2", "PID(0)-21-3", "PID(0)-21-4-1", 
			"PID(0)-21-4-2", "PID(0)-21-4-3", "PID(0)-21-5", "PID(0)-21-6-1", 
			"PID(0)-21-6-2", "PID(0)-21-6-3",
			"PID(0)-22-1", "PID(0)-22-2", "PID(0)-22-3","PID(0)-22-4", "PID(0)-22-5", 
			"PID(0)-22-6",
			"PID(0)-23",
			"PID(0)-24",
			"PID(0)-25",
			"PID(0)-26(0)-1", "PID(0)-26(0)-2", "PID(0)-26(0)-3","PID(0)-26(0)-4", 
			"PID(0)-26(0)-5", "PID(0)-26(0)-6",
			"PID(0)-27-1", "PID(0)-27-2", "PID(0)-27-3","PID(0)-27-4", "PID(0)-27-5", 
			"PID(0)-27-6",
			"PID(0)-28-1", "PID(0)-28-2", "PID(0)-28-3","PID(0)-28-4", "PID(0)-28-5", 
			"PID(0)-28-6",
			"PID(0)-29",
			"PID(0)-30"
			};
	
	protected String[] pv1Spec = {
			"PV1-1",
			"PV1-2",
			"PV1-3-1", "PV1-3-2", "PV1-3-3", "PV1-3-4-1", "PV1-3-4-2",
			"PV1-3-4-3", "PV1-3-5", "PV1-3-6", "PV1-3-7", "PV1-3-8",
			"PV1-3-9",
			"PV1-4",
			"PV1-5-1", "PV1-5-2", "PV1-5-3", "PV1-5-4-1", "PV1-5-4-2",
			"PV1-5-4-3", "PV1-5-5", "PV1-5-6-1", "PV1-5-6-2", "PV1-5-6-3",
			"PV1-6-1", "PV1-6-2", "PV1-6-3", "PV1-6-4-1", "PV1-6-4-2",
			"PV1-6-4-3", "PV1-6-5", "PV1-6-6", "PV1-6-7", "PV1-6-8",
			"PV1-6-9",
			"PV1-7(0)-1", "PV1-7(0)-2-1", "PV1-7(0)-2-2", "PV1-7(0)-3",
			"PV1-7(0)-4", "PV1-7(0)-5", "PV1-7(0)-6", "PV1-7(0)-7",
			"PV1-7(0)-8", "PV1-7(0)-9-1", "PV1-7(0)-9-2", "PV1-7(0)-9-3",
			"PV1-7(0)-10", "PV1-7(0)-11", "PV1-7(0)-12", "PV1-7(0)-13",
			"PV1-7(0)-14-1", "PV1-7(0)-14-2", "PV1-7(0)-14-3", "PV1-7(0)-15",
			"PV1-8(0)-1", "PV1-8(0)-2-1", "PV1-8(0)-2-2", "PV1-8(0)-3",
			"PV1-8(0)-4", "PV1-8(0)-5", "PV1-8(0)-6", "PV1-8(0)-7",
			"PV1-8(0)-8", "PV1-8(0)-9-1", "PV1-8(0)-9-2", "PV1-8(0)-9-3",
			"PV1-8(0)-10", "PV1-8(0)-11", "PV1-8(0)-12", "PV1-8(0)-13",
			"PV1-8(0)-14-1", "PV1-8(0)-14-2", "PV1-8(0)-14-3", "PV1-8(0)-15",
			"PV1-9(0)-1", "PV1-9(0)-2-1", "PV1-9(0)-2-2", "PV1-9(0)-3",
			"PV1-9(0)-4", "PV1-9(0)-5", "PV1-9(0)-6", "PV1-9(0)-7",
			"PV1-9(0)-8", "PV1-9(0)-9-1", "PV1-9(0)-9-2", "PV1-9(0)-9-3",
			"PV1-9(0)-10", "PV1-9(0)-11", "PV1-9(0)-12", "PV1-9(0)-13",
			"PV1-9(0)-14-1", "PV1-9(0)-14-2", "PV1-9(0)-14-3", "PV1-9(0)-15",
			"PV1-10",
			"PV1-11-1", "PV1-11-2", "PV1-11-3", "PV1-11-4-1", "PV1-11-4-2",
			"PV1-11-4-3", "PV1-11-5", "PV1-11-6", "PV1-11-7", "PV1-11-8",
			"PV1-11-9",
			"PV1-12",
			"PV1-13",
			"PV1-14",
			"PV1-15(0)",
			"PV1-16",
			"PV1-17(0)-1", "PV1-17(0)-2-1", "PV1-17(0)-2-2", "PV1-17(0)-3",
			"PV1-17(0)-4", "PV1-17(0)-5", "PV1-17(0)-6", "PV1-17(0)-7",
			"PV1-17(0)-8", "PV1-17(0)-9-1", "PV1-17(0)-9-2", "PV1-17(0)-9-3",
			"PV1-17(0)-10", "PV1-17(0)-11", "PV1-17(0)-12", "PV1-17(0)-13",
			"PV1-17(0)-14-1", "PV1-17(0)-14-2", "PV1-17(0)-14-3", "PV1-17(0)-15",
			"PV1-18",
			"PV1-19-1", "PV1-19-2", "PV1-19-3", "PV1-19-4-1", "PV1-19-4-2",
			"PV1-19-4-3", "PV1-19-5", "PV1-19-6-1", "PV1-19-6-2", "PV1-19-6-3",
			"PV1-20(0)-1", "PV1-20(0)-2",
			"PV1-21",
			"PV1-22",
			"PV1-23",
			"PV1-24(0)",
			"PV1-25(0)",
			"PV1-26(0)",
			"PV1-27(0)",
			"PV1-28",
			"PV1-29",
			"PV1-30",
			"PV1-31",
			"PV1-32",
			"PV1-33",
			"PV1-34",
			"PV1-35",
			"PV1-36",
			"PV1-37-1", "PV1-37-2",
			"PV1-38-1", "PV1-38-2", "PV1-38-3", "PV1-38-4", "PV1-38-5",
			"PV1-38-6",
			"PV1-39",
			"PV1-40",
			"PV1-41",
			"PV1-42-1", "PV1-42-2", "PV1-42-3", "PV1-42-4-1", "PV1-42-4-2",
			"PV1-42-4-3", "PV1-42-5", "PV1-42-6", "PV1-42-7", "PV1-42-8",
			"PV1-42-9",
			"PV1-43-1", "PV1-43-2", "PV1-43-3", "PV1-43-4-1", "PV1-43-4-2",
			"PV1-43-4-3", "PV1-43-5", "PV1-43-6", "PV1-43-7", "PV1-43-8",
			"PV1-43-9",
			"PV1-44",
			"PV1-45",
			"PV1-46",
			"PV1-47",
			"PV1-48",
			"PV1-49",
			"PV1-50-1", "PV1-50-2", "PV1-50-3", "PV1-50-4-1", "PV1-50-4-2",
			"PV1-50-4-3", "PV1-50-5", "PV1-50-6-1", "PV1-50-6-2", "PV1-50-6-3",
			"PV1-51",
			"PV1-52(0)-1", "PV1-52(0)-2-1", "PV1-52(0)-2-2", "PV1-52(0)-3",
			"PV1-52(0)-4", "PV1-52(0)-5", "PV1-52(0)-6", "PV1-52(0)-7",
			"PV1-52(0)-8", "PV1-52(0)-9-1", "PV1-52(0)-9-2", "PV1-52(0)-9-3",
			"PV1-52(0)-10", "PV1-52(0)-11", "PV1-52(0)-12", "PV1-52(0)-13",
			"PV1-52(0)-14-1", "PV1-52(0)-14-2", "PV1-52(0)-14-3", "PV1-52(0)-15"				
	};
	
	
	
	private static Parser parser;
	
	private static final Logger log = Logger.getLogger(A17FileTransformer.class);	
	
	static{
		parser = new PipeParser();
		parser.setValidationContext(new NoValidation());
	}
	
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


	protected void copyMSH(Terser src, Terser dest) throws DataTypeException, HL7Exception{
		
		String[] mshSpec = {"MSH-1", "MSH-2", "MSH-3-1", "MSH-3-2", "MSH-3-3",
				"MSH-4-1", "MSH-4-2", "MSH-4-3", "MSH-5-1", "MSH-5-2", "MSH-5-3",
				"MSH-6-1", "MSH-6-2", "MSH-6-3", "MSH-7-1", "MSH-7-2", "MSH-8",
				"MSH-9-1", "MSH-9-2", "MSH-9-3", "MSH-10", "MSH-11-1", "MSH-11-2",
				"MSH-12-1", "MSH-12-2-1", "MSH-12-2-2", "MSH-12-2-3", "MSH-12-2-4",
				"MSH-12-2-5", "MSH-12-2-6", "MSH-12-3-1", "MSH-12-3-2", "MSH-12-3-3", 
				"MSH-12-3-4", "MSH-12-3-5", "MSH-12-3-6", "MSH-13", "MSH-14", "MSH-15",
				"MSH-16", "MSH-17", "MSH-18", "MSH-19-1", "MSH-19-2", "MSH-19-3", 
				"MSH-19-4", "MSH-19-5", "MSH-19-6","MSH-20"};
		
		
		for(String spec : mshSpec){
			
			if(spec.equals("MSH-9-1")){
				dest.set(spec, "ADT");
			}else if(spec.equals("MSH-9-2")){
				dest.set(spec, "A02");
			}else{			
				dest.set(spec, src.get(spec));
			}
			
		}		
		
	}
	
	protected void copyEVN(Terser src, Terser dest) throws HL7Exception{

		String[] evnSpec = {"EVN-1", "EVN-2-1", "EVN-2-2", "EVN-3-1", "EVN-3-2",
				"EVN-4", "EVN-5-1", "EVN-5-2-1", "EVN-5-2-2", "EVN-5-3", "EVN-5-4", 
				"EVN-5-5", "EVN-5-6", "EVN-5-7", "EVN-5-8", "EVN-5-9-1", "EVN-5-9-2",
				"EVN-5-9-3", "EVN-5-10", "EVN-5-11", "EVN-5-12", "EVN-5-13", 
				"EVN-5-14-1", "EVN-5-14-2", "EVN-5-14-3", "EVN-5-15", "EVN-6"};
		
		for(String spec : evnSpec){
			
			if(spec.equals("EVN-1")){
				dest.set(spec, "A02");
			}else{
				dest.set(spec, src.get(spec));
			}	
			
		}
		
	}
	
	protected abstract void copyPID(Terser src, Terser dest) throws HL7Exception;
	
	protected abstract void copyPV1(Terser src, Terser dest) throws HL7Exception;
	
	/**
	 * This is necessary because the ADT_A17 we received don't have standard behavior.
	 * This is a shoddy piece of work :(.
	 * NOTE: con los mensajes de DAE no es necesario este método
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
