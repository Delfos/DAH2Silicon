package org.delfos.mirth.hie;

import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.delfos.io.AbstractFileTransformer;
import org.delfos.io.FileTransformException;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.DataTypeException;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.v21.message.ADT_A03;
import ca.uhn.hl7v2.model.v231.message.ADT_A01;
import ca.uhn.hl7v2.model.v231.message.ADT_A02;
import ca.uhn.hl7v2.util.Terser;

public class A02FileTransformer extends AbstractFileTransformer {
	
	private static final Logger log = Logger.getLogger(A02FileTransformer.class);
	
	//Identificador DAE del Hospital Infanta Elena 
	private static final String HIE_ID = "10036";
	
	//Traslado dentro del mismo hospital
	private static final int SAME_HOSPITAL = 0;
	
	//Traslado del HIE a otro hospital
	private static final int FROM_HIE2OTHER = 1;
	
	//Traslado de otro hospital al HIE
	private static final int FROM_OTHER2HIE = 2;	

	@Override
	protected void copyEVN(Terser src, Terser dest) throws HL7Exception {}

	@Override
	protected void copyMSH(Terser src, Terser dest) throws DataTypeException,
			HL7Exception {}

	@Override
	protected void copyPID(Terser src, Terser dest) throws HL7Exception {}

	@Override
	protected void copyPV1(Terser src, Terser dest) throws HL7Exception {}

	public void transform(InputStream fileSource, OutputStream fileResult)
			throws FileTransformException {

		try{
			
			String hl7Str = IOUtils.toString(fileSource);
			log.debug("Mensaje HL7 ADT^A02: \n" + hl7Str);
			
			Message hl7Msg = parser.parse(hl7Str);
			
			log.info("Mensaje HL7 ADT^A02 parseado correctamente.");
			
			Terser a02Terser = new Terser(hl7Msg);
			
			int trsType = traslationType(a02Terser);
			
			if(trsType == SAME_HOSPITAL){ //El mesanje no se modifica
				log.info("Mensaje de traslado (ADT^A02) dentro del mismo hospital.");
			}else if(trsType == FROM_HIE2OTHER){
				log.info("Mensaje de traslado (ADT^A02) del HIE a otro hospital.");
				a02Terser.set("MSH-9-2", "A03");
				a02Terser.set("MSH-9-3", "ADT_A03");
				
				a02Terser.set("PV1-3-1", a02Terser.get("PV1-6-1"));
				a02Terser.set("PV1-3-2", a02Terser.get("PV1-6-2"));
				a02Terser.set("PV1-3-3", a02Terser.get("PV1-6-3"));
				a02Terser.set("PV1-3-4-1", a02Terser.get("PV1-6-4-1"));
				
			}else{
				log.info("Mensaje de traslado (ADT^A02) de otro hospital al HIE.");
				a02Terser.set("MSH-9-2", "A01");
				a02Terser.set("MSH-9-3", "ADT_A01");
			}
			
			String result;
			
			if(trsType == SAME_HOSPITAL)
				result = parser.encode(hl7Msg);
			else if(trsType == FROM_HIE2OTHER)
				result = parser.encode(hl7Msg);
			else 
				result = parser.encode(hl7Msg);				
			
			log.info("Nuevo mensaje creado.");
			log.debug("Nuevo mensaje: \n" + result);
			
			fileResult.write(result.getBytes());			
			
		}catch(Exception ex){			
			log.error(ex.getMessage(), ex);
			throw new FileTransformException(ex);			
		}
		

	}
	
	/**
	 * Devuelve el tipo de traslado: entre el mismo hospital, del HIE a otro hospital o de otro hospital al HIE.
	 * @param terser
	 * @return
	 * @throws HL7Exception
	 */
	private int traslationType(Terser terser) throws HL7Exception{
		
		String destHosp = terser.get("PV1-3-4-1");
		String orgHosp = terser.get("PV1-6-4-1");
		
		if(orgHosp.endsWith(destHosp))
			return SAME_HOSPITAL;
		else if(orgHosp.equals(HIE_ID))
			return FROM_HIE2OTHER;
		else
			return FROM_OTHER2HIE;	

	}

}
