package com.idra.gestionpeluqueria.exception;
/**
 * Excepcion personalizada para errores en la capa de servicios.
 * Se lanza cuando ocurren problemas durante la ejecucion de la logica
 * de negocio o validaciones en la capa de servicio.
 * 
 * @author Idra
 */
public class ServiceException extends Exception {
    
    /**
     * Constructor con mensaje descriptivo del error.
     * 
     * @param message Mensaje que describe el error ocurrido
     */
    public ServiceException(String message) {
        super(message);
    }
    /**
     * Constructor con mensaje y causa raiz de la excepcion 
     * 
     * @param message Mensaje que describe el error ocurrido 
     * @param cause Excepcion original que causo este error 
     */
    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }
    /**
     * Constructor que recibe solo la causa raiz de la excepcion.
     * @param cause 
     */
    public ServiceException(Throwable cause) {
        super(cause);
    }
}