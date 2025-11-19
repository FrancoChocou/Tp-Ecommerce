package com.idra.gestionpeluqueria.model;

public class Zona {
    private int id;
    private String ciudad;
    private String provincia;
    
    // Constructores
    public Zona() {}
    
    public Zona(String ciudad, String provincia) {
        this.ciudad = ciudad;
        this.provincia = provincia;
    }
    
    public Zona(int id, String ciudad, String provincia) {
        this.id = id;
        this.ciudad = ciudad;
        this.provincia = provincia;
    }
    
    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getCiudad() { return ciudad; }
    public void setCiudad(String ciudad) { this.ciudad = ciudad; }
    
    public String getProvincia() { return provincia; }
    public void setProvincia(String provincia) { this.provincia = provincia; }
    
    @Override
    public String toString() {
        return ciudad + ", " + provincia;
    }
}