package com.idra.gestionpeluqueria.model.enums;
/**
 * Enumeracion que representa los posibles estados de pago de un turno.
 * Define si un turno esta pagado completamente, pediente o abonado parcialmente.
 * 
 * @author Idra
 */
public enum EstadoPago {
    PAGADO("Pagado"),
    PENDIENTE("Pendiente de pago"),
    ABONADO_PARCIAL("Abonado parcialmente");
    
    private final String descripcion;
    
    private EstadoPago(String descripcion) {
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