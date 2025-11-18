package com.idra.gestionpeluqueria.service;

import com.idra.gestionpeluqueria.model.Venta;
import com.idra.gestionpeluqueria.exception.ServiceException;
import java.time.LocalDate;
import java.util.List;

/**
 * Interfaz que define los servicios de negocio para la gestion de ventas.
 * Proporciona metodos para realizar operaciones CRUD, validaciones,
 * consultas y analisis estadisticos de las ventas del e-commerce.
 * 
 * @author Idra
 */
public interface VentaService {
    /**
     * Crea una nueva venta en el sistema y actualiza el stock del producto.
     * 
     * @param venta La venta a crear
     * @throws ServiceException Si ocurre un error al crear la venta o no hay stock
     */
    void crearVenta(Venta venta) throws ServiceException;
    
    /**
     * Busca una venta por su identificador único.
     * 
     * @param id El ID de la venta a buscar
     * @return La venta encontrada o null si no existe
     * @throws ServiceException Si ocurre un error al buscar la venta
     */
    Venta buscarVentaPorId(int id) throws ServiceException;
    
    /**
     * Obtiene todas las ventas registradas en el sistema.
     * 
     * @return Lista de todas las ventas
     * @throws ServiceException Si ocurre un error al obtener las ventas
     */
    List<Venta> buscarTodasVentas() throws ServiceException;
    
    /**
     * Busca todas las ventas realizadas en una fecha específica.
     * 
     * @param fecha La fecha para buscar ventas
     * @return Lista de ventas en la fecha especificada
     * @throws ServiceException Si ocurre un error al buscar las ventas
     */
    List<Venta> buscarVentasPorFecha(LocalDate fecha) throws ServiceException;
    
    /**
     * Busca todas las ventas asociadas a un cliente específico.
     * 
     * @param clienteId El ID del cliente
     * @return Lista de ventas del cliente
     * @throws ServiceException Si ocurre un error al buscar las ventas
     */
    List<Venta> buscarVentasPorCliente(int clienteId) throws ServiceException;
    
    /**
     * Busca todas las ventas de un producto específico.
     * 
     * @param productoId El ID del producto
     * @return Lista de ventas del producto
     * @throws ServiceException Si ocurre un error al buscar las ventas
     */
    List<Venta> buscarVentasPorProducto(int productoId) throws ServiceException;
    
    /**
     * Busca ventas realizadas con un método de pago específico.
     * 
     * @param metodoPagoId El ID del método de pago
     * @return Lista de ventas con el método de pago especificado
     * @throws ServiceException Si ocurre un error al buscar las ventas
     */
    List<Venta> buscarVentasPorMetodoPago(int metodoPagoId) throws ServiceException;
    
    /**
     * Actualiza la información de una venta existente.
     * 
     * @param venta La venta con la información actualizada
     * @throws ServiceException Si ocurre un error al actualizar la venta
     */
    void actualizarVenta(Venta venta) throws ServiceException;
    
    /**
     * Elimina una venta del sistema.
     * 
     * @param id El ID de la venta a eliminar
     * @throws ServiceException Si ocurre un error al eliminar la venta
     */
    void eliminarVenta(int id) throws ServiceException;
    
    /**
     * Valida que los datos de una venta cumplan con las reglas de negocio.
     * 
     * @param venta La venta a validar
     * @return true si los datos son válidos, false en caso contrario
     * @throws ServiceException Si ocurre un error durante la validación
     */
    boolean validarVenta(Venta venta) throws ServiceException;
    
    //Metodos de analisis estadistico 
    
    /**
     * Calcula el promedio de ventas diarias para un período específico.
     * REQUERIDO EN TP: Media/promedio de ventas por día
     * 
     * @param fechaInicio Fecha de inicio del período
     * @param fechaFin Fecha de fin del período
     * @return Promedio de ventas por día
     * @throws ServiceException Si ocurre un error al calcular el promedio
     */
    double calcularPromedioVentasDiarias(LocalDate fechaInicio, LocalDate fechaFin) throws ServiceException;
    
    /**
     * Calcula la desviación estándar de los montos de venta para un período.
     * REQUERIDO EN TP: Desvío estándar de las ventas
     * 
     * @param fechaInicio Fecha de inicio del período
     * @param fechaFin Fecha de fin del período
     * @return Desviación estándar de los montos de venta
     * @throws ServiceException Si ocurre un error al calcular la desviación
     */
    double calcularDesviacionEstandarVentas(LocalDate fechaInicio, LocalDate fechaFin) throws ServiceException;
    
    /**
     * Obtiene el total de ventas por día para un período específico.
     * 
     * @param fechaInicio Fecha de inicio del período
     * @param fechaFin Fecha de fin del período
     * @return Lista de objetos con fecha y total vendido por día
     * @throws ServiceException Si ocurre un error al obtener los totales
     */
    List<Object[]> obtenerTotalVentasPorDia(LocalDate fechaInicio, LocalDate fechaFin) throws ServiceException;
    
    /**
     * Obtiene las ventas agrupadas por categoría de producto.
     * 
     * @return Lista de objetos con categoría y total vendido
     * @throws ServiceException Si ocurre un error al obtener los datos
     */
    List<Object[]> obtenerVentasPorCategoria() throws ServiceException;
    
    /**
     * Obtiene los productos más vendidos.
     * 
     * @param limite Cantidad máxima de productos a retornar
     * @return Lista de objetos con producto y cantidad vendida
     * @throws ServiceException Si ocurre un error al obtener los datos
     */
    List<Object[]> obtenerProductosMasVendidos(int limite) throws ServiceException;
    
    /**
     * Obtiene estadísticas de ventas por método de pago.
     * REQUERIDO EN TP: Correlación entre monto total y método de pago
     * 
     * @return Lista de objetos con método de pago y monto total
     * @throws ServiceException Si ocurre un error al obtener los datos
     */
    List<Object[]> obtenerVentasPorMetodoPago() throws ServiceException;
    
    /**
     * Calcula el coeficiente de correlación de Pearson entre precio y cantidad vendida.
     * REQUERIDO EN TP: Correlación entre variables (precio vs cantidad)
     * 
     * @return Coeficiente de correlación de Pearson
     * @throws ServiceException Si ocurre un error al calcular la correlación
     */
    double calcularCorrelacionPrecioCantidad() throws ServiceException;
    
    /**
     * Calcula el coeficiente de correlación de Pearson entre día de semana y cantidad vendida.
     * REQUERIDO EN TP: Correlación entre variables (día vs cantidad)
     * 
     * @return Coeficiente de correlación de Pearson
     * @throws ServiceException Si ocurre un error al calcular la correlación
     */
    double calcularCorrelacionDiaCantidad() throws ServiceException;
    
    /**
     * Obtiene el total de ventas del día actual.
     * 
     * @return Total de ventas del día de hoy
     * @throws ServiceException Si ocurre un error al calcular el total
     */
    double calcularTotalVentasHoy() throws ServiceException;
    
    /**
     * Obtiene el total de ventas del mes actual.
     * 
     * @return Total de ventas del mes actual
     * @throws ServiceException Si ocurre un error al calcular el total
     */
    double calcularTotalVentasMes() throws ServiceException;
}