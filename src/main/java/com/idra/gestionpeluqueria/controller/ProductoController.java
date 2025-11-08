package com.idra.gestionpeluqueria.controller;

import com.idra.gestionpeluqueria.model.Producto;
import com.idra.gestionpeluqueria.service.impl.ProductoServiceImpl;
import com.idra.gestionpeluqueria.dao.impl.ProductoDAOImpl;
import com.idra.gestionpeluqueria.exception.ServiceException;
import java.util.List;
import com.idra.gestionpeluqueria.service.ProductoService;

/**
 * Controlador para la gestion de productos del e-commerce.
 * Actua como intermediario entre la capa de presentacion y la capa de servicio,
 * delegando las operaciones CRUD y validaciones al ProductoService.
 * 
 * @author Idra
 */
public class ProductoController {
    
    private ProductoService productoService;
    
    /**
     * Constructor que inicializa el controlador con sus dependencias.
     * Crea una instancia de ProductoServiceImpl con su DAO correspondiente.
     */
    public ProductoController() {
        this.productoService = new ProductoServiceImpl(new ProductoDAOImpl());
    }
    
    /**
     * Crea un nuevo producto en el sistema.
     * 
     * @param producto El producto a crear 
     * @throws ServiceException Si ocurre un error al crear el producto
     */
    public void crearProducto(Producto producto) throws ServiceException {
        productoService.crearProducto(producto);
    }
    
    /**
     * Busca un producto por su identificador unico.
     * 
     * @param id El ID del producto a buscar 
     * @return El producto encontrado o null si no existe
     * @throws ServiceException Si ocurre un error al buscar el producto
     */
    public Producto buscarProductoPorId(int id) throws ServiceException {
        return productoService.buscarProductoPorId(id);
    }
    
    /**
     * Obtiene la lista completa de todos los productos registrados 
     * 
     * @return Lista de todos los productos en el sistema 
     * @throws ServiceException Si ocurre un error al obtener los productos 
     */
    public List<Producto> listarTodos() throws ServiceException {
        return productoService.buscarTodosProductos();
    }
    
    /**
     * Obtiene unicamente los productos que estan activos en el sistema.
     * 
     * @return Lista de productos activos 
     * @throws ServiceException Si ocurre un error al obtener los productos 
     */
    public List<Producto> buscarProductosActivos() throws ServiceException {
        return productoService.buscarProductosActivos();
    }
    
    /**
     * Busca productos que pertenezcan a una categoría específica.
     * 
     * @param idCategoria El ID de la categoría a buscar 
     * @return Lista de productos de la categoría especificada 
     * @throws ServiceException Si ocurre un error al buscar los productos 
     */
    public List<Producto> buscarProductosPorCategoria(int idCategoria) throws ServiceException {
        return productoService.buscarProductosPorCategoria(idCategoria);
    }
    
    /**
     * Busca productos cuyo nombre contenga el texto especificado.
     * 
     * @param nombre El texto a buscar en los nombres de productos
     * @return Lista de productos que coinciden con la búsqueda 
     * @throws ServiceException Si ocurre un error al buscar los productos 
     */
    public List<Producto> buscarPorNombre(String nombre) throws ServiceException {
        return productoService.buscarProductosPorNombre(nombre);
    }
    
    /**
     * Busca productos con stock bajo (menor al límite especificado).
     * 
     * @param stockMinimo El límite mínimo de stock 
     * @return Lista de productos con stock bajo 
     * @throws ServiceException Si ocurre un error al buscar los productos 
     */
    public List<Producto> buscarProductosConStockBajo(int stockMinimo) throws ServiceException {
        return productoService.buscarProductosConStockBajo(stockMinimo);
    }
    
    /**
     * Actualiza la informacion de un producto existente.
     * 
     * @param producto El producto con la informacion actualizada 
     * @throws ServiceException Si ocurre un error al actualizar el producto 
     */
    public void actualizarProducto(Producto producto) throws ServiceException {
        productoService.actualizarProducto(producto);
    }
    
    /**
     * Actualiza el stock de un producto específico.
     * 
     * @param idProducto El ID del producto a actualizar
     * @param nuevoStock El nuevo valor de stock 
     * @throws ServiceException Si ocurre un error al actualizar el stock 
     */
    public void actualizarStockProducto(int idProducto, int nuevoStock) throws ServiceException {
        productoService.actualizarStockProducto(idProducto, nuevoStock);
    }
    
    /**
     * Reduce el stock de un producto después de una venta.
     * 
     * @param idProducto El ID del producto
     * @param cantidad La cantidad a reducir
     * @throws ServiceException Si no hay suficiente stock o ocurre un error
     */
    public void reducirStockProducto(int idProducto, int cantidad) throws ServiceException {
        productoService.reducirStockProducto(idProducto, cantidad);
    }
    
    /**
     * Elimina un producto del sistema (borrado lógico).
     * 
     * @param id El ID del producto a eliminar 
     * @throws ServiceException Si ocurre un error al eliminar el producto 
     */
    public void eliminarProducto(int id) throws ServiceException {
        productoService.eliminarProducto(id);
    }
    
    /**
     * Valida si los datos de un producto son correctos y cumplen las reglas de negocio.
     * 
     * @param producto El producto a validar 
     * @return true si los datos son validos, false en caso contrario 
     * @throws ServiceException Si ocurre un error durante la validacion 
     */
    public boolean validarProducto(Producto producto) throws ServiceException {
        return productoService.validarProducto(producto);
    }
}