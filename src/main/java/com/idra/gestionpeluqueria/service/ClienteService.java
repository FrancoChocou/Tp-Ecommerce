package com.idra.gestionpeluqueria.service;

import com.idra.gestionpeluqueria.model.Cliente;
import com.idra.gestionpeluqueria.exception.ServiceException;
import java.util.List;
/**
 * Interfaz que define los servicios de negocio para la gestion de clientes 
 * Proporciona me todos para realizar operaciones CRUD, validaciones y 
 * consultas sobre clientes en el sistema.
 * 
 * @author Idra
 */
public interface ClienteService {
    /**
     * Crea un nuevo cliente en el sistema.
     * 
     * @param cliente El cliente a crear 
     * @throws ServiceException Si ocurre un error al crear el cliente 
     */
    void crearCliente(Cliente cliente) throws ServiceException;
    /**
     * Busca un cliente por su identificador unico.
     * 
     * @param id El ID del cliente a buscar 
     * @return El cliente encontrado o null si no existe 
     * @throws ServiceException Si ocurre un error al buscar el cliente 
     */
    Cliente buscarClientePorId(int id) throws ServiceException;
    /**
     * Obtiene todos los clientes registrados en el sistema 
     * 
     * @return Lista de todos los clientes 
     * @throws ServiceException Si ocurre un error al obtener los clientes 
     */
    List<Cliente> buscarTodosClientes() throws ServiceException;
    /**
     * Busca clientes cuyo nombre o apellido coincida con el criterio de busqueda 
     * 
     * @param nombre El nombre o apellido a buscar 
     * @return Lista de clientes que coinciden con la busqueda 
     * @throws ServiceException Si ocurre un error al buscar los clientes 
     */
    List<Cliente> buscarClientesPorNombre(String nombre) throws ServiceException;
    /**
     * Actualiza la informacion de un cliente existente 
     * 
     * @param cliente El cliente con la informacion actualizada 
     * @throws ServiceException Si ocurre un error al actualizar el cliente 
     */
    void actualizarCliente(Cliente cliente) throws ServiceException;
    /**
     * Elimina un cliente del sistema 
     * 
     * @param id El ID del cliente a eliminar 
     * @throws ServiceException Si ocurre un error al eliminar el cliente 
     */
    void eliminarCliente(int id) throws ServiceException;
    /**
     * Valida que los datos de un cliente cumplan con las reglas del negocio.
     * 
     * @param cliente El cliente a validar 
     * @return true si los datos son validos, false en caso contrario 
     * @throws ServiceException Si ocurre un error durante la validacion 
     */
    boolean validarCliente(Cliente cliente) throws ServiceException;
}