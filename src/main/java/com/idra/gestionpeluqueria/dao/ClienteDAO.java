package com.idra.gestionpeluqueria.dao;

import com.idra.gestionpeluqueria.model.Cliente;
import com.idra.gestionpeluqueria.exception.DAOException;
import java.util.List;

/**
 * Interfaz que define las operaciones de acceso a datos para la entidad Cliente.
 * Proporciona metodos para realizar operaciones CRUD y consultas especificas
 * sobre la tabla de clientes en la base de datos.
 * 
 * @author Idra
 */
public interface ClienteDAO {
    /**
     * Crea un nuevo cliente en la base de datos 
     * 
     * @param cliente El cliente a crear 
     * @throws DAOException Si ocurre un error al crear el cliente 
     */
    void crear(Cliente cliente) throws DAOException;
    
    /**
     * Busca un cliente por su identificador unico 
     * 
     * @param id El ID del cliente a buscar 
     * @return El cliente encontrado o null si no existe 
     * @throws DAOException Si ocurre un error al buscar el cliente 
     */
    Cliente buscarPorId(int id) throws DAOException;
    
    /**
     * Obtiene todos los clientes registrados en el sistema 
     * 
     * @return Lista de todos los clientes 
     * @throws DAOException Si ocurre un error al obtener los clientes 
     */
    List<Cliente> buscarTodos() throws DAOException;
    
    /**
     * Busca clientes cuyo nombre o apellido coincida con el parametro de busqueda.
     * 
     * @param nombre El nombre o apellido a buscar 
     * @return Lista de clientes que coinciden con la busqueda 
     * @throws DAOException Si ocurre un error al buscar los clientes 
     */
    List<Cliente> buscarPorNombre(String nombre) throws DAOException;
    
    /**
     * Actualiza los datos de un cliente existente 
     * 
     * @param cliente El clienbte con los datos actualizados 
     * @throws DAOException Si ocurre un error al actualizar el cliente 
     */
    void actualizar(Cliente cliente) throws DAOException;
    
    /**
     * Elimina un cliente de la base de datos 
     * 
     * @param id El ID del cliente a eliminar 
     * @throws DAOException Si ocurre un error al eliminar el cliente 
     */
    void eliminar(int id) throws DAOException;
    
    /**
     * Verifica si ya existe un cliente con el numero de telefono especificado
     * 
     * @param telefono El numero de telefono a verificar 
     * @return true si el telefono ya existe, false en caso contrario 
     * @throws DAOException Si ocurre un error al verificar el telefono 
     */
    boolean existeTelefono(String telefono) throws DAOException;
}