package com.idra.gestionpeluqueria.exception;
/**
 * Excepcion personalizada con errores de validacion de datos.
 * Se lanza cuando los datos proporcionados no cumplen con las reglas 
 * de negocio o restricciones de validacion del sistema.
 * 
 * @author Idra
 */
public class ValidacionException extends Exception {
    /**
     * Constructor con mensaje descriptivo del error de validacion.
     * 
     * @param message Mensaje que describe el error de validacion ocurrido
     */
    public ValidacionException(String message) {
        super(message);
    }
    /**
     * Constructor con mensaje y causa raiz de la excepcion.
     * 
     * @param message Mensaje que describe el error de validacion ocurrido 
     * @param cause Excepcion original que causo este error 
     */
    public ValidacionException(String message, Throwable cause) {
        super(message, cause);
    }
}