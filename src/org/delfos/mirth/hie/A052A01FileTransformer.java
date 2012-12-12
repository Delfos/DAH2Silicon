package org.delfos.mirth.hie;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.delfos.io.AbstractFileTransformer;
import org.delfos.io.FileTransformException;
import org.delfos.mirth.hie.dao.DaoUtils;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.dao.DataAccessException;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.DataTypeException;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.v231.message.ADT_A01;
import ca.uhn.hl7v2.util.Terser;

public class A052A01FileTransformer extends AbstractFileTransformer {
	
	private static final Logger log = Logger.getLogger(A052A01FileTransformer.class);
	
	public void transform(InputStream fileSource,
			OutputStream fileResult) throws FileTransformException {
		
		try{
			
			String hl7Str = IOUtils.toString(fileSource);						
			Message hl7Msg = parser.parse(hl7Str);	
			
			log.info("Message parser correctly");			
			
			//Create the ADT_A01 message
			ADT_A01 adt_a01 = new ADT_A01();			

			Terser a05Terser = new Terser(hl7Msg);		
			Terser a01Terser = new Terser(adt_a01);			
			
			this.copyMSH(a05Terser, a01Terser);
			this.copyEVN(a05Terser, a01Terser);			
			this.copyPID(a05Terser, a01Terser);
			this.copyPV1(a05Terser, a01Terser);
			
			
			//Flush out to file
			String adt_a01_str = parser.encode(adt_a01);
			
			//Comprueba si existe el paciente
			try{
			
				if(DaoUtils.getPatientByNhc(a05Terser.get("PID-3-1")) != null){ //Existe el paciente				
					adt_a01_str = "El paciente existe en Silicon \n\n" + adt_a01_str; 				
					log.info("El paciente " + a05Terser.get("PID-3-1") + " existe en Silicon");				
				}
			}catch(Exception ex){
				log.warn("Error al verificar la existencia del paciente. No se ha podido conectar con la base de datos. \n" +
						ex.getMessage());
			}
			
			log.info("Created ADT_A01 message");
			log.debug("ADT_A01 message: \n" + adt_a01_str);			
			
			fileResult.write(adt_a01_str.getBytes());			
			
		
		}catch(Exception ex){			
			log.error(ex.getMessage(), ex);
			throw new FileTransformException(ex);
		}

	}	


	@Override
	protected void copyEVN(Terser src, Terser dest) throws HL7Exception {
		
		for(String spec : evnSpec){
			dest.set(spec, src.get(spec));
		}

	}

	@Override
	protected void copyMSH(Terser src, Terser dest) throws DataTypeException,
			HL7Exception {
		
		for(String spec : mshSpec){
			
			if(spec.equals("MSH-9-3")){
				dest.set(spec, "ADT_A01");
			}else if(spec.equals("MSH-9-2"))
				dest.set(spec, "A01");
			else{			
				dest.set(spec, src.get(spec));
			}
			
		}	

	}

	@Override
	protected void copyPID(Terser src, Terser dest) throws HL7Exception {
		
		for(String spec : pidSpec){
			dest.set(spec, src.get(spec));
		}

	}

	@Override
	protected void copyPV1(Terser src, Terser dest) throws HL7Exception {
		for(String spec : pv1Spec){
			dest.set(spec, src.get(spec));
		}
		
		dest.set("PV1-19-1", src.get("PID-3-1"));
		dest.set("PV1-3-3", "XXX");
		dest.set("PV1-10", "XXX");
	}
	
	public static void main(String[] args) throws Exception{
		
		BeanFactory beanFact = new FileSystemXmlApplicationContext("E:/Grifols/projects/java/mirth/mirth_utils/spring/spring_mirth_hie.xml");
		
		File hl7File = new File("e:/tmp/1450403663209_A05.ADM");
		File result = new File("e:/tmp/result.txt");		
		
		FileInputStream fis = new FileInputStream(hl7File);
		FileOutputStream fos = new FileOutputStream(result);		
		
		new A052A01FileTransformer().transform(fis, fos);		
		
	}


}
