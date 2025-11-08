package com.idra.gestionpeluqueria.controller;

import com.idra.gestionpeluqueria.model.Cliente;
import com.idra.gestionpeluqueria.service.ClienteService;
import com.idra.gestionpeluqueria.service.impl.ClienteServiceImpl;
import com.idra.gestionpeluqueria.dao.impl.ClienteDAOImpl;
import com.idra.gestionpeluqueria.exception.ServiceException;
import java.util.List;

public class ClienteController {
    
    private ClienteService clienteService;
    
    public ClienteController() {
        this.clienteService = new ClienteServiceImpl(new ClienteDAOImpl());
    }
    
    /**
     * Crea un nuevo cliente en el sistema
     * @param cliente El cliente a crear
     * @throws ServiceException Si ocurre un error al crear el cliente
     */
    public void crearCliente(Cliente cliente) throws ServiceException {
        clienteService.crearCliente(cliente);
    }
    
    /**
     * Busca un cliente por su ID
     * @param id El ID del cliente a buscar
     * @return El cliente encontrado o null si no existe
     * @throws ServiceException Si ocurre un error al buscar el cliente
     */
    public Cliente buscarClientePorId(int id) throws ServiceException {
        return clienteService.buscarClientePorId(id);
    }
    
    /**
     * Obtiene todos los clientes del sistema
     * @return Lista de todos los clientes
     * @throws ServiceException Si ocurre un error al obtener los clientes
     */
    public List<Cliente> obtenerTodosClientes() throws ServiceException {
        return clienteService.buscarTodosClientes();
    }
    
    /**
     * Busca clientes por nombre o apellido
     * @param nombre El nombre o apellido a buscar
     * @return Lista de clientes que coinciden con la búsqueda
     * @throws ServiceException Si ocurre un error al buscar los clientes
     */
    public List<Cliente> buscarClientesPorNombre(String nombre) throws ServiceException {
        return clienteService.buscarClientesPorNombre(nombre);
    }
    
    /**
     * Actualiza la información de un cliente existente
     * @param cliente El cliente con la información actualizada
     * @throws ServiceException Si ocurre un error al actualizar el cliente
     */
    public void actualizarCliente(Cliente cliente) throws ServiceException {
        clienteService.actualizarCliente(cliente);
    }
    
    /**
     * Elimina un cliente del sistema
     * @param id El ID del cliente a eliminar
     * @throws ServiceException Si ocurre un error al eliminar el cliente
     */
    public void eliminarCliente(int id) throws ServiceException {
        clienteService.eliminarCliente(id);
    }
    
    /**
     * Valida si los datos del cliente son correctos
     * @param cliente El cliente a validar
     * @return true si los datos son válidos, false en caso contrario
     * @throws ServiceException Si ocurre un error durante la validación
     */
    public boolean validarCliente(Cliente cliente) throws ServiceException {
        return clienteService.validarCliente(cliente);
    }
}