package com.idra.gestionpeluqueria.service;

import com.idra.gestionpeluqueria.model.Zona;
import com.idra.gestionpeluqueria.config.DatabaseConfig;
import com.idra.gestionpeluqueria.exception.ServiceException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ZonaService {
    
    public List<Zona> listarTodas() throws ServiceException {
        List<Zona> zonas = new ArrayList<>();
        String sql = "SELECT id, nombre, provincia FROM zonas ORDER BY provincia, nombre";
        
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Zona zona = new Zona();
                zona.setId(rs.getInt("id"));
                zona.setCiudad(rs.getString("nombre"));
                zona.setProvincia(rs.getString("provincia"));
                zonas.add(zona);
            }
            
        } catch (SQLException e) {
            throw new ServiceException("Error al listar zonas: " + e.getMessage());
        }
        return zonas;
    }
    
    public List<String> listarCiudades() throws ServiceException {
        List<String> ciudades = new ArrayList<>();
        String sql = "SELECT DISTINCT nombre FROM zonas ORDER BY nombre";
        
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                ciudades.add(rs.getString("nombre"));
            }
            
        } catch (SQLException e) {
            throw new ServiceException("Error al obtener ciudades: " + e.getMessage());
        }
        return ciudades;
    }
    
    public List<String> listarProvincias() throws ServiceException {
        List<String> provincias = new ArrayList<>();
        String sql = "SELECT DISTINCT provincia FROM zonas ORDER BY provincia";
        
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                provincias.add(rs.getString("provincia"));
            }
            
        } catch (SQLException e) {
            throw new ServiceException("Error al obtener provincias: " + e.getMessage());
        }
        return provincias;
    }
    
    public Zona buscarPorId(int id) throws ServiceException {
        String sql = "SELECT id, nombre, provincia FROM zonas WHERE id = ?";
        
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Zona zona = new Zona();
                    zona.setId(rs.getInt("id"));
                    zona.setCiudad(rs.getString("nombre"));
                    zona.setProvincia(rs.getString("provincia"));
                    return zona;
                }
            }
            
        } catch (SQLException e) {
            throw new ServiceException("Error al buscar zona por ID: " + e.getMessage());
        }
        return null;
    }
    
    public Zona buscarPorCiudadYProvincia(String ciudad, String provincia) throws ServiceException {
        String sql = "SELECT id, nombre, provincia FROM zonas WHERE nombre = ? AND provincia = ?";
        
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, ciudad);
            stmt.setString(2, provincia);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Zona zona = new Zona();
                    zona.setId(rs.getInt("id"));
                    zona.setCiudad(rs.getString("nombre"));
                    zona.setProvincia(rs.getString("provincia"));
                    return zona;
                }
            }
            
        } catch (SQLException e) {
            throw new ServiceException("Error al buscar zona: " + e.getMessage());
        }
        return null;
    }
    
    public void crearZona(Zona zona) throws ServiceException {
        String sql = "INSERT INTO zonas (nombre, provincia) VALUES (?, ?)";
        
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, zona.getCiudad());
            stmt.setString(2, zona.getProvincia());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows == 0) {
                throw new ServiceException("Error al crear zona, ninguna fila afectada");
            }
            
            // Obtener el ID generado
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    zona.setId(generatedKeys.getInt(1));
                }
            }
            
        } catch (SQLException e) {
            throw new ServiceException("Error al crear zona: " + e.getMessage());
        }
    }
    
    public boolean existeZona(String ciudad, String provincia) throws ServiceException {
        String sql = "SELECT COUNT(*) FROM zonas WHERE nombre = ? AND provincia = ?";
        
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, ciudad);
            stmt.setString(2, provincia);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
            
        } catch (SQLException e) {
            throw new ServiceException("Error al verificar zona: " + e.getMessage());
        }
        return false;
    }
}