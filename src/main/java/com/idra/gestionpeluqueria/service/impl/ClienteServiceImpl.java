package com.idra.gestionpeluqueria.service.impl;

import com.idra.gestionpeluqueria.model.Cliente;
import com.idra.gestionpeluqueria.dao.ClienteDAO;
import com.idra.gestionpeluqueria.service.ClienteService;
import com.idra.gestionpeluqueria.exception.ServiceException;
import java.util.List;

public class ClienteServiceImpl implements ClienteService {
    
    private ClienteDAO clienteDAO;
    
    public ClienteServiceImpl(ClienteDAO clienteDAO) {
        this.clienteDAO = clienteDAO;
    }
    
    @Override
    public void crearCliente(Cliente cliente) throws ServiceException {
        try {
            if (!validarCliente(cliente)) {
                throw new ServiceException("Datos del cliente no válidos");
            }
            clienteDAO.crear(cliente);
        } catch (Exception e) {
            throw new ServiceException("Error al crear cliente: " + e.getMessage(), e);
        }
    }
    
    @Override
    public Cliente buscarClientePorId(int id) throws ServiceException {
        try {
            return clienteDAO.buscarPorId(id);
        } catch (Exception e) {
            throw new ServiceException("Error al buscar cliente por ID: " + e.getMessage(), e);
        }
    }
    
    @Override
    public List<Cliente> listarTodos() throws ServiceException {
        try {
            return clienteDAO.buscarTodos();
        } catch (Exception e) {
            throw new ServiceException("Error al listar clientes: " + e.getMessage(), e);
        }
    }
    
    @Override
    public List<Cliente> buscarPorNombre(String nombre) throws ServiceException {
        try {
            return clienteDAO.buscarPorNombre(nombre);
        } catch (Exception e) {
            throw new ServiceException("Error al buscar clientes por nombre: " + e.getMessage(), e);
        }
    }
    
    @Override
    public void actualizarCliente(Cliente cliente) throws ServiceException {
        try {
            if (!validarCliente(cliente)) {
                throw new ServiceException("Datos del cliente no válidos");
            }
            clienteDAO.actualizar(cliente);
        } catch (Exception e) {
            throw new ServiceException("Error al actualizar cliente: " + e.getMessage(), e);
        }
    }
    
    @Override
    public void eliminarCliente(int id) throws ServiceException {
        try {
            clienteDAO.eliminar(id);
        } catch (Exception e) {
            throw new ServiceException("Error al eliminar cliente: " + e.getMessage(), e);
        }
    }
    
    @Override
    public boolean validarCliente(Cliente cliente) throws ServiceException {
        if (cliente == null) {
            System.out.println("Error: Cliente es null");
            return false;
        }
        
        // Validar nombre
        if (cliente.getNombre() == null || cliente.getNombre().trim().isEmpty()) {
            System.out.println("Error: Nombre vacío");
            return false;
        }
        
        // Validar apellido
        if (cliente.getApellido() == null || cliente.getApellido().trim().isEmpty()) {
            System.out.println("Error: Apellido vacío");
            return false;
        }
        
        // Validar email
        if (cliente.getEmail() == null || cliente.getEmail().trim().isEmpty()) {
            System.out.println("Error: Email vacío");
            return false;
        }
        
        // Validar formato de email básico
        String email = cliente.getEmail().trim();
        if (!email.contains("@") || !email.contains(".")) {
            System.out.println("Error: Formato de email inválido: " + email);
            return false;
        }
        
        // Validar teléfono
        if (cliente.getTelefono() == null || cliente.getTelefono().trim().isEmpty()) {
            System.out.println("Error: Teléfono vacío");
            return false;
        }
        
        // Validar edad (es int primitivo, no necesita verificar null)
        if (cliente.getEdad() < 0) {
            System.out.println("Error: Edad no puede ser negativa");
            return false;
        }
        
        System.out.println("✅ Cliente válido: " + cliente.getNombre() + " " + cliente.getApellido());
        return true;
    }
}