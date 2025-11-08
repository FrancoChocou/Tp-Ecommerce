package com.idra.gestionpeluqueria.exception;

/**
 * Excepcion personalizada para errores en la capa de datos (DAO).
 * Se lanza cuando ocurren problemas durante las operaciones de persistencia
 * con la base de datos 
 * 
 * @author Idra
 */
public class DAOException extends Exception {
    
    /**
     * Constructor con mensaje descriptivo del error 
     * 
     * @param message Mensaje que describe el error ocurrido 
     */
    public DAOException(String message) {
        super(message);
    }
    
    /**
     * Constructor con mensaje y causa raiz de la excepcion 
     * 
     * @param message Mensaje que describe el error ocurrido 
     * @param cause Excepcion original que causo este error 
     */
    public DAOException(String message, Throwable cause) {
        super(message, cause);
    }
    
    /**
     * Constructor que recibe solo la causa raiz de la excepcion 
     * 
     * @param cause Excepcion original que causo este error 
     */
    public DAOException(Throwable cause) {
        super(cause);
    }
}