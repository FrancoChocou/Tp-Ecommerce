package com.idra.gestionpeluqueria.model.enums;
/**
 * Enumeracion que representa los tipos de servicios ofrecidos por la peluqueria.
 * Clasifica los diferentes servicios que se pueden ofrecer a los clientes.
 * 
 * @author Franco
 */
public enum TipoServicio {
    CORTE("Corte de pelo"),
    TINTURA("Tintura"),
    PEINADO("Peinado"),
    ALISADO("Alisado"),
    MECHAS("Mechas"),
    BARBA("Arreglo de barba"),
    CEJAS("Arreglo de cejas"),
    TRATAMIENTO("Tratamiento capilar"),
    OTRO("Otro servicio");
    
    private final String descripcion;
    
    private TipoServicio(String descripcion) {
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