package com.idra.gestionpeluqueria.dao;

import com.idra.gestionpeluqueria.model.Producto;
import com.idra.gestionpeluqueria.exception.DAOException;
import java.util.List;

/**
 * Interfaz que define las operaciones de acceso a datos para la entidad Producto.
 * Proporciona metodos para gestionar los productos del e-commerce en la base de datos,
 * incluyendo operaciones CRUD y consultas especializadas.
 * 
 * @author Franco
 */
public interface ProductoDAO {
    /**
     * Crea un nuevo producto en la base de datos.
     * 
     * @param producto El producto a crear 
     * @throws DAOException Si ocurre un error al crear el producto 
     */
    void crear(Producto producto) throws DAOException;
    
    /**
     * Busca un producto por su identificador unico.
     * 
     * @param id El ID del producto a buscar 
     * @return El producto encontrado o null si no existe 
     * @throws DAOException Si ocurre un error al buscar el producto 
     */
    Producto buscarPorId(int id) throws DAOException;
    
    /**
     * Obtiene todos los productos registrados en el sistema.
     * 
     * @return Lista de todos los productos 
     * @throws DAOException Si ocurre un error al obtener los productos 
     */
    List<Producto> buscarTodos() throws DAOException;
    
    /**
     * Obtiene unicamente los productos que estan marcados como activos.
     * 
     * @return Lista de productos activos 
     * @throws DAOException Si ocurre un error al obtener los productos 
     */
    List<Producto> buscarActivos() throws DAOException;
    
    /**
     * Busca productos que pertenezcan a una categoria especifica.
     * 
     * @param idCategoria El ID de la categoria a buscar 
     * @return Lista de productos de la categoria especificada 
     * @throws DAOException Si ocurre un error al buscar los productos 
     */
    List<Producto> buscarPorCategoria(int idCategoria) throws DAOException;
    
    /**
     * Busca productos cuyo nombre contenga el texto especificado.
     * 
     * @param nombre El texto a buscar en los nombres de productos
     * @return Lista de productos que coinciden con la busqueda 
     * @throws DAOException Si ocurre un error al buscar los productos 
     */
    List<Producto> buscarPorNombre(String nombre) throws DAOException;
    
    /**
     * Busca productos con stock bajo (menor al limite especificado).
     * 
     * @param stockMinimo El limite minimo de stock 
     * @return Lista de productos con stock bajo 
     * @throws DAOException Si ocurre un error al buscar los productos 
     */
    List<Producto> buscarConStockBajo(int stockMinimo) throws DAOException;
    
    /**
     * Actualiza los datos de un producto existente.
     * 
     * @param producto El producto con los datos actualizados 
     * @throws DAOException Si ocurre un error al actualizar el producto 
     */
    void actualizar(Producto producto) throws DAOException;
    
    /**
     * Actualiza el stock de un producto especifico.
     * 
     * @param idProducto El ID del producto a actualizar
     * @param nuevoStock El nuevo valor de stock 
     * @throws DAOException Si ocurre un error al actualizar el stock 
     */
    void actualizarStock(int idProducto, int nuevoStock) throws DAOException;
    
    /**
     * Elimina un producto de la base de datos (borrado logico).
     * 
     * @param id El ID del producto a eliminar 
     * @throws DAOException Si ocurre un error al eliminar el producto
     */
    void eliminar(int id) throws DAOException;
}