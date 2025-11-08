package com.idra.gestionpeluqueria.service.impl;

import com.idra.gestionpeluqueria.dao.ClienteDAO;
import com.idra.gestionpeluqueria.exception.DAOException;
import com.idra.gestionpeluqueria.model.Cliente;
import com.idra.gestionpeluqueria.service.ClienteService;
import com.idra.gestionpeluqueria.exception.ServiceException;
import com.idra.gestionpeluqueria.exception.ValidacionException;
import java.util.List;

/**
 * Implementacion de la interfaz ClienteService.
 * Gestiona la logica de negocio relacionada con clientes,incluyendo
 * validaciones y coordinacion con la capa de acceso a datos.
 * 
 * @author Idra
 */
public class ClienteServiceImpl implements ClienteService {
    
    private ClienteDAO clienteDAO;
    
    /**
     * Constructor que inicializa el servicio con su DAO correspondiente.
     * 
     * @param clienteDAO El DAO para operaciones de persistencia de clientes
     */
    public ClienteServiceImpl(ClienteDAO clienteDAO) {
        this.clienteDAO = clienteDAO;
    }
    
    @Override
    public void crearCliente(Cliente cliente) throws ServiceException {
        try {
            if (!validarCliente(cliente)) {
                throw new ValidacionException("Datos del cliente no válidos");
            }
            
            if (clienteDAO.existeTelefono(cliente.getTelefono())) {
                throw new ValidacionException("Ya existe un cliente con ese teléfono");
            }
            
            clienteDAO.crear(cliente);
        } catch (DAOException | ServiceException | ValidacionException e) {
            throw new ServiceException("Error al crear cliente: " + e.getMessage(), e);
        }
    }
    
    @Override
    public Cliente buscarClientePorId(int id) throws ServiceException {
        try {
            return clienteDAO.buscarPorId(id);
        } catch (DAOException e) {
            throw new ServiceException("Error al buscar cliente por ID: " + e.getMessage(), e);
        }
    }
    
    @Override
    public List<Cliente> buscarTodosClientes() throws ServiceException {
        try {
            return clienteDAO.buscarTodos();
        } catch (DAOException e) {
            throw new ServiceException("Error al buscar todos los clientes: " + e.getMessage(), e);
        }
    }
    
    @Override
    public List<Cliente> buscarClientesPorNombre(String nombre) throws ServiceException {
        try {
            return clienteDAO.buscarPorNombre(nombre);
        } catch (DAOException e) {
            throw new ServiceException("Error al buscar clientes por nombre: " + e.getMessage(), e);
        }
    }
    
    @Override
    public void actualizarCliente(Cliente cliente) throws ServiceException {
        try {
            if (!validarCliente(cliente)) {
                throw new ValidacionException("Datos del cliente no válidos");
            }
            clienteDAO.actualizar(cliente);
        } catch (DAOException | ServiceException | ValidacionException e) {
            throw new ServiceException("Error al actualizar cliente: " + e.getMessage(), e);
        }
    }
    
    @Override
    public void eliminarCliente(int id) throws ServiceException {
        try {
            clienteDAO.eliminar(id);
        } catch (DAOException e) {
            throw new ServiceException("Error al eliminar cliente: " + e.getMessage(), e);
        }
    }
    
    @Override
    public boolean validarCliente(Cliente cliente) throws ServiceException {
        if (cliente == null) return false;
        if (cliente.getNombre() == null || cliente.getNombre().trim().isEmpty()) return false;
        if (cliente.getApellido() == null || cliente.getApellido().trim().isEmpty()) return false;
        if (cliente.getTelefono() == null || cliente.getTelefono().trim().isEmpty()) return false;
        if (cliente.getEmail() == null || cliente.getEmail().trim().isEmpty()) return false;
        
        // Validaciones para e-commerce (nuevas)
        if (cliente.getEdad() < 0 || cliente.getEdad() > 120) return false;
        if (cliente.getIdZona() <= 0) return false;
        
        // Validación básica de email
        if (!cliente.getEmail().contains("@")) return false;
        
        return true;
    }
}