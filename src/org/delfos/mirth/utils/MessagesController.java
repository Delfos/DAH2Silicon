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
	 * @return n�mero del mensaje que se va a procesar
	 * @throws java.util.NoSuchElementException cuando no quedan mensajes que procesar
	 */
	public int next() throws NoSuchElementException;
	
	/**
	 * Devuelve el índice del mensaje que se está procesando.
	 * @return
	 */
	public int getCounter();
	
	/**
	 * A�ade un mensaje con error al controlador.
	 * 
	 * @param errorMsg n�mero del mensaje que tiene el error
	 */
	public void addError(int errorMsg);
	
	/**
	 * Obtiene los errores de los mensajes
	 * 
	 * @return n�meros de los mensajes con error 
	 */
	public Collection<Integer> getErrors();
	
	/**
	 * Indica si el mensaje tiene que ser filtrado por el canal.
	 * 
	 * @return true si el mensaje tiene que ser filtrado por el canal.
	 */
	public boolean isFilter();
	
	/**
	 * Establece el filtrado o no del mensaje.
	 * 
	 * @param filter true el mensaje tiene que ser filtrado
	 */
	public void setFilter(boolean filter);
	
	/**
	 * El controlador prepara los mensajes para que sean procesados por el canal.
	 */
	public void processMessages() throws MessageControllerException;
	
}
