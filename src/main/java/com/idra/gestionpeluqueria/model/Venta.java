package com.idra.gestionpeluqueria.model;

import java.time.LocalDateTime;

/**
 * Clase que representa una venta en el e-commerce.
 * Relaciona a un cliente con un producto, incluyendo cantidad, precio,
 * método de pago y total de la venta para análisis estadístico.
 * 
 * @author Idra
 */
public class Venta {
    private int id;
    private LocalDateTime fecha;
    private Cliente cliente;
    private Producto producto;
    private int cantidad;
    private double precioUnitario;
    private int idMetodoPago;
    private double total;
    
    /**
     * Constructor por defecto sin parametros 
     */
    public Venta() {}
    
    /**
     * Constructor con parametros principales de la venta.
     * Calcula automáticamente el total y establece la fecha actual.
     * 
     * @param cliente Cliente que realiza la compra
     * @param producto Producto vendido
     * @param cantidad Cantidad del producto vendida
     * @param precioUnitario Precio unitario al momento de la venta
     * @param idMetodoPago ID del método de pago utilizado
     */
    public Venta(Cliente cliente, Producto producto, int cantidad, double precioUnitario, int idMetodoPago) {
        this.cliente = cliente;
        this.producto = producto;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.idMetodoPago = idMetodoPago;
        this.total = cantidad * precioUnitario;
        this.fecha = LocalDateTime.now();
    }
    
    /**
     * Constructor simplificado para registro rápido de ventas.
     * 
     * @param idCliente ID del cliente
     * @param idProducto ID del producto  
     * @param cantidad Cantidad vendida
     * @param precioUnitario Precio unitario
     * @param idMetodoPago ID del método de pago
     */
    public Venta(int idCliente, int idProducto, int cantidad, double precioUnitario, int idMetodoPago) {
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.idMetodoPago = idMetodoPago;
        this.total = cantidad * precioUnitario;
        this.fecha = LocalDateTime.now();
        
        // Crear objetos básicos (en una implementación real se cargarían desde BD)
        this.cliente = new Cliente();
        this.cliente.setId(idCliente);
        
        this.producto = new Producto();
        this.producto.setId(idProducto);
    }
    
    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }
    
    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }
    
    public Producto getProducto() { return producto; }
    public void setProducto(Producto producto) { this.producto = producto; }
    
    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { 
        this.cantidad = cantidad;
        calcularTotal(); // Recalcular total cuando cambia la cantidad
    }
    
    public double getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(double precioUnitario) { 
        this.precioUnitario = precioUnitario;
        calcularTotal(); // Recalcular total cuando cambia el precio
    }
    
    public int getIdMetodoPago() { return idMetodoPago; }
    public void setIdMetodoPago(int idMetodoPago) { this.idMetodoPago = idMetodoPago; }
    
    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }
    
    /**
     * Calcula el total de la venta basado en cantidad y precio unitario.
     */
    
    
    /**
     * Obtiene el día de la semana de la venta (para análisis estadístico).
     * 
     * @return Nombre del día de la semana (Lunes, Martes, etc.)
     */
    public String getDiaSemana() {
        return fecha.getDayOfWeek().toString();
    }
    
    /**
     * Obtiene el mes de la venta (para análisis estadístico).
     * 
     * @return Número del mes (1-12)
     */
    public int getMes() {
        return fecha.getMonthValue();
    }
    
    /**
     * Obtiene el año de la venta (para análisis estadístico).
     * 
     * @return Año de la venta
     */
    public int getAnio() {
        return fecha.getYear();
    }
    
    /**
     * Retorna una representacion en texto de la venta.
     * 
     * @return String con cliente, producto, cantidad y total
     */
    @Override
    public String toString() {
        return "Venta #" + id + " - " + cliente.getNombre() + " " + cliente.getApellido() + 
               " - " + producto.getNombre() + " x" + cantidad + " - $" + total;
    }
    
    /**
     * Método auxiliar para recalcular el total
     */
    private void calcularTotal() {
        this.total = this.cantidad * this.precioUnitario;
    }
}