package com.idra.gestionpeluqueria.service;

import com.idra.gestionpeluqueria.model.Categoria;
import com.idra.gestionpeluqueria.config.DatabaseConfig;
import com.idra.gestionpeluqueria.exception.ServiceException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoriaService {
    
    public List<Categoria> listarTodas() throws ServiceException {
        List<Categoria> categorias = new ArrayList<>();
        String sql = "SELECT id, nombre, descripcion FROM categorias ORDER BY nombre";
        
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
                
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Categoria categoria = new Categoria();
                categoria.setId(rs.getInt("id"));
                categoria.setNombre(rs.getString("nombre"));
                categoria.setDescripcion(rs.getString("descripcion"));
                categorias.add(categoria);
            }
            
        } catch (SQLException e) {
            throw new ServiceException("Error al listar categorías: " + e.getMessage());
        }
        return categorias;
    }
    
    public List<String> listarNombres() throws ServiceException {
        List<String> nombres = new ArrayList<>();
        String sql = "SELECT nombre FROM categorias ORDER BY nombre";
        
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                nombres.add(rs.getString("nombre"));
            }
            
        } catch (SQLException e) {
            throw new ServiceException("Error al obtener nombres de categorías: " + e.getMessage());
        }
        return nombres;
    }
    
    public Categoria buscarPorId(int id) throws ServiceException {
        String sql = "SELECT id, nombre, descripcion FROM categorias WHERE id = ?";
        
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Categoria categoria = new Categoria();
                    categoria.setId(rs.getInt("id"));
                    categoria.setNombre(rs.getString("nombre"));
                    categoria.setDescripcion(rs.getString("descripcion"));
                    return categoria;
                }
            }
            
        } catch (SQLException e) {
            throw new ServiceException("Error al buscar categoría por ID: " + e.getMessage());
        }
        return null;
    }
    
    public String obtenerNombrePorId(int id) throws ServiceException {
        String sql = "SELECT nombre FROM categorias WHERE id = ?";
        
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("nombre");
                }
            }
            
        } catch (SQLException e) {
            throw new ServiceException("Error al obtener nombre de categoría: " + e.getMessage());
        }
        return "Desconocida";
    }
    
    public int obtenerIdPorNombre(String nombre) throws ServiceException {
        String sql = "SELECT id FROM categorias WHERE nombre = ?";
        
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, nombre);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
            
        } catch (SQLException e) {
            throw new ServiceException("Error al obtener ID de categoría: " + e.getMessage());
        }
        return -1; // No encontrado
    }
}