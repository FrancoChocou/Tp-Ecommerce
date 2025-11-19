package com.idra.gestionpeluqueria.dao.impl;
import java.time.LocalDate;
import com.idra.gestionpeluqueria.model.Producto;
import com.idra.gestionpeluqueria.config.DatabaseConfig;
import com.idra.gestionpeluqueria.exception.DAOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import com.idra.gestionpeluqueria.dao.ProductoDAO;

/**
 * Implementacion de la interfaz ProductoDAO para acceso a datos de productos.
 * Gestiona todas las operaciones de persistencia relacionadas con productos del
 * e-commerce en la base de datos MySQL.
 *
 * @author Idra
 */
public class ProductoDAOImpl implements ProductoDAO {

    private Connection getConnection() throws SQLException {
        return DatabaseConfig.getInstance().getConnection();
    }

    @Override
   public void crear(Producto producto) throws DAOException {
        String sql = "INSERT INTO productos (nombre, descripcion, id_categoria, precio_unitario, stock, activo, fecha_creacion) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            // Asegurar que la fecha no sea nula
            LocalDate fechaCreacion = producto.getFechaCreacion();
            if (fechaCreacion == null) {
                fechaCreacion = LocalDate.now();
                producto.setFechaCreacion(fechaCreacion);
            }
            
            stmt.setString(1, producto.getNombre());
            stmt.setString(2, producto.getDescripcion());
            stmt.setInt(3, producto.getIdCategoria());
            stmt.setDouble(4, producto.getPrecioUnitario());
            stmt.setInt(5, producto.getStock());
            stmt.setBoolean(6, producto.isActivo());
            stmt.setDate(7, java.sql.Date.valueOf(fechaCreacion)); // Usar fecha asegurada
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows == 0) {
                throw new DAOException("Error al crear producto, ninguna fila afectada");
            }
            
            // Obtener el ID generado
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    producto.setId(generatedKeys.getInt(1));
                } else {
                    throw new DAOException("Error al obtener ID generado para el producto");
                }
            }
            
        } catch (SQLException e) {
            throw new DAOException("Error al crear producto en la base de datos: " + e.getMessage(), e);
        }
    }
    
    @Override
    public Producto buscarPorId(int id) throws DAOException {
        String sql = "SELECT * FROM productos WHERE id = ?";
        Producto producto = null;

        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                producto = mapResultSetToProducto(rs);
            }

        } catch (SQLException e) {
            throw new DAOException("Error al buscar producto por ID: " + id, e);
        }

        return producto;
    }

    @Override
    public List<Producto> buscarTodos() throws DAOException {
        String sql = "SELECT * FROM productos ORDER BY nombre";
        List<Producto> productos = new ArrayList<>();

        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                productos.add(mapResultSetToProducto(rs));
            }

        } catch (SQLException e) {
            throw new DAOException("Error al buscar todos los productos", e);
        }

        return productos;
    }

    @Override
    public List<Producto> buscarActivos() throws DAOException {
        String sql = "SELECT * FROM productos WHERE activo = true ORDER BY nombre";
        List<Producto> productos = new ArrayList<>();

        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                productos.add(mapResultSetToProducto(rs));
            }

        } catch (SQLException e) {
            throw new DAOException("Error al buscar productos activos", e);
        }

        return productos;
    }

    @Override
    public List<Producto> buscarPorCategoria(int idCategoria) throws DAOException {
        String sql = "SELECT * FROM productos WHERE id_categoria = ? AND activo = true ORDER BY nombre";
        List<Producto> productos = new ArrayList<>();

        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idCategoria);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                productos.add(mapResultSetToProducto(rs));
            }

        } catch (SQLException e) {
            throw new DAOException("Error al buscar productos por categoría: " + idCategoria, e);
        }

        return productos;
    }

    @Override
    public List<Producto> buscarPorNombre(String nombre) throws DAOException {
        String sql = "SELECT * FROM productos WHERE nombre LIKE ? AND activo = true ORDER BY nombre";
        List<Producto> productos = new ArrayList<>();

        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            String searchPattern = "%" + nombre + "%";
            stmt.setString(1, searchPattern);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                productos.add(mapResultSetToProducto(rs));
            }

        } catch (SQLException e) {
            throw new DAOException("Error al buscar productos por nombre: " + nombre, e);
        }

        return productos;
    }

    @Override
    public List<Producto> buscarConStockBajo(int stockMinimo) throws DAOException {
        String sql = "SELECT * FROM productos WHERE stock <= ? AND activo = true ORDER BY stock ASC";
        List<Producto> productos = new ArrayList<>();

        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, stockMinimo);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                productos.add(mapResultSetToProducto(rs));
            }

        } catch (SQLException e) {
            throw new DAOException("Error al buscar productos con stock bajo", e);
        }

        return productos;
    }

    @Override
    public void actualizar(Producto producto) throws DAOException {
        String sql = "UPDATE productos SET nombre = ?, descripcion = ?, id_categoria = ?, precio_unitario = ?, stock = ?, activo = ? WHERE id = ?";

        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, producto.getNombre());
            stmt.setString(2, producto.getDescripcion());
            stmt.setInt(3, producto.getIdCategoria());
            stmt.setDouble(4, producto.getPrecioUnitario());
            stmt.setInt(5, producto.getStock());
            stmt.setBoolean(6, producto.isActivo());
            stmt.setInt(7, producto.getId());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new DAOException("Error al actualizar producto, ninguna fila afectada.");
            }

        } catch (SQLException e) {
            throw new DAOException("Error al actualizar producto con ID: " + producto.getId(), e);
        }
    }

    @Override
    public void actualizarStock(int idProducto, int nuevoStock) throws DAOException {
        String sql = "UPDATE productos SET stock = ? WHERE id = ?";

        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, nuevoStock);
            stmt.setInt(2, idProducto);

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new DAOException("Error al actualizar stock, ninguna fila afectada.");
            }

        } catch (SQLException e) {
            throw new DAOException("Error al actualizar stock del producto ID: " + idProducto, e);
        }
    }

    @Override
public void eliminar(int id) throws DAOException {
    
    String sql = "DELETE FROM productos WHERE id = ?";
    
    try (Connection conn = getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        
        stmt.setInt(1, id);
        int affectedRows = stmt.executeUpdate();
        
        if (affectedRows == 0) {
            throw new DAOException("Error al eliminar producto, ninguna fila afectada.");
        }
        
    } catch (SQLException e) {
        throw new DAOException("Error al eliminar producto con ID: " + id, e);
    }
}

    private Producto mapResultSetToProducto(ResultSet rs) throws SQLException {
        Producto producto = new Producto();
        producto.setId(rs.getInt("id"));
        producto.setNombre(rs.getString("nombre"));
        producto.setDescripcion(rs.getString("descripcion"));
        producto.setIdCategoria(rs.getInt("id_categoria"));
        producto.setPrecioUnitario(rs.getDouble("precio_unitario"));
        producto.setStock(rs.getInt("stock"));
        producto.setActivo(rs.getBoolean("activo"));
        producto.setFechaCreacion(rs.getDate("fecha_creacion").toLocalDate());
        return producto;
    }
}
