package com.idra.gestionpeluqueria.controller;

import com.idra.gestionpeluqueria.model.Venta;
import com.idra.gestionpeluqueria.service.impl.VentaServiceImpl;
import com.idra.gestionpeluqueria.dao.impl.VentaDAOImpl;
import com.idra.gestionpeluqueria.exception.ServiceException;
import java.time.LocalDate;
import java.util.List;
import com.idra.gestionpeluqueria.service.VentaService;

/**
 * Controlador para la gestion de ventas del e-commerce.
 * Actua como intermediario entre la capa de presentacion y la capa de servicio,
 * coordinando las operaciones de creacion, consulta y analisis estadistico de ventas.
 * 
 * @author Idra
 */
public class VentaController {
    
    private VentaService ventaService;
    
    /**
     * Constructor que inicializa el controlador con sus dependencias.
     */
    public VentaController() {
        this.ventaService = new VentaServiceImpl(new VentaDAOImpl());
    }
    
    //Metodos CRUD basicos
    
    /**
     * Crea una nueva venta en el sistema
     * @param venta La venta a crear
     * @throws ServiceException Si ocurre un error al crear la venta
     */
    public void crearVenta(Venta venta) throws ServiceException {
        ventaService.crearVenta(venta);
    }
    
    /**
     * Busca una venta por su ID
     * @param id El ID de la venta a buscar
     * @return La venta encontrada o null si no existe
     * @throws ServiceException Si ocurre un error al buscar la venta
     */
    public Venta buscarVentaPorId(int id) throws ServiceException {
        return ventaService.buscarVentaPorId(id);
    }
    
    /**
     * Obtiene todas las ventas del sistema
     * @return Lista de todas las ventas
     * @throws ServiceException Si ocurre un error al obtener las ventas
     */
    public List<Venta> buscarTodasVentas() throws ServiceException {
        return ventaService.buscarTodasVentas();
    }
    
    /**
     * Busca ventas por fecha específica
     * @param fecha La fecha para buscar ventas
     * @return Lista de ventas en la fecha especificada
     * @throws ServiceException Si ocurre un error al buscar las ventas
     */
    public List<Venta> buscarVentasPorFecha(LocalDate fecha) throws ServiceException {
        return ventaService.buscarVentasPorFecha(fecha);
    }
    
    /**
     * Busca ventas por cliente
     * @param clienteId El ID del cliente
     * @return Lista de ventas del cliente
     * @throws ServiceException Si ocurre un error al buscar las ventas
     */
    public List<Venta> buscarVentasPorCliente(int clienteId) throws ServiceException {
        return ventaService.buscarVentasPorCliente(clienteId);
    }
    
    /**
     * Busca ventas por producto
     * @param productoId El ID del producto
     * @return Lista de ventas del producto
     * @throws ServiceException Si ocurre un error al buscar las ventas
     */
    public List<Venta> buscarVentasPorProducto(int productoId) throws ServiceException {
        return ventaService.buscarVentasPorProducto(productoId);
    }
    
    /**
     * Busca ventas por método de pago
     * @param metodoPagoId El ID del método de pago
     * @return Lista de ventas con el método de pago especificado
     * @throws ServiceException Si ocurre un error al buscar las ventas
     */
    public List<Venta> buscarVentasPorMetodoPago(int metodoPagoId) throws ServiceException {
        return ventaService.buscarVentasPorMetodoPago(metodoPagoId);
    }
    
    /**
     * Actualiza la información de una venta existente
     * @param venta La venta con la información actualizada
     * @throws ServiceException Si ocurre un error al actualizar la venta
     */
    public void actualizarVenta(Venta venta) throws ServiceException {
        ventaService.actualizarVenta(venta);
    }
    
    /**
     * Elimina una venta del sistema
     * @param id El ID de la venta a eliminar
     * @throws ServiceException Si ocurre un error al eliminar la venta
     */
    public void eliminarVenta(int id) throws ServiceException {
        ventaService.eliminarVenta(id);
    }
    
    /**
     * Valida los datos de una venta
     * @param venta La venta a validar
     * @return true si los datos son válidos, false en caso contrario
     * @throws ServiceException Si ocurre un error durante la validación
     */
    public boolean validarVenta(Venta venta) throws ServiceException {
        return ventaService.validarVenta(venta);
    }
    
    // Metodos de analisis estadistico 
    
    /**
     * Calcula el promedio de ventas diarias para un período específico
     * REQUERIDO EN TP: Media/promedio de ventas por día
     * 
     * @param fechaInicio Fecha de inicio del período
     * @param fechaFin Fecha de fin del período
     * @return Promedio de ventas por día
     * @throws ServiceException Si ocurre un error al calcular el promedio
     */
    public double calcularPromedioVentasDiarias(LocalDate fechaInicio, LocalDate fechaFin) throws ServiceException {
        return ventaService.calcularPromedioVentasDiarias(fechaInicio, fechaFin);
    }
    
    /**
     * Calcula la desviación estándar de los montos de venta para un período
     * REQUERIDO EN TP: Desvío estándar de las ventas
     * 
     * @param fechaInicio Fecha de inicio del período
     * @param fechaFin Fecha de fin del período
     * @return Desviación estándar de los montos de venta
     * @throws ServiceException Si ocurre un error al calcular la desviación
     */
    public double calcularDesviacionEstandarVentas(LocalDate fechaInicio, LocalDate fechaFin) throws ServiceException {
        return ventaService.calcularDesviacionEstandarVentas(fechaInicio, fechaFin);
    }
    
    /**
     * Obtiene el total de ventas por día para un período específico
     * 
     * @param fechaInicio Fecha de inicio del período
     * @param fechaFin Fecha de fin del período
     * @return Lista de objetos con fecha y total vendido por día
     * @throws ServiceException Si ocurre un error al obtener los totales
     */
    public List<Object[]> obtenerTotalVentasPorDia(LocalDate fechaInicio, LocalDate fechaFin) throws ServiceException {
        return ventaService.obtenerTotalVentasPorDia(fechaInicio, fechaFin);
    }
    
    /**
     * Obtiene las ventas agrupadas por categoría de producto
     * 
     * @return Lista de objetos con categoría y total vendido
     * @throws ServiceException Si ocurre un error al obtener los datos
     */
    public List<Object[]> obtenerVentasPorCategoria() throws ServiceException {
        return ventaService.obtenerVentasPorCategoria();
    }
    
    /**
     * Obtiene los productos más vendidos
     * 
     * @param limite Cantidad máxima de productos a retornar
     * @return Lista de objetos con producto y cantidad vendida
     * @throws ServiceException Si ocurre un error al obtener los datos
     */
    public List<Object[]> obtenerProductosMasVendidos(int limite) throws ServiceException {
        return ventaService.obtenerProductosMasVendidos(limite);
    }
    
    /**
     * Obtiene estadísticas de ventas por método de pago
     * REQUERIDO EN TP: Correlación entre monto total y método de pago
     * 
     * @return Lista de objetos con método de pago y monto total
     * @throws ServiceException Si ocurre un error al obtener los datos
     */
    public List<Object[]> obtenerVentasPorMetodoPago() throws ServiceException {
        return ventaService.obtenerVentasPorMetodoPago();
    }
    
    /**
     * Calcula el coeficiente de correlación de Pearson entre precio y cantidad vendida
     * REQUERIDO EN TP: Correlación entre variables (precio vs cantidad)
     * 
     * @return Coeficiente de correlación de Pearson
     * @throws ServiceException Si ocurre un error al calcular la correlación
     */
    public double calcularCorrelacionPrecioCantidad() throws ServiceException {
        return ventaService.calcularCorrelacionPrecioCantidad();
    }
    
    /**
     * Calcula el coeficiente de correlación de Pearson entre día de semana y cantidad vendida
     * REQUERIDO EN TP: Correlación entre variables (día vs cantidad)
     * 
     * @return Coeficiente de correlación de Pearson
     * @throws ServiceException Si ocurre un error al calcular la correlación
     */
    public double calcularCorrelacionDiaCantidad() throws ServiceException {
        return ventaService.calcularCorrelacionDiaCantidad();
    }
    
    /**
     * Obtiene el total de ventas del día actual
     * 
     * @return Total de ventas del día de hoy
     * @throws ServiceException Si ocurre un error al calcular el total
     */
    public double calcularTotalVentasHoy() throws ServiceException {
        return ventaService.calcularTotalVentasHoy();
    }
    
    /**
     * Obtiene el total de ventas del mes actual
     * 
     * @return Total de ventas del mes actual
     * @throws ServiceException Si ocurre un error al calcular el total
     */
    public double calcularTotalVentasMes() throws ServiceException {
        return ventaService.calcularTotalVentasMes();
    }
    
    /**
     * Método simplificado para crear venta rápida (para uso en interfaz)
     * 
     * @param idCliente ID del cliente
     * @param idProducto ID del producto
     * @param cantidad Cantidad vendida
     * @param precioUnitario Precio unitario
     * @param idMetodoPago ID del método de pago
     * @throws ServiceException Si ocurre un error al crear la venta
     */
    public void crearVentaRapida(int idCliente, int idProducto, int cantidad, double precioUnitario, int idMetodoPago) throws ServiceException {
        Venta venta = new Venta(idCliente, idProducto, cantidad, precioUnitario, idMetodoPago);
        ventaService.crearVenta(venta);
    }
}