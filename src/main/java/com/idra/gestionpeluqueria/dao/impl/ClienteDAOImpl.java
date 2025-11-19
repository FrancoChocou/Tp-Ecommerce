package com.idra.gestionpeluqueria.dao.impl;

import com.idra.gestionpeluqueria.dao.ClienteDAO;
import com.idra.gestionpeluqueria.model.Cliente;
import com.idra.gestionpeluqueria.config.DatabaseConfig;
import com.idra.gestionpeluqueria.exception.DAOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementacion de la interfaz ClienteDAO para acceso a datos de clientes.
 * Gestiona todas las operaciones de persistencia relacionadas con clientes 
 * en la base de datos MySQL.
 * 
 * @author Idra
 */
public class ClienteDAOImpl implements ClienteDAO {
    
    private Connection getConnection() throws SQLException {
        return DatabaseConfig.getInstance().getConnection();
    }
    
    @Override
   
public void crear(Cliente cliente) throws DAOException {
    String sql = "INSERT INTO clientes (nombre, apellido, email, telefono, edad, id_zona, fecha_registro, activo) " +
                 "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    
    try (Connection conn = DatabaseConfig.getInstance().getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
        
        // ✅ ORDEN CORRECTO según la estructura de tu tabla
        stmt.setString(1, cliente.getNombre());      // nombre
        stmt.setString(2, cliente.getApellido());    // apellido  
        stmt.setString(3, cliente.getEmail());       // email
        stmt.setString(4, cliente.getTelefono());    // telefono
        stmt.setInt(5, cliente.getEdad());           // edad
        stmt.setInt(6, cliente.getIdZona());         // id_zona
        stmt.setDate(7, java.sql.Date.valueOf(cliente.getFechaRegistro())); // fecha_registro
        stmt.setBoolean(8, cliente.isActivo());      // activo
        
        int affectedRows = stmt.executeUpdate();
        
        if (affectedRows == 0) {
            throw new DAOException("Error al crear cliente, ninguna fila afectada");
        }
        
        // Obtener el ID generado
        try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                cliente.setId(generatedKeys.getInt(1));
            }
        }
        
    } catch (SQLException e) {
        throw new DAOException("Error al crear cliente: " + e.getMessage(), e);
    }
}
    
   @Override

public Cliente buscarPorId(int id) throws DAOException {
    String sql = "SELECT id, nombre, apellido, email, telefono, edad, id_zona, fecha_registro, activo " +
                 "FROM clientes WHERE id = ?";
    
    try (Connection conn = DatabaseConfig.getInstance().getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        
        stmt.setInt(1, id);
        try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                Cliente cliente = new Cliente();
                cliente.setId(rs.getInt("id"));
                cliente.setNombre(rs.getString("nombre"));
                
                // ✅ VERIFICAR QUE ESTE ORDEN ES CORRECTO
                cliente.setApellido(rs.getString("apellido")); // Columna 3: apellido
                cliente.setEmail(rs.getString("email"));       // Columna 4: email
                cliente.setTelefono(rs.getString("telefono")); // Columna 5: telefono
                
                cliente.setEdad(rs.getInt("edad"));
                cliente.setIdZona(rs.getInt("id_zona"));
                
                // Fecha registro
                java.sql.Date fechaSql = rs.getDate("fecha_registro");
                if (fechaSql != null) {
                    cliente.setFechaRegistro(fechaSql.toLocalDate());
                }
                
                cliente.setActivo(rs.getBoolean("activo"));
                
                // DEBUG
                System.out.println("DAO - Cliente cargado:");
                System.out.println("  Nombre: " + cliente.getNombre());
                System.out.println("  Apellido: " + cliente.getApellido());
                System.out.println("  Email: " + cliente.getEmail());
                System.out.println("  Activo: " + cliente.isActivo());
                
                return cliente;
            }
        }
    } catch (SQLException e) {
        throw new DAOException("Error al buscar cliente por ID: " + e.getMessage(), e);
    }
    return null;
}
    
    @Override
public List<Cliente> buscarTodos() throws DAOException {
    List<Cliente> clientes = new ArrayList<>();
    String sql = "SELECT id, nombre, apellido, email, telefono, edad, id_zona, fecha_registro, activo FROM clientes";
    
    try (Connection conn = DatabaseConfig.getInstance().getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql);
         ResultSet rs = stmt.executeQuery()) {
        
        while (rs.next()) {
            Cliente cliente = new Cliente();
            cliente.setId(rs.getInt("id"));
            cliente.setNombre(rs.getString("nombre"));
            
            // ✅ CORREGIDO: Orden correcto
            cliente.setApellido(rs.getString("apellido")); // ✅ apellido
            cliente.setEmail(rs.getString("email"));       // ✅ email
            
            cliente.setTelefono(rs.getString("telefono"));
            cliente.setEdad(rs.getInt("edad"));
            cliente.setIdZona(rs.getInt("id_zona"));
            
            java.sql.Date fechaSql = rs.getDate("fecha_registro");
            if (fechaSql != null) {
                cliente.setFechaRegistro(fechaSql.toLocalDate());
            }
            
            cliente.setActivo(rs.getBoolean("activo"));
            clientes.add(cliente);
        }
        
    } catch (SQLException e) {
        throw new DAOException("Error al obtener todos los clientes: " + e.getMessage(), e);
    }
    return clientes;
}
    
    @Override
public List<Cliente> buscarPorNombre(String nombre) throws DAOException {
    List<Cliente> clientes = new ArrayList<>();
    String sql = "SELECT id, nombre, apellido, email, telefono, edad, id_zona, fecha_registro, activo " +
                 "FROM clientes WHERE nombre LIKE ? OR apellido LIKE ?";
    
    try (Connection conn = DatabaseConfig.getInstance().getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        
        String likePattern = "%" + nombre + "%";
        stmt.setString(1, likePattern);
        stmt.setString(2, likePattern);
        
        try (ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Cliente cliente = new Cliente();
                cliente.setId(rs.getInt("id"));
                cliente.setNombre(rs.getString("nombre"));
                
                // ✅ CORREGIDO
                cliente.setApellido(rs.getString("apellido")); // ✅ apellido
                cliente.setEmail(rs.getString("email"));       // ✅ email
                
                cliente.setTelefono(rs.getString("telefono"));
                cliente.setEdad(rs.getInt("edad"));
                cliente.setIdZona(rs.getInt("id_zona"));
                
                java.sql.Date fechaSql = rs.getDate("fecha_registro");
                if (fechaSql != null) {
                    cliente.setFechaRegistro(fechaSql.toLocalDate());
                }
                
                cliente.setActivo(rs.getBoolean("activo"));
                clientes.add(cliente);
            }
        }
        
    } catch (SQLException e) {
        throw new DAOException("Error al buscar clientes por nombre: " + e.getMessage(), e);
    }
    return clientes;
}
    @Override
    public void actualizar(Cliente cliente) throws DAOException {
        String sql = "UPDATE clientes SET nombre = ?, apellido = ?, telefono = ?, email = ?, edad = ?, id_zona = ?, activo = ? WHERE id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, cliente.getNombre());
            stmt.setString(2, cliente.getApellido());
            stmt.setString(3, cliente.getTelefono());
            stmt.setString(4, cliente.getEmail());
            stmt.setInt(5, cliente.getEdad());
            stmt.setInt(6, cliente.getIdZona());
            stmt.setBoolean(7, cliente.isActivo());
            stmt.setInt(8, cliente.getId());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows == 0) {
                throw new DAOException("Error al actualizar cliente, ninguna fila afectada.");
            }
            
        } catch (SQLException e) {
            throw new DAOException("Error al actualizar cliente con ID: " + cliente.getId(), e);
        }
    }
    
   @Override
public void eliminar(int id) throws DAOException {
    // CAMBIO: Borrado físico en lugar de lógico
    String sql = "DELETE FROM clientes WHERE id = ?";
    
    try (Connection conn = getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        
        stmt.setInt(1, id);
        int affectedRows = stmt.executeUpdate();
        
        if (affectedRows == 0) {
            throw new DAOException("Error al eliminar cliente, ninguna fila afectada.");
        }
        
    } catch (SQLException e) {
        throw new DAOException("Error al eliminar cliente con ID: " + id, e);
    }
}


    @Override
    public boolean existeTelefono(String telefono) throws DAOException {
        String sql = "SELECT COUNT(*) FROM clientes WHERE telefono = ? AND activo = true";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, telefono);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            
        } catch (SQLException e) {
            throw new DAOException("Error al verificar existencia de teléfono: " + telefono, e);
        }
        
        return false;
    }
    
    private Cliente mapResultSetToCliente(ResultSet rs) throws SQLException {
        Cliente cliente = new Cliente();
        cliente.setId(rs.getInt("id"));
        cliente.setNombre(rs.getString("nombre"));
        cliente.setApellido(rs.getString("apellido"));
        cliente.setTelefono(rs.getString("telefono"));
        cliente.setEmail(rs.getString("email"));
        cliente.setEdad(rs.getInt("edad"));
        cliente.setIdZona(rs.getInt("id_zona"));
        cliente.setFechaRegistro(rs.getDate("fecha_registro").toLocalDate());
        cliente.setActivo(rs.getBoolean("activo"));
        return cliente;
    }
} 