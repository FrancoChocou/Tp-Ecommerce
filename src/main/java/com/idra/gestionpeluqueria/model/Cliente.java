package com.idra.gestionpeluqueria.model;

import java.time.LocalDate;

public class Cliente {
    private int id;
    private String nombre;
    private String apellido;
    private String email;
    private String telefono;
    private int edad;
    private boolean activo;
    private int idZona;
    private LocalDate fechaRegistro; // ✅ NUEVO CAMPO
    
    // Constructores
    public Cliente() {
        this.activo = true;
        this.edad = 0;
        this.fechaRegistro = LocalDate.now(); // ✅ Fecha actual por defecto
    }
    
    public Cliente(String nombre, String apellido, String email, String telefono) {
        this();
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.telefono = telefono;
    }
    
    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    
    public int getEdad() { return edad; }
    public void setEdad(int edad) { this.edad = edad; }
    
    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
    
    public int getIdZona() { return idZona; }
    public void setIdZona(int idZona) { this.idZona = idZona; }
    
    public LocalDate getFechaRegistro() { return fechaRegistro; } // ✅ NUEVO
    public void setFechaRegistro(LocalDate fechaRegistro) { this.fechaRegistro = fechaRegistro; } // ✅ NUEVO
    
    // Método útil para obtener nombre completo
    public String getNombreCompleto() {
        return nombre + " " + apellido;
    }
}