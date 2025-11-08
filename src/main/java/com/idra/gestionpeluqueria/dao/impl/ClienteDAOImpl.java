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
        String sql = "INSERT INTO clientes (nombre, apellido, telefono, email, edad, id_zona, fecha_registro, activo) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, cliente.getNombre());
            stmt.setString(2, cliente.getApellido());
            stmt.setString(3, cliente.getTelefono());
            stmt.setString(4, cliente.getEmail());
            stmt.setInt(5, cliente.getEdad());
            stmt.setInt(6, cliente.getIdZona());
            stmt.setDate(7, Date.valueOf(cliente.getFechaRegistro()));
            stmt.setBoolean(8, cliente.isActivo());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows == 0) {
                throw new DAOException("Error al crear cliente, ninguna fila afectada.");
            }
            
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    cliente.setId(generatedKeys.getInt(1));
                } else {
                    throw new DAOException("Error al obtener ID generado para el cliente.");
                }
            }
            
        } catch (SQLException e) {
            throw new DAOException("Error al crear cliente en la base de datos", e);
        }
    }
    
    @Override
    public Cliente buscarPorId(int id) throws DAOException {
        String sql = "SELECT * FROM clientes WHERE id = ?";
        Cliente cliente = null;
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                cliente = mapResultSetToCliente(rs);
            }
            
        } catch (SQLException e) {
            throw new DAOException("Error al buscar cliente por ID: " + id, e);
        }
        
        return cliente;
    }
    
    @Override
    public List<Cliente> buscarTodos() throws DAOException {
        String sql = "SELECT * FROM clientes ORDER BY apellido, nombre";
        List<Cliente> clientes = new ArrayList<>();
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                clientes.add(mapResultSetToCliente(rs));
            }
            
        } catch (SQLException e) {
            throw new DAOException("Error al buscar todos los clientes", e);
        }
        
        return clientes;
    }
    
    @Override
    public List<Cliente> buscarPorNombre(String nombre) throws DAOException {
        String sql = "SELECT * FROM clientes WHERE nombre LIKE ? OR apellido LIKE ? ORDER BY apellido, nombre";
        List<Cliente> clientes = new ArrayList<>();
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            String searchPattern = "%" + nombre + "%";
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);
            
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                clientes.add(mapResultSetToCliente(rs));
            }
            
        } catch (SQLException e) {
            throw new DAOException("Error al buscar clientes por nombre: " + nombre, e);
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
        // En lugar de eliminar, marcamos como inactivo para mantener integridad referencial
        String sql = "UPDATE clientes SET activo = false WHERE id = ?";
        
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