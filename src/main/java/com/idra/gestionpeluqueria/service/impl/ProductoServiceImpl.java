package com.idra.gestionpeluqueria.service.impl;

import com.idra.gestionpeluqueria.model.Producto;
import com.idra.gestionpeluqueria.exception.ServiceException;
import java.util.List;
import com.idra.gestionpeluqueria.exception.DAOException;
import com.idra.gestionpeluqueria.dao.ProductoDAO;
import com.idra.gestionpeluqueria.service.ProductoService;
import com.idra.gestionpeluqueria.exception.ValidacionException; 


/**
 * Implementacion de la interfaz ProductoService.
 * Gestiona la logica de negocio relacionada con productos del e-commerce,
 * incluyendo validaciones y coordinacion con la capa de acceso a datos.
 * 
 * @author Idra
 */
public class ProductoServiceImpl implements ProductoService {
    
    private ProductoDAO productoDAO;
    
    /**
     * Constructor que inicializa el servicio con su DAO correspondiente.
     * 
     * @param productoDAO El DAO para operaciones de persistencia de productos
     */
    public ProductoServiceImpl(ProductoDAO productoDAO) {
        this.productoDAO = productoDAO;
    }
    
    @Override
    public void crearProducto(Producto producto) throws ServiceException {
        try {
            if (!validarProducto(producto)) {
                throw new ValidacionException("Datos del producto no válidos");
            }
            productoDAO.crear(producto);
        } catch (Exception e) {
            throw new ServiceException("Error al crear producto: " + e.getMessage(), e);
        }
    }
    
    @Override
    public Producto buscarProductoPorId(int id) throws ServiceException {
        try {
            return productoDAO.buscarPorId(id);
        } catch (Exception e) {
            throw new ServiceException("Error al buscar producto por ID: " + e.getMessage(), e);
        }
    }
    
    @Override
    public List<Producto> buscarTodosProductos() throws ServiceException {
        try {
            return productoDAO.buscarTodos();
        } catch (Exception e) {
            throw new ServiceException("Error al buscar todos los productos: " + e.getMessage(), e);
        }
    }
    
    @Override
    public List<Producto> buscarProductosActivos() throws ServiceException {
        try {
            return productoDAO.buscarActivos();
        } catch (Exception e) {
            throw new ServiceException("Error al buscar productos activos: " + e.getMessage(), e);
        }
    }
    
    @Override
    public List<Producto> buscarProductosPorCategoria(int idCategoria) throws ServiceException {
        try {
            return productoDAO.buscarPorCategoria(idCategoria);
        } catch (Exception e) {
            throw new ServiceException("Error al buscar productos por categoría: " + e.getMessage(), e);
        }
    }
    
    @Override
    public List<Producto> buscarProductosPorNombre(String nombre) throws ServiceException {
        try {
            return productoDAO.buscarPorNombre(nombre);
        } catch (Exception e) {
            throw new ServiceException("Error al buscar productos por nombre: " + e.getMessage(), e);
        }
    }
    
    @Override
    public List<Producto> buscarProductosConStockBajo(int stockMinimo) throws ServiceException {
        try {
            return productoDAO.buscarConStockBajo(stockMinimo);
        } catch (Exception e) {
            throw new ServiceException("Error al buscar productos con stock bajo: " + e.getMessage(), e);
        }
    }
    
    @Override
    public void actualizarProducto(Producto producto) throws ServiceException {
        try {
            if (!validarProducto(producto)) {
                throw new ServiceException("Datos del producto no válidos");
            }
            productoDAO.actualizar(producto); 
        } catch (DAOException e) {
            throw new ServiceException("Error al actualizar producto: " + e.getMessage(), e);
        }
    }
    
    @Override
    public void actualizarStockProducto(int idProducto, int nuevoStock) throws ServiceException {
        try {
            if (nuevoStock < 0) {
                throw new ValidacionException("El stock no puede ser negativo");
            }
            productoDAO.actualizarStock(idProducto, nuevoStock);
        } catch (Exception e) {
            throw new ServiceException("Error al actualizar stock del producto: " + e.getMessage(), e);
        }
    }
    
    @Override
    public void reducirStockProducto(int idProducto, int cantidad) throws ServiceException {
        try {
            if (cantidad <= 0) {
                throw new ValidacionException("La cantidad a reducir debe ser mayor a cero");
            }
            
            Producto producto = productoDAO.buscarPorId(idProducto);
            if (producto == null) {
                throw new ValidacionException("Producto no encontrado");
            }
            
            if (!producto.tieneStock()) {
                throw new ValidacionException("El producto no tiene stock disponible");
            }
            
            if (producto.getStock() < cantidad) {
                throw new ValidacionException("Stock insuficiente. Disponible: " + producto.getStock() + ", Solicitado: " + cantidad);
            }
            
            productoDAO.actualizarStock(idProducto, producto.getStock() - cantidad);
            
        } catch (Exception e) {
            throw new ServiceException("Error al reducir stock del producto: " + e.getMessage(), e);
        }
    }
    
    @Override
    public void eliminarProducto(int id) throws ServiceException {
        try {
            productoDAO.eliminar(id);
        } catch (Exception e) {
            throw new ServiceException("Error al eliminar producto: " + e.getMessage(), e);
        }
    }
    
    @Override
    public boolean validarProducto(Producto producto) throws ServiceException {
        if (producto == null) return false;
        if (producto.getNombre() == null || producto.getNombre().trim().isEmpty()) return false;
        if (producto.getPrecioUnitario() <= 0) return false;
        if (producto.getStock() < 0) return false;
        if (producto.getIdCategoria() <= 0) return false;
        
        // Validación de descripción opcional pero recomendada
        if (producto.getDescripcion() == null || producto.getDescripcion().trim().isEmpty()) {
            // Podría ser una advertencia en lugar de un error
            System.out.println("Advertencia: Producto sin descripción");
        }
        
        return true;
    }
}