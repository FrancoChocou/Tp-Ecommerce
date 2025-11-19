package com.idra.gestionpeluqueria.model;

import java.time.LocalDate;

public class Producto {
    private int id;
    private String nombre;
    private String descripcion;
    private int idCategoria;
    private double precioUnitario;
    private int stock;
    private boolean activo;
    private LocalDate fechaCreacion;
    
    // Constructores (mantener los mismos)
    public Producto() {
        this.fechaCreacion = LocalDate.now();
        this.activo = true;
    }
    
    public Producto(String nombre, String descripcion, int idCategoria, 
                   double precioUnitario, int stock) {
        this();
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.idCategoria = idCategoria;
        this.precioUnitario = precioUnitario;
        this.stock = stock;
    }
    
    // Getters y Setters (mantener los mismos)
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    
    public int getIdCategoria() { return idCategoria; }
    public void setIdCategoria(int idCategoria) { this.idCategoria = idCategoria; }
    
    public double getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(double precioUnitario) { this.precioUnitario = precioUnitario; }
    
    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }
    
    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
    
    public LocalDate getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDate fechaCreacion) { 
        this.fechaCreacion = fechaCreacion != null ? fechaCreacion : LocalDate.now();
    }
    
    public boolean tieneStock() {
        return this.stock > 0 && this.activo;
    }
    
    public boolean tieneStockSuficiente(int cantidad) {
        return this.stock >= cantidad && this.activo;
    }
    
    // ✅ MÉTODO TOString AGREGADO
    @Override
    public String toString() {
        return nombre;
    }
}