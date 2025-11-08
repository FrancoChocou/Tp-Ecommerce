package com.idra.gestionpeluqueria.dao;

import com.idra.gestionpeluqueria.model.Venta;
import com.idra.gestionpeluqueria.exception.DAOException;
import java.time.LocalDate;
import java.util.List;

/**
 * Interfaz que define las operaciones de acceso a datos para la entidad Venta.
 * Proporciona metodos para gestionar las ventas del e-commerce, incluyendo
 * operaciones CRUD, consultas estadísticas y análisis de datos.
 * 
 * @author Idra
 */
public interface VentaDAO {
    /**
     * Crea una nueva venta en la base de datos.
     * 
     * @param venta La venta a crear 
     * @throws DAOException Si ocurre un error al crear la venta 
     */
    void crear(Venta venta) throws DAOException;
    
    /**
     * Busca una venta por su identificador unico
     * 
     * @param id El ID de la venta a buscar 
     * @return La venta encontrada o null si no existe 
     * @throws DAOException Si ocurre un error al buscar la venta 
     */
    Venta buscarPorId(int id) throws DAOException;
    
    /**
     * Obtiene todas las ventas registradas en el sistema.
     * 
     * @return Lista de todas las ventas 
     * @throws DAOException Si ocurre un error al obtener las ventas 
     */
    List<Venta> buscarTodos() throws DAOException;
    
    /**
     * Busca todas las ventas realizadas en una fecha especifica 
     * 
     * @param fecha La fecha para buscar ventas 
     * @return Lista de ventas en la fecha especificada 
     * @throws DAOException Si ocurre un error al buscar las ventas 
     */
    List<Venta> buscarPorFecha(LocalDate fecha) throws DAOException;
    
    /**
     * Busca todas las ventas asociadas a un cliente especifico  
     * 
     * @param clienteId El ID del cliente 
     * @return Lista de ventas del cliente 
     * @throws DAOException Si ocurre un error al buscar las ventas 
     */
    List<Venta> buscarPorCliente(int clienteId) throws DAOException;
    
    /**
     * Busca todas las ventas de un producto especifico
     * 
     * @param productoId El ID del producto 
     * @return Lista de ventas del producto 
     * @throws DAOException Si ocurre un error al buscar las ventas 
     */
    List<Venta> buscarPorProducto(int productoId) throws DAOException;
    
    /**
     * Busca ventas realizadas con un método de pago específico
     * 
     * @param metodoPagoId El ID del método de pago 
     * @return Lista de ventas con el método de pago especificado 
     * @throws DAOException Si ocurre un error al buscar las ventas 
     */
    List<Venta> buscarPorMetodoPago(int metodoPagoId) throws DAOException;
    
    /**
     * Obtiene el total de ventas por día para un período específico
     * 
     * @param fechaInicio Fecha de inicio del período
     * @param fechaFin Fecha de fin del período
     * @return Lista de objetos con fecha y total vendido por día
     * @throws DAOException Si ocurre un error al obtener los totales
     */
    List<Object[]> obtenerTotalVentasPorDia(LocalDate fechaInicio, LocalDate fechaFin) throws DAOException;
    
    /**
     * Obtiene las ventas por categoría de producto
     * 
     * @return Lista de objetos con categoría y total vendido
     * @throws DAOException Si ocurre un error al obtener los datos
     */
    List<Object[]> obtenerVentasPorCategoria() throws DAOException;
    
    /**
     * Obtiene los productos más vendidos
     * 
     * @param limite Cantidad máxima de productos a retornar
     * @return Lista de objetos con producto y cantidad vendida
     * @throws DAOException Si ocurre un error al obtener los datos
     */
    List<Object[]> obtenerProductosMasVendidos(int limite) throws DAOException;
    
    /**
     * Obtiene estadísticas de ventas por método de pago
     * 
     * @return Lista de objetos con método de pago y monto total
     * @throws DAOException Si ocurre un error al obtener los datos
     */
    List<Object[]> obtenerVentasPorMetodoPago() throws DAOException;
    
    /**
     * Calcula el promedio de ventas diarias para un período
     * 
     * @param fechaInicio Fecha de inicio
     * @param fechaFin Fecha de fin
     * @return Promedio de ventas por día
     * @throws DAOException Si ocurre un error al calcular el promedio
     */
    double calcularPromedioVentasDiarias(LocalDate fechaInicio, LocalDate fechaFin) throws DAOException;
    
    /**
     * Calcula la desviación estándar de los montos de venta para un período
     * 
     * @param fechaInicio Fecha de inicio
     * @param fechaFin Fecha de fin
     * @return Desviación estándar de los montos de venta
     * @throws DAOException Si ocurre un error al calcular la desviación
     */
    double calcularDesviacionEstandarVentas(LocalDate fechaInicio, LocalDate fechaFin) throws DAOException;
    
    /**
     * Actualiza los datos de una venta existente 
     * 
     * @param venta La venta con los datos actualizados 
     * @throws DAOException Si ocurre un error al actualizar la venta 
     */
    void actualizar(Venta venta) throws DAOException;
    
    /**
     * Elimina una venta de la base de datos.
     * 
     * @param id El ID de la venta a eliminar 
     * @throws DAOException Si ocurre un error al eliminar la venta 
     */
    void eliminar(int id) throws DAOException;
}