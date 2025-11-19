package com.idra.gestionpeluqueria.service;

import com.idra.gestionpeluqueria.model.Cliente;
import com.idra.gestionpeluqueria.exception.ServiceException;
import java.util.List;

public interface ClienteService {
    void crearCliente(Cliente cliente) throws ServiceException;
    Cliente buscarClientePorId(int id) throws ServiceException;
    List<Cliente> listarTodos() throws ServiceException;
    List<Cliente> buscarPorNombre(String nombre) throws ServiceException;
    void actualizarCliente(Cliente cliente) throws ServiceException;
    void eliminarCliente(int id) throws ServiceException;
    boolean validarCliente(Cliente cliente) throws ServiceException; // ✅ SOLO DECLARACIÓN
}