package com.idra.gestionpeluqueria.model.enums;
/**
 * Enumeracion que representa los posibles estados de un turno en la peluqueria.
 * Define el ciclo de vida de un turno desde su creacion hasta su finalizacion.
 * 
 * @author Idra
 */
public enum EstadoTurno {
    PENDIENTE("Pendiente"),
    CONFIRMADO("Confirmado"),
    COMPLETADO("Completado"),
    CANCELADO("Cancelado"),
    AUSENTE("Ausente");
    
    private final String descripcion;
    
    private EstadoTurno(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
    
    @Override
    public String toString() {
        return descripcion;
    }
}