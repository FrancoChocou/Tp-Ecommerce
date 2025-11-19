package com.idra.gestionpeluqueria.controller;

import com.idra.gestionpeluqueria.model.Categoria;
import com.idra.gestionpeluqueria.service.CategoriaService;
import com.idra.gestionpeluqueria.exception.ServiceException;
import java.util.List;

public class CategoriaController {
    
    private CategoriaService categoriaService;
    
    public CategoriaController() {
        this.categoriaService = new CategoriaService();
    }
    
    public List<Categoria> listarTodasLasCategorias() throws ServiceException {
        return categoriaService.listarTodas();
    }
    
    public List<String> listarNombresCategorias() throws ServiceException {
        return categoriaService.listarNombres();
    }
    
    public Categoria buscarCategoriaPorId(int id) throws ServiceException {
        return categoriaService.buscarPorId(id);
    }
    
    public String obtenerNombreCategoriaPorId(int id) throws ServiceException {
        return categoriaService.obtenerNombrePorId(id);
    }
    
    public int obtenerIdCategoriaPorNombre(String nombre) throws ServiceException {
        return categoriaService.obtenerIdPorNombre(nombre);
    }
}