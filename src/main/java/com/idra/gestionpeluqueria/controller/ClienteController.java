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
    
    public void crearCliente(Cliente cliente) throws ServiceException {
        clienteService.crearCliente(cliente);
    }
    
    public Cliente buscarClientePorId(int id) throws ServiceException {
        return clienteService.buscarClientePorId(id);
    }
    
    // ✅ CORREGIDO: Cambiar buscarTodosClientes() por listarTodos()
    public List<Cliente> listarTodos() throws ServiceException {
        return clienteService.listarTodos();
    }
    
    // ✅ CORREGIDO: Cambiar buscarClientesPorNombre() por buscarPorNombre()
    public List<Cliente> buscarPorNombre(String nombre) throws ServiceException {
        return clienteService.buscarPorNombre(nombre);
    }
    
    public void actualizarCliente(Cliente cliente) throws ServiceException {
        clienteService.actualizarCliente(cliente);
    }
    
    public void eliminarCliente(int id) throws ServiceException {
        clienteService.eliminarCliente(id);
    }
    
    public boolean validarCliente(Cliente cliente) throws ServiceException {
        return clienteService.validarCliente(cliente);
    }
}