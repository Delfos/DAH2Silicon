package org.delfos.mirth.utils;

/**
 * Message Controller for Hl7 messages.
 * 
 * @author alopezg
 *
 */
public interface HL7MessagesController extends MessagesController {

	/**
	 * True is the HL7 message type is valid.
	 * 
	 * @param type Hl7 message type.
	 * @return
	 */
	public boolean isValidType(String type);
	
}
