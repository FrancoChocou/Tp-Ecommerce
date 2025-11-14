package com.idra.gestionpeluqueria.view.dialogs;

import com.idra.gestionpeluqueria.model.Cliente;
import com.idra.gestionpeluqueria.controller.ClienteController;
import com.idra.gestionpeluqueria.exception.ServiceException;
import com.idra.gestionpeluqueria.util.Validator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Dialogo para crear y editar clientes.
 * Proporciona un formulario con validaciones para ingresar 
 * la informacion de un cliente nuevo o modificar uno existente.
 * 
 * @author Idra
 */
public class ClienteDialog extends JDialog {
    private JTextField txtNombre, txtApellido, txtTelefono, txtEmail, txtEdad, txtZona;
    private JButton btnGuardar, btnCancelar;
    private ClienteController clienteController;
    private Cliente clienteEditar;
    private boolean guardadoExitoso;
    
    /**
     * Constructor que crea el diálogo para agregar o editar un cliente.
     * 
     * @param parent El frame padre del diálogo
     * @param titulo El título a mostrar en el diálogo
     * @param clienteEditar El cliente a editar, o null para crear uno nuevo
     */
    
    public ClienteDialog(JFrame parent, String titulo, Cliente clienteEditar) {
        super(parent, titulo, true);
        this.clienteEditar = clienteEditar;
        this.clienteController = new ClienteController();
        this.guardadoExitoso = false;
        
        initializeUI();
        if (clienteEditar != null) {
            cargarDatosCliente();
        }
    }

    private void initializeUI() {
        setSize(450, 400); // Aumenté el tamaño para los nuevos campos
        setLocationRelativeTo(getParent());
        setResizable(false);
        setLayout(new BorderLayout(10, 10));
        
        // Panel principal con padding
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Formulario
        JPanel formPanel = createFormPanel();
        // Botones
        JPanel buttonPanel = createButtonPanel();
        
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }

    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Datos del Cliente"));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        
        // Nombre
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1;
        txtNombre = new JTextField(20);
        formPanel.add(txtNombre, gbc);
        
        // Apellido
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Apellido:"), gbc);
        gbc.gridx = 1;
        txtApellido = new JTextField(20);
        formPanel.add(txtApellido, gbc);
        
        // Teléfono
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Teléfono:"), gbc);
        gbc.gridx = 1;
        txtTelefono = new JTextField(20);
        formPanel.add(txtTelefono, gbc);
        
        // Email
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        txtEmail = new JTextField(20);
        formPanel.add(txtEmail, gbc);
        
        // Edad - NUEVO CAMPO
        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(new JLabel("Edad:"), gbc);
        gbc.gridx = 1;
        txtEdad = new JTextField(20);
        formPanel.add(txtEdad, gbc);
        
        // Zona - NUEVO CAMPO
       gbc.gridx = 0; gbc.gridy = 5;
formPanel.add(new JLabel("Ciudad:"), gbc); // ← CAMBIADO DEFINITIVAMENTE
gbc.gridx = 1;
txtZona = new JTextField(20);
txtZona.setToolTipText("Ingrese la ciudad del cliente (solo letras y números)");
formPanel.add(txtZona, gbc);
        
        return formPanel;
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        btnGuardar = new JButton("💾 Guardar");
        btnCancelar = new JButton("❌ Cancelar");
        
        // Estilos de botones
        btnGuardar.setBackground(new Color(39, 174, 96));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFocusPainted(false);
        
        btnCancelar.setBackground(new Color(231, 76, 60));
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setFocusPainted(false);
        
        // Event listeners
        btnGuardar.addActionListener(e -> guardarCliente());
        btnCancelar.addActionListener(e -> dispose());
        
        buttonPanel.add(btnCancelar);
        buttonPanel.add(btnGuardar);
        
        return buttonPanel;
    }

    private void cargarDatosCliente() {
        if (clienteEditar != null) {
            txtNombre.setText(clienteEditar.getNombre());
            txtApellido.setText(clienteEditar.getApellido());
            txtTelefono.setText(clienteEditar.getTelefono());
            txtEmail.setText(clienteEditar.getEmail() != null ? clienteEditar.getEmail() : "");
            txtEdad.setText(String.valueOf(clienteEditar.getEdad()));
            txtZona.setText(String.valueOf(clienteEditar.getIdZona()));
        }
    }

    private void guardarCliente() {
        try {
            if (!validarCampos()) {
                return;
            }
            
            if (clienteEditar == null) {
                // Crear nuevo cliente
                Cliente nuevoCliente = new Cliente(
                    txtNombre.getText().trim(),
                    txtApellido.getText().trim(),
                    txtTelefono.getText().trim(),
                    txtEmail.getText().trim(),
                    Integer.parseInt(txtEdad.getText().trim()),
                    Integer.parseInt(txtZona.getText().trim())
                );
                
                ClienteController controller = new ClienteController();
                controller.crearCliente(nuevoCliente);
                
                JOptionPane.showMessageDialog(this, 
                    "Cliente creado exitosamente!", 
                    "Éxito", 
                    JOptionPane.INFORMATION_MESSAGE);
            } else {
                // Actualizar cliente existente
                clienteEditar.setNombre(txtNombre.getText().trim());
                clienteEditar.setApellido(txtApellido.getText().trim());
                clienteEditar.setTelefono(txtTelefono.getText().trim());
                clienteEditar.setEmail(txtEmail.getText().trim());
                clienteEditar.setEdad(Integer.parseInt(txtEdad.getText().trim()));
                clienteEditar.setIdZona(Integer.parseInt(txtZona.getText().trim()));
                
                ClienteController controller = new ClienteController();
                controller.actualizarCliente(clienteEditar);
                
                JOptionPane.showMessageDialog(this, 
                    "Cliente actualizado exitosamente!", 
                    "Éxito", 
                    JOptionPane.INFORMATION_MESSAGE);
            }
            
            guardadoExitoso = true;
            dispose();
            
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this, 
                "Error: " + ex.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, 
                "Error: La edad y zona deben ser números válidos", 
                "Error de formato", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean validarCampos() {
    if (!Validator.isNotEmpty(txtNombre.getText())) {
        mostrarError("El nombre es obligatorio");
        txtNombre.requestFocus();
        return false;
    }
    
    if (!Validator.isValidName(txtNombre.getText())) {
        mostrarError("El nombre solo puede contener letras y espacios");
        txtNombre.requestFocus();
        return false;
    }
    
    if (!Validator.isNotEmpty(txtApellido.getText())) {
        mostrarError("El apellido es obligatorio");
        txtApellido.requestFocus();
        return false;
    }
    
    if (!Validator.isValidName(txtApellido.getText())) {
        mostrarError("El apellido solo puede contener letras y espacios");
        txtApellido.requestFocus();
        return false;
    }
    
    if (!Validator.isValidPhone(txtTelefono.getText())) {
        mostrarError("El teléfono debe tener entre 10 y 15 dígitos");
        txtTelefono.requestFocus();
        return false;
    }
    
    String email = txtEmail.getText().trim();
    if (!email.isEmpty() && !Validator.isValidEmail(email)) {
        mostrarError("El formato del email no es válido");
        txtEmail.requestFocus();
        return false;
    }
    
    // Validación para edad
    try {
        int edad = Integer.parseInt(txtEdad.getText().trim());
        if (edad < 0 || edad > 120) {
            mostrarError("La edad debe estar entre 0 y 120 años");
            txtEdad.requestFocus();
            return false;
        }
    } catch (NumberFormatException e) {
        mostrarError("La edad debe ser un número válido");
        txtEdad.requestFocus();
        return false;
    }
    
    // VALIDACIÓN MODIFICADA PARA CIUDAD - SOLO LETRAS Y NÚMEROS
    String ciudad = txtZona.getText().trim();
    if (ciudad.isEmpty()) {
        mostrarError("La ciudad es obligatoria");
        txtZona.requestFocus();
        return false;
    }
    
    // SOLO PERMITIR LETRAS, NÚMEROS Y ESPACIOS
    if (!ciudad.matches("^[a-zA-Z0-9áéíóúÁÉÍÓÚñÑ\\s]+$")) {
        mostrarError("La ciudad solo puede contener letras, números y espacios");
        txtZona.requestFocus();
        return false;
    }
    
    return true;
}

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, 
            mensaje, 
            "Error de Validación", 
            JOptionPane.WARNING_MESSAGE);
    }
    
    /**
     * Verifica si el cliente fue guardado exitosamente.
     * 
     * @return true si el guardado fue exitoso, false en caso contrario
     */
    public boolean isGuardadoExitoso() {
        return guardadoExitoso;
    }
    
    /**
     * Obtiene el cliente que fue creado o editado.
     * 
     * @return El objeto Cliente con los datos actualizados
     */
    public Cliente getCliente() {
        return clienteEditar;
    }
}