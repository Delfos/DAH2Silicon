package org.delfos.mirth.utils;

import java.util.Collection;
import java.util.NoSuchElementException;

/**
 * La interfaz <code>MessagesController</code> se utiliza para controlar la cola de mensajes procesadas por
 * canales de Mirth.
 * 
 * @author alopezg
 */
public interface MessagesController {

	/**
	 * Inicializa los valores del controlador.
	 */
	public void reset();
	
	/**
	 * Indica si hay errores en los mensajes procesados.
	 * 
	 * @return true si se han procesado mensajes con error.
	 */
	public boolean hasErrors();
	
	/**
	 * Indica al controlador que se va a procesar un nuevo mensaje.
	 * 
	 * @return número del mensaje que se va a procesar
	 * @throws java.util.NoSuchElementException cuando no quedan mensajes que procesar
	 */
	public int next() throws NoSuchElementException;
	
	/**
	 * Añade un mensaje con error al controlador.
	 * 
	 * @param errorMsg número del mensaje que tiene el error
	 */
	public void addError(int errorMsg);
	
	/**
	 * Obtiene los errores de los mensajes
	 * 
	 * @return números de los mensajes con error 
	 */
	public Collection<Integer> getErrors();
	
	/**
	 * El controlador prepara los mensajes para que sean procesados por el canal.
	 */
	public void processMessages() throws MessageControllerException;
	
}
