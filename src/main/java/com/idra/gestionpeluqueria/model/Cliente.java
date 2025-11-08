package com.idra.gestionpeluqueria.model;

import java.time.LocalDate;

/**
 * Clase que representa a un cliente del e-commerce.
 * Contiene la informacion personal, de contacto y ubicacion del cliente.
 * @author Idra
 */
public class Cliente {
    private int id;
    private String nombre;
    private String apellido;
    private String telefono;
    private String email;
    private int edad;
    private int idZona;
    private LocalDate fechaRegistro;
    private boolean activo;
    
    /**
     * Constructor por defecto sin parametros.
     */
    public Cliente() {}
    
    /**
     * Constructor con parametros principales del cliente.
     * Inicializa la fecha de registro con la fecha actual y activo como true.
     */
    public Cliente(String nombre, String apellido, String telefono, String email, int edad, int idZona) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.telefono = telefono;
        this.email = email;
        this.edad = edad;
        this.idZona = idZona;
        this.fechaRegistro = LocalDate.now();
        this.activo = true;
    }
    
    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }
    
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public int getEdad() { return edad; }
    public void setEdad(int edad) { this.edad = edad; }
    
    public int getIdZona() { return idZona; }
    public void setIdZona(int idZona) { this.idZona = idZona; }
    
    public LocalDate getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(LocalDate fechaRegistro) { this.fechaRegistro = fechaRegistro; }
    
    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
    
    /**
     * Retorna una representacion en texto del cliente 
     */
    @Override
    public String toString() {
        return nombre + " " + apellido + " - " + email + " (Zona: " + idZona + ")";
    }
} 