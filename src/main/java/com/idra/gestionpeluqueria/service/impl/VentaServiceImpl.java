package com.idra.gestionpeluqueria.service.impl;

import com.idra.gestionpeluqueria.model.Venta;
import com.idra.gestionpeluqueria.model.Producto;
import com.idra.gestionpeluqueria.exception.ServiceException;
import com.idra.gestionpeluqueria.exception.DAOException;
import java.time.LocalDate;
import java.util.List;
import com.idra.gestionpeluqueria.dao.VentaDAO;
import com.idra.gestionpeluqueria.service.VentaService;
import com.idra.gestionpeluqueria.service.ProductoService;
import com.idra.gestionpeluqueria.service.impl.ProductoServiceImpl;
import com.idra.gestionpeluqueria.dao.impl.ProductoDAOImpl;

/**
 * Implementacion de la interfaz VentaService.
 * Gestiona la logica de negocio relacionada con ventas, incluyendo
 * validaciones, control de stock y calculos estadisticos.
 * 
 * @author Franco
 */
public class VentaServiceImpl implements VentaService {
    
    private VentaDAO ventaDAO;
    private ProductoService productoService;
    
    /**
     * Constructor que inicializa el servicio con su DAO correspondiente.
     * 
     * @param ventaDAO El DAO para operaciones de persistencia de ventas
     */
    public VentaServiceImpl(VentaDAO ventaDAO) {
        this.ventaDAO = ventaDAO;
        this.productoService = new ProductoServiceImpl(new ProductoDAOImpl());
    }
    
    @Override
    public void crearVenta(Venta venta) throws ServiceException {
        try {
            if (!validarVenta(venta)) {
                throw new ServiceException("Datos de la venta no válidos");
            }
            
            // Verificar stock disponible
            Producto producto = productoService.buscarProductoPorId(venta.getProducto().getId());
            if (producto == null) {
                throw new ServiceException("Producto no encontrado");
            }
            
            if (!producto.tieneStock() || producto.getStock() < venta.getCantidad()) {
                throw new ServiceException("Stock insuficiente. Disponible: " + producto.getStock());
            }
            
            // Crear venta
            ventaDAO.crear(venta);
            
            // Actualizar stock
            productoService.reducirStockProducto(venta.getProducto().getId(), venta.getCantidad());
            
        } catch (DAOException | ServiceException e) {
            throw new ServiceException("Error al crear venta: " + e.getMessage(), e);
        }
    }
    
    @Override
    public Venta buscarVentaPorId(int id) throws ServiceException {
        try {
            return ventaDAO.buscarPorId(id);
        } catch (DAOException e) {
            throw new ServiceException("Error al buscar venta por ID: " + e.getMessage(), e);
        }
    }
    
    @Override
    public List<Venta> buscarTodasVentas() throws ServiceException {
        try {
            return ventaDAO.buscarTodos();
        } catch (DAOException e) {
            throw new ServiceException("Error al buscar todas las ventas: " + e.getMessage(), e);
        }
    }
    
    @Override
    public List<Venta> buscarVentasPorFecha(LocalDate fecha) throws ServiceException {
        try {
            return ventaDAO.buscarPorFecha(fecha);
        } catch (DAOException e) {
            throw new ServiceException("Error al buscar ventas por fecha: " + e.getMessage(), e);
        }
    }
    
    @Override
    public List<Venta> buscarVentasPorCliente(int clienteId) throws ServiceException {
        try {
            return ventaDAO.buscarPorCliente(clienteId);
        } catch (DAOException e) {
            throw new ServiceException("Error al buscar ventas por cliente: " + e.getMessage(), e);
        }
    }
    
    @Override
    public List<Venta> buscarVentasPorProducto(int productoId) throws ServiceException {
        try {
            return ventaDAO.buscarPorProducto(productoId);
        } catch (DAOException e) {
            throw new ServiceException("Error al buscar ventas por producto: " + e.getMessage(), e);
        }
    }
    
    @Override
    public List<Venta> buscarVentasPorMetodoPago(int metodoPagoId) throws ServiceException {
        try {
            return ventaDAO.buscarPorMetodoPago(metodoPagoId);
        } catch (DAOException e) {
            throw new ServiceException("Error al buscar ventas por método de pago: " + e.getMessage(), e);
        }
    }
    
    @Override
    public void actualizarVenta(Venta venta) throws ServiceException {
        try {
            if (!validarVenta(venta)) {
                throw new ServiceException("Datos de la venta no válidos");
            }
            ventaDAO.actualizar(venta);
        } catch (DAOException e) {
            throw new ServiceException("Error al actualizar venta: " + e.getMessage(), e);
        }
    }
    
    @Override
    public void eliminarVenta(int id) throws ServiceException {
        try {
            ventaDAO.eliminar(id);
        } catch (DAOException e) {
            throw new ServiceException("Error al eliminar venta: " + e.getMessage(), e);
        }
    }
    
    @Override
    public boolean validarVenta(Venta venta) throws ServiceException {
        if (venta == null) return false;
        if (venta.getCliente() == null || venta.getCliente().getId() <= 0) return false;
        if (venta.getProducto() == null || venta.getProducto().getId() <= 0) return false;
        if (venta.getCantidad() <= 0) return false;
        if (venta.getPrecioUnitario() <= 0) return false;
        if (venta.getIdMetodoPago() <= 0) return false;
        if (venta.getTotal() <= 0) return false;
        
        return true;
    }
    
    // ==================== IMPLEMENTACIÓN MÉTODOS ESTADÍSTICOS ====================
    
    @Override
    public double calcularPromedioVentasDiarias(LocalDate fechaInicio, LocalDate fechaFin) throws ServiceException {
        try {
            return ventaDAO.calcularPromedioVentasDiarias(fechaInicio, fechaFin);
        } catch (DAOException e) {
            throw new ServiceException("Error al calcular promedio de ventas diarias: " + e.getMessage(), e);
        }
    }
    
    @Override
    public double calcularDesviacionEstandarVentas(LocalDate fechaInicio, LocalDate fechaFin) throws ServiceException {
        try {
            return ventaDAO.calcularDesviacionEstandarVentas(fechaInicio, fechaFin);
        } catch (DAOException e) {
            throw new ServiceException("Error al calcular desviación estándar de ventas: " + e.getMessage(), e);
        }
    }
    
    @Override
    public List<Object[]> obtenerTotalVentasPorDia(LocalDate fechaInicio, LocalDate fechaFin) throws ServiceException {
        try {
            return ventaDAO.obtenerTotalVentasPorDia(fechaInicio, fechaFin);
        } catch (DAOException e) {
            throw new ServiceException("Error al obtener total de ventas por día: " + e.getMessage(), e);
        }
    }
    
    @Override
    public List<Object[]> obtenerVentasPorCategoria() throws ServiceException {
        try {
            return ventaDAO.obtenerVentasPorCategoria();
        } catch (DAOException e) {
            throw new ServiceException("Error al obtener ventas por categoría: " + e.getMessage(), e);
        }
    }
    
    @Override
    public List<Object[]> obtenerProductosMasVendidos(int limite) throws ServiceException {
        try {
            return ventaDAO.obtenerProductosMasVendidos(limite);
        } catch (DAOException e) {
            throw new ServiceException("Error al obtener productos más vendidos: " + e.getMessage(), e);
        }
    }
    
    @Override
    public List<Object[]> obtenerVentasPorMetodoPago() throws ServiceException {
        try {
            return ventaDAO.obtenerVentasPorMetodoPago();
        } catch (DAOException e) {
            throw new ServiceException("Error al obtener ventas por método de pago: " + e.getMessage(), e);
        }
    }
    
    @Override
    public double calcularCorrelacionPrecioCantidad() throws ServiceException {
        try {
            List<Venta> ventas = ventaDAO.buscarTodos();
            if (ventas.isEmpty()) return 0.0;
            
            // Calcular correlación de Pearson: precio vs cantidad
            double sumXY = 0, sumX = 0, sumY = 0, sumX2 = 0, sumY2 = 0;
            int n = ventas.size();
            
            for (Venta venta : ventas) {
                double x = venta.getPrecioUnitario(); // Precio
                double y = venta.getCantidad();       // Cantidad
                
                sumXY += x * y;
                sumX += x;
                sumY += y;
                sumX2 += x * x;
                sumY2 += y * y;
            }
            
            // Fórmula correlación de Pearson
            double numerador = n * sumXY - sumX * sumY;
            double denominador = Math.sqrt((n * sumX2 - sumX * sumX) * (n * sumY2 - sumY * sumY));
            
            return denominador != 0 ? numerador / denominador : 0.0;
            
        } catch (DAOException e) {
            throw new ServiceException("Error al calcular correlación precio-cantidad: " + e.getMessage(), e);
        }
    }
    
    @Override
    public double calcularCorrelacionDiaCantidad() throws ServiceException {
        try {
            List<Venta> ventas = ventaDAO.buscarTodos();
            if (ventas.isEmpty()) return 0.0;
            
            // Calcular correlación de Pearson: día de semana (1-7) vs cantidad
            double sumXY = 0, sumX = 0, sumY = 0, sumX2 = 0, sumY2 = 0;
            int n = ventas.size();
            
            for (Venta venta : ventas) {
                double x = venta.getFecha().getDayOfWeek().getValue(); // Día de semana (1=Lunes, 7=Domingo)
                double y = venta.getCantidad();                        // Cantidad
                
                sumXY += x * y;
                sumX += x;
                sumY += y;
                sumX2 += x * x;
                sumY2 += y * y;
            }
            
            // Fórmula correlación de Pearson
            double numerador = n * sumXY - sumX * sumY;
            double denominador = Math.sqrt((n * sumX2 - sumX * sumX) * (n * sumY2 - sumY * sumY));
            
            return denominador != 0 ? numerador / denominador : 0.0;
            
        } catch (DAOException e) {
            throw new ServiceException("Error al calcular correlación día-cantidad: " + e.getMessage(), e);
        }
    }
    
    @Override
    public double calcularTotalVentasHoy() throws ServiceException {
        try {
            List<Venta> ventasHoy = ventaDAO.buscarPorFecha(LocalDate.now());
            return ventasHoy.stream()
                .mapToDouble(Venta::getTotal)
                .sum();
        } catch (DAOException e) {
            throw new ServiceException("Error al calcular total de ventas hoy: " + e.getMessage(), e);
        }
    }
    
    @Override
    public double calcularTotalVentasMes() throws ServiceException {
        try {
            LocalDate inicioMes = LocalDate.now().withDayOfMonth(1);
            LocalDate finMes = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth());
            
            List<Venta> ventasMes = ventaDAO.buscarTodos().stream()
                .filter(venta -> !venta.getFecha().toLocalDate().isBefore(inicioMes) && 
                                !venta.getFecha().toLocalDate().isAfter(finMes))
                .toList();
            
            return ventasMes.stream()
                .mapToDouble(Venta::getTotal)
                .sum();
                
        } catch (DAOException e) {
            throw new ServiceException("Error al calcular total de ventas del mes: " + e.getMessage(), e);
        }
    }
}