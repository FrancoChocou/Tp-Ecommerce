package com.idra.gestionpeluqueria.service;

import com.idra.gestionpeluqueria.model.Producto;
import com.idra.gestionpeluqueria.exception.ServiceException;
import java.util.List;

/**
 * Interfaz que define los servicios de negocio para la gestion de productos del e-commerce.
 * Proporciona metodos para realizar operaciones CRUD, validaciones y 
 * consultas sobre productos ofrecidos en el e-commerce.
 * 
 * @author Idra
 */
public interface ProductoService {
    /**
     * Crea un nuevo producto en el sistema.
     * 
     * @param producto El producto a crear
     * @throws ServiceException Si ocurre un error al crear el producto
     */
    void crearProducto(Producto producto) throws ServiceException;
    
    /**
     * Busca un producto por su identificador único.
     * 
     * @param id El ID del producto a buscar
     * @return El producto encontrado o null si no existe
     * @throws ServiceException Si ocurre un error al buscar el producto
     */
    Producto buscarProductoPorId(int id) throws ServiceException;
    
    /**
     * Obtiene todos los productos registrados en el sistema.
     * 
     * @return Lista de todos los productos
     * @throws ServiceException Si ocurre un error al obtener los productos
     */
    List<Producto> buscarTodosProductos() throws ServiceException;
    
    /**
     * Obtiene únicamente los productos que están activos.
     * 
     * @return Lista de productos activos
     * @throws ServiceException Si ocurre un error al obtener los productos
     */
    List<Producto> buscarProductosActivos() throws ServiceException;
    
    /**
     * Busca productos que pertenezcan a una categoría específica.
     * 
     * @param idCategoria El ID de la categoría a buscar
     * @return Lista de productos de la categoría especificada
     * @throws ServiceException Si ocurre un error al buscar los productos
     */
    List<Producto> buscarProductosPorCategoria(int idCategoria) throws ServiceException;
    
    /**
     * Busca productos cuyo nombre contenga el texto especificado.
     * 
     * @param nombre El texto a buscar en los nombres de productos
     * @return Lista de productos que coinciden con la búsqueda
     * @throws ServiceException Si ocurre un error al buscar los productos
     */
    List<Producto> buscarProductosPorNombre(String nombre) throws ServiceException;
    
    /**
     * Busca productos con stock bajo (menor al límite especificado).
     * 
     * @param stockMinimo El límite mínimo de stock
     * @return Lista de productos con stock bajo
     * @throws ServiceException Si ocurre un error al buscar los productos
     */
    List<Producto> buscarProductosConStockBajo(int stockMinimo) throws ServiceException;
    
    /**
     * Actualiza la información de un producto existente.
     * 
     * @param producto El producto con la información actualizada
     * @throws ServiceException Si ocurre un error al actualizar el producto
     */
    void actualizarProducto(Producto producto) throws ServiceException;
    
    /**
     * Actualiza el stock de un producto específico.
     * 
     * @param idProducto El ID del producto a actualizar
     * @param nuevoStock El nuevo valor de stock
     * @throws ServiceException Si ocurre un error al actualizar el stock
     */
    void actualizarStockProducto(int idProducto, int nuevoStock) throws ServiceException;
    
    /**
     * Reduce el stock de un producto después de una venta.
     * 
     * @param idProducto El ID del producto
     * @param cantidad La cantidad a reducir
     * @throws ServiceException Si no hay suficiente stock o ocurre un error
     */
    void reducirStockProducto(int idProducto, int cantidad) throws ServiceException;
    
    /**
     * Elimina un producto del sistema (borrado lógico).
     * 
     * @param id El ID del producto a eliminar
     * @throws ServiceException Si ocurre un error al eliminar el producto
     */
    void eliminarProducto(int id) throws ServiceException;
    
    /**
     * Valida que los datos de un producto cumplan con las reglas de negocio.
     * 
     * @param producto El producto a validar
     * @return true si los datos son válidos, false en caso contrario
     * @throws ServiceException Si ocurre un error durante la validación
     */
    boolean validarProducto(Producto producto) throws ServiceException;
}