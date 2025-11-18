package com.idra.gestionpeluqueria.dao.impl;

import com.idra.gestionpeluqueria.model.Venta;
import com.idra.gestionpeluqueria.model.Cliente;
import com.idra.gestionpeluqueria.model.Producto;
import com.idra.gestionpeluqueria.config.DatabaseConfig;
import com.idra.gestionpeluqueria.exception.DAOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import com.idra.gestionpeluqueria.dao.VentaDAO;

/**
 * Implementacion de la interfaz VentaDAO para acceso a datos de ventas.
 * Gestiona todas las operaciones de persistencia relacionadas con ventas 
 * en la base de datos MySQL, incluyendo consultas estadísticas para análisis.
 * 
 * @author Idra
 */
public class VentaDAOImpl implements VentaDAO {
    
    private Connection getConnection() throws SQLException {
        return DatabaseConfig.getInstance().getConnection();
    }
    
    @Override
    public void crear(Venta venta) throws DAOException {
        String sql = "INSERT INTO ventas (fecha, id_cliente, id_producto, cantidad, precio_unitario, id_metodo_pago, total) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setTimestamp(1, Timestamp.valueOf(venta.getFecha()));
            stmt.setInt(2, venta.getCliente().getId());
            stmt.setInt(3, venta.getProducto().getId());
            stmt.setInt(4, venta.getCantidad());
            stmt.setDouble(5, venta.getPrecioUnitario());
            stmt.setInt(6, venta.getIdMetodoPago());
            stmt.setDouble(7, venta.getTotal());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows == 0) {
                throw new DAOException("Error al crear venta, ninguna fila afectada.");
            }
            
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    venta.setId(generatedKeys.getInt(1));
                } else {
                    throw new DAOException("Error al obtener ID generado para la venta.");
                }
            }
            
        } catch (SQLException e) {
            throw new DAOException("Error al crear venta en la base de datos", e);
        }
    }
    
    @Override
    public Venta buscarPorId(int id) throws DAOException {
        String sql = "SELECT v.*, c.nombre as cliente_nombre, c.apellido as cliente_apellido, c.email as cliente_email, " +
                    "p.nombre as producto_nombre, p.descripcion as producto_descripcion " +
                    "FROM ventas v " +
                    "INNER JOIN clientes c ON v.id_cliente = c.id " +
                    "INNER JOIN productos p ON v.id_producto = p.id " +
                    "WHERE v.id = ?";
        Venta venta = null;
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                venta = mapResultSetToVenta(rs);
            }
            
        } catch (SQLException e) {
            throw new DAOException("Error al buscar venta por ID: " + id, e);
        }
        
        return venta;
    }
    
    @Override
    public List<Venta> buscarTodos() throws DAOException {
        String sql = "SELECT v.*, c.nombre as cliente_nombre, c.apellido as cliente_apellido, c.email as cliente_email, " +
                    "p.nombre as producto_nombre, p.descripcion as producto_descripcion " +
                    "FROM ventas v " +
                    "INNER JOIN clientes c ON v.id_cliente = c.id " +
                    "INNER JOIN productos p ON v.id_producto = p.id " +
                    "ORDER BY v.fecha DESC";
        List<Venta> ventas = new ArrayList<>();
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                ventas.add(mapResultSetToVenta(rs));
            }
            
        } catch (SQLException e) {
            throw new DAOException("Error al buscar todas las ventas", e);
        }
        
        return ventas;
    }
    
    @Override
    public List<Venta> buscarPorFecha(LocalDate fecha) throws DAOException {
        String sql = "SELECT v.*, c.nombre as cliente_nombre, c.apellido as cliente_apellido, c.email as cliente_email, " +
                    "p.nombre as producto_nombre, p.descripcion as producto_descripcion " +
                    "FROM ventas v " +
                    "INNER JOIN clientes c ON v.id_cliente = c.id " +
                    "INNER JOIN productos p ON v.id_producto = p.id " +
                    "WHERE DATE(v.fecha) = ? " +
                    "ORDER BY v.fecha";
        List<Venta> ventas = new ArrayList<>();
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setDate(1, Date.valueOf(fecha));
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                ventas.add(mapResultSetToVenta(rs));
            }
            
        } catch (SQLException e) {
            throw new DAOException("Error al buscar ventas por fecha: " + fecha, e);
        }
        
        return ventas;
    }
    
    @Override
    public List<Venta> buscarPorCliente(int clienteId) throws DAOException {
        String sql = "SELECT v.*, c.nombre as cliente_nombre, c.apellido as cliente_apellido, c.email as cliente_email, " +
                    "p.nombre as producto_nombre, p.descripcion as producto_descripcion " +
                    "FROM ventas v " +
                    "INNER JOIN clientes c ON v.id_cliente = c.id " +
                    "INNER JOIN productos p ON v.id_producto = p.id " +
                    "WHERE v.id_cliente = ? " +
                    "ORDER BY v.fecha DESC";
        List<Venta> ventas = new ArrayList<>();
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, clienteId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                ventas.add(mapResultSetToVenta(rs));
            }
            
        } catch (SQLException e) {
            throw new DAOException("Error al buscar ventas por cliente ID: " + clienteId, e);
        }
        
        return ventas;
    }
    
    @Override
    public List<Venta> buscarPorProducto(int productoId) throws DAOException {
        String sql = "SELECT v.*, c.nombre as cliente_nombre, c.apellido as cliente_apellido, c.email as cliente_email, " +
                    "p.nombre as producto_nombre, p.descripcion as producto_descripcion " +
                    "FROM ventas v " +
                    "INNER JOIN clientes c ON v.id_cliente = c.id " +
                    "INNER JOIN productos p ON v.id_producto = p.id " +
                    "WHERE v.id_producto = ? " +
                    "ORDER BY v.fecha DESC";
        List<Venta> ventas = new ArrayList<>();
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, productoId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                ventas.add(mapResultSetToVenta(rs));
            }
            
        } catch (SQLException e) {
            throw new DAOException("Error al buscar ventas por producto ID: " + productoId, e);
        }
        
        return ventas;
    }
    
    @Override
    public List<Venta> buscarPorMetodoPago(int metodoPagoId) throws DAOException {
        String sql = "SELECT v.*, c.nombre as cliente_nombre, c.apellido as cliente_apellido, c.email as cliente_email, " +
                    "p.nombre as producto_nombre, p.descripcion as producto_descripcion " +
                    "FROM ventas v " +
                    "INNER JOIN clientes c ON v.id_cliente = c.id " +
                    "INNER JOIN productos p ON v.id_producto = p.id " +
                    "WHERE v.id_metodo_pago = ? " +
                    "ORDER BY v.fecha DESC";
        List<Venta> ventas = new ArrayList<>();
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, metodoPagoId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                ventas.add(mapResultSetToVenta(rs));
            }
            
        } catch (SQLException e) {
            throw new DAOException("Error al buscar ventas por método de pago ID: " + metodoPagoId, e);
        }
        
        return ventas;
    }
    
    @Override
    public List<Object[]> obtenerTotalVentasPorDia(LocalDate fechaInicio, LocalDate fechaFin) throws DAOException {
        String sql = "SELECT DATE(fecha) as dia, SUM(total) as total_dia, COUNT(*) as cantidad_ventas " +
                    "FROM ventas " +
                    "WHERE DATE(fecha) BETWEEN ? AND ? " +
                    "GROUP BY DATE(fecha) " +
                    "ORDER BY dia";
        List<Object[]> resultados = new ArrayList<>();
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setDate(1, Date.valueOf(fechaInicio));
            stmt.setDate(2, Date.valueOf(fechaFin));
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Object[] fila = {
                    rs.getDate("dia"),
                    rs.getDouble("total_dia"),
                    rs.getInt("cantidad_ventas")
                };
                resultados.add(fila);
            }
            
        } catch (SQLException e) {
            throw new DAOException("Error al obtener total de ventas por día", e);
        }
        
        return resultados;
    }
    
    @Override
    public List<Object[]> obtenerVentasPorCategoria() throws DAOException {
        String sql = "SELECT cat.nombre as categoria, SUM(v.total) as total_ventas, COUNT(*) as cantidad_ventas " +
                    "FROM ventas v " +
                    "INNER JOIN productos p ON v.id_producto = p.id " +
                    "INNER JOIN categorias cat ON p.id_categoria = cat.id " +
                    "GROUP BY cat.id, cat.nombre " +
                    "ORDER BY total_ventas DESC";
        List<Object[]> resultados = new ArrayList<>();
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Object[] fila = {
                    rs.getString("categoria"),
                    rs.getDouble("total_ventas"),
                    rs.getInt("cantidad_ventas")
                };
                resultados.add(fila);
            }
            
        } catch (SQLException e) {
            throw new DAOException("Error al obtener ventas por categoría", e);
        }
        
        return resultados;
    }
    
    @Override
    public List<Object[]> obtenerProductosMasVendidos(int limite) throws DAOException {
        String sql = "SELECT p.nombre as producto, SUM(v.cantidad) as unidades_vendidas, SUM(v.total) as total_ventas " +
                    "FROM ventas v " +
                    "INNER JOIN productos p ON v.id_producto = p.id " +
                    "GROUP BY p.id, p.nombre " +
                    "ORDER BY unidades_vendidas DESC " +
                    "LIMIT ?";
        List<Object[]> resultados = new ArrayList<>();
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, limite);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Object[] fila = {
                    rs.getString("producto"),
                    rs.getInt("unidades_vendidas"),
                    rs.getDouble("total_ventas")
                };
                resultados.add(fila);
            }
            
        } catch (SQLException e) {
            throw new DAOException("Error al obtener productos más vendidos", e);
        }
        
        return resultados;
    }
    
    @Override
    public List<Object[]> obtenerVentasPorMetodoPago() throws DAOException {
        String sql = "SELECT mp.nombre as metodo_pago, SUM(v.total) as total_ventas, COUNT(*) as cantidad_ventas, AVG(v.total) as promedio_venta " +
                    "FROM ventas v " +
                    "INNER JOIN metodos_pago mp ON v.id_metodo_pago = mp.id " +
                    "GROUP BY mp.id, mp.nombre " +
                    "ORDER BY total_ventas DESC";
        List<Object[]> resultados = new ArrayList<>();
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Object[] fila = {
                    rs.getString("metodo_pago"),
                    rs.getDouble("total_ventas"),
                    rs.getInt("cantidad_ventas"),
                    rs.getDouble("promedio_venta")
                };
                resultados.add(fila);
            }
            
        } catch (SQLException e) {
            throw new DAOException("Error al obtener ventas por método de pago", e);
        }
        
        return resultados;
    }
    
    @Override
    public double calcularPromedioVentasDiarias(LocalDate fechaInicio, LocalDate fechaFin) throws DAOException {
        String sql = "SELECT AVG(total_dia) as promedio FROM (" +
                    "SELECT DATE(fecha) as dia, SUM(total) as total_dia " +
                    "FROM ventas " +
                    "WHERE DATE(fecha) BETWEEN ? AND ? " +
                    "GROUP BY DATE(fecha)" +
                    ") as ventas_por_dia";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setDate(1, Date.valueOf(fechaInicio));
            stmt.setDate(2, Date.valueOf(fechaFin));
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getDouble("promedio");
            }
            
        } catch (SQLException e) {
            throw new DAOException("Error al calcular promedio de ventas diarias", e);
        }
        
        return 0.0;
    }
    
    @Override
    public double calcularDesviacionEstandarVentas(LocalDate fechaInicio, LocalDate fechaFin) throws DAOException {
        String sql = "SELECT STDDEV(total_dia) as desviacion FROM (" +
                    "SELECT DATE(fecha) as dia, SUM(total) as total_dia " +
                    "FROM ventas " +
                    "WHERE DATE(fecha) BETWEEN ? AND ? " +
                    "GROUP BY DATE(fecha)" +
                    ") as ventas_por_dia";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setDate(1, Date.valueOf(fechaInicio));
            stmt.setDate(2, Date.valueOf(fechaFin));
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getDouble("desviacion");
            }
            
        } catch (SQLException e) {
            throw new DAOException("Error al calcular desviación estándar de ventas", e);
        }
        
        return 0.0;
    }
    
    @Override
    public void actualizar(Venta venta) throws DAOException {
        String sql = "UPDATE ventas SET fecha = ?, id_cliente = ?, id_producto = ?, cantidad = ?, precio_unitario = ?, id_metodo_pago = ?, total = ? WHERE id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setTimestamp(1, Timestamp.valueOf(venta.getFecha()));
            stmt.setInt(2, venta.getCliente().getId());
            stmt.setInt(3, venta.getProducto().getId());
            stmt.setInt(4, venta.getCantidad());
            stmt.setDouble(5, venta.getPrecioUnitario());
            stmt.setInt(6, venta.getIdMetodoPago());
            stmt.setDouble(7, venta.getTotal());
            stmt.setInt(8, venta.getId());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows == 0) {
                throw new DAOException("Error al actualizar venta, ninguna fila afectada.");
            }
            
        } catch (SQLException e) {
            throw new DAOException("Error al actualizar venta con ID: " + venta.getId(), e);
        }
    }
    
    @Override
    public void eliminar(int id) throws DAOException {
        String sql = "DELETE FROM ventas WHERE id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows == 0) {
                throw new DAOException("Error al eliminar venta, ninguna fila afectada.");
            }
            
        } catch (SQLException e) {
            throw new DAOException("Error al eliminar venta con ID: " + id, e);
        }
    }
    
    private Venta mapResultSetToVenta(ResultSet rs) throws SQLException {
        Venta venta = new Venta();
        venta.setId(rs.getInt("id"));
        venta.setFecha(rs.getTimestamp("fecha").toLocalDateTime());
        
        // Crear y configurar Cliente
        Cliente cliente = new Cliente();
        cliente.setId(rs.getInt("id_cliente"));
        cliente.setNombre(rs.getString("cliente_nombre"));
        cliente.setApellido(rs.getString("cliente_apellido"));
        cliente.setEmail(rs.getString("cliente_email"));
        venta.setCliente(cliente);
        
        // Crear y configurar Producto
        Producto producto = new Producto();
        producto.setId(rs.getInt("id_producto"));
        producto.setNombre(rs.getString("producto_nombre"));
        producto.setDescripcion(rs.getString("producto_descripcion"));
        venta.setProducto(producto);
        
        venta.setCantidad(rs.getInt("cantidad"));
        venta.setPrecioUnitario(rs.getDouble("precio_unitario"));
        venta.setIdMetodoPago(rs.getInt("id_metodo_pago"));
        venta.setTotal(rs.getDouble("total"));
        
        return venta;
    }
}