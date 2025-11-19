package com.idra.gestionpeluqueria.controller;

import com.idra.gestionpeluqueria.model.Zona;
import com.idra.gestionpeluqueria.service.ZonaService;
import com.idra.gestionpeluqueria.exception.ServiceException;
import java.util.List;

public class ZonaController {
    
    private ZonaService zonaService;
    
    public ZonaController() {
        this.zonaService = new ZonaService();
    }
    
    public List<Zona> listarTodasLasZonas() throws ServiceException {
        return zonaService.listarTodas();
    }
    
    public List<String> listarCiudades() throws ServiceException {
        return zonaService.listarCiudades();
    }
    
    public List<String> listarProvincias() throws ServiceException {
        return zonaService.listarProvincias();
    }
    
    public Zona buscarZonaPorId(int id) throws ServiceException {
        return zonaService.buscarPorId(id);
    }
    
    public Zona buscarZonaPorCiudadYProvincia(String ciudad, String provincia) throws ServiceException {
        return zonaService.buscarPorCiudadYProvincia(ciudad, provincia);
    }
    
    public void crearZona(Zona zona) throws ServiceException {
        zonaService.crearZona(zona);
    }
    
    public boolean existeZona(String ciudad, String provincia) throws ServiceException {
        return zonaService.existeZona(ciudad, provincia);
    }
    
    /**
     * Método para crear una nueva zona si no existe
     */
    public Zona crearZonaSiNoExiste(String ciudad, String provincia) throws ServiceException {
        // Primero verificar si ya existe
        Zona zonaExistente = zonaService.buscarPorCiudadYProvincia(ciudad, provincia);
        if (zonaExistente != null) {
            return zonaExistente;
        }
        
        // Si no existe, crear nueva
        Zona nuevaZona = new Zona(ciudad, provincia);
        zonaService.crearZona(nuevaZona);
        return nuevaZona;
    }
}