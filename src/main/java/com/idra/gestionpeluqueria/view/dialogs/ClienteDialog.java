package com.idra.gestionpeluqueria.view.dialogs;

import com.idra.gestionpeluqueria.controller.ClienteController;
import com.idra.gestionpeluqueria.controller.ZonaController;
import com.idra.gestionpeluqueria.model.Cliente;
import com.idra.gestionpeluqueria.model.Zona;
import com.idra.gestionpeluqueria.exception.ServiceException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ClienteDialog extends JDialog {
    private JTextField txtNombre;
    private JTextField txtEmail;
    private JTextField txtTelefono;
    private JTextField txtApellido;
    private JComboBox<String> comboCiudad;
    private JComboBox<String> comboProvincia;
    private JButton btnAgregarZona;
    private JCheckBox chkActivo;
    private JButton btnGuardar;
    private JButton btnCancelar;
    
    private Cliente cliente;
    private boolean guardadoExitoso;
    private ZonaController zonaController;
    private ClienteController clienteController;

   public ClienteDialog(JFrame parent, String titulo, Cliente cliente) {
    super(parent, titulo, true);
    this.cliente = cliente;  // ✅ Esto asigna el cliente completo
    this.guardadoExitoso = false;
    this.zonaController = new ZonaController();
    this.clienteController = new ClienteController();
    
    initializeUI();
    cargarZonas();
    if (cliente != null) {
        cargarDatosCliente();  // ✅ Esto cargará todos los datos del cliente
    }
    
    pack();
    setLocationRelativeTo(parent);
    setResizable(false);
}
   private void initializeUI() {
    setLayout(new BorderLayout(10, 10));
    setBackground(Color.WHITE);
    
    // Panel principal con BoxLayout para mejor control
    JPanel mainPanel = new JPanel();
    mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
    mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    mainPanel.setBackground(Color.WHITE);
    
    // Título
    JLabel lblTitulo = new JLabel(getTitle());
    lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
    lblTitulo.setForeground(new Color(50, 50, 50));
    lblTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);
    
    // Separador
    JSeparator separator = new JSeparator();
    separator.setAlignmentX(Component.LEFT_ALIGNMENT);
    
    // Panel de campos del formulario con GridLayout
    JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));
    formPanel.setBackground(Color.WHITE);
    formPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
    formPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
    
    // ✅ CAMPOS ORGANIZADOS CORRECTAMENTE
    // Nombre
    formPanel.add(new JLabel("Nombre:"));
    txtNombre = new JTextField();
    formPanel.add(txtNombre);
    
    // Apellido
    formPanel.add(new JLabel("Apellido:"));
    txtApellido = new JTextField();
    formPanel.add(txtApellido);
    
    // Email
    formPanel.add(new JLabel("Email:"));
    txtEmail = new JTextField();
    formPanel.add(txtEmail);
    
    // Teléfono
    formPanel.add(new JLabel("Teléfono:"));
    txtTelefono = new JTextField();
    formPanel.add(txtTelefono);
    
    // Ciudad
    formPanel.add(new JLabel("Ciudad:"));
    comboCiudad = new JComboBox<>();
    formPanel.add(comboCiudad);
    
    // Provincia
    formPanel.add(new JLabel("Provincia:"));
    comboProvincia = new JComboBox<>();
    formPanel.add(comboProvincia);
    
    // Botón para agregar nueva zona
    btnAgregarZona = new JButton("➕ Agregar Nueva Ciudad/Provincia");
    btnAgregarZona.setBackground(new Color(155, 89, 182));
    btnAgregarZona.setForeground(Color.WHITE);
    btnAgregarZona.setFocusPainted(false);
    btnAgregarZona.setAlignmentX(Component.LEFT_ALIGNMENT);
    btnAgregarZona.addActionListener(e -> agregarNuevaZona());
    
    // Checkbox activo
    chkActivo = new JCheckBox("Cliente activo");
    chkActivo.setSelected(true);
    chkActivo.setBackground(Color.WHITE);
    chkActivo.setAlignmentX(Component.LEFT_ALIGNMENT);
    
    // Panel de botones
    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
    buttonPanel.setBackground(Color.WHITE);
    buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
    
    btnGuardar = new JButton("Guardar");
    btnGuardar.setBackground(new Color(39, 174, 96));
    btnGuardar.setForeground(Color.WHITE);
    btnGuardar.setFocusPainted(false);
    
    btnCancelar = new JButton("Cancelar");
    btnCancelar.setBackground(new Color(231, 76, 60));
    btnCancelar.setForeground(Color.WHITE);
    btnCancelar.setFocusPainted(false);
    
    btnGuardar.addActionListener(new GuardarActionListener());
    btnCancelar.addActionListener(e -> dispose());
    
    buttonPanel.add(btnGuardar);
    buttonPanel.add(btnCancelar);
    
    // ✅ ENSAMBLAR TODOS LOS COMPONENTES EN ORDEN
    mainPanel.add(lblTitulo);
    mainPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Espacio
    mainPanel.add(separator);
    mainPanel.add(Box.createRigidArea(new Dimension(0, 15))); // Espacio
    mainPanel.add(formPanel);
    mainPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Espacio
    mainPanel.add(btnAgregarZona);
    mainPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Espacio
    mainPanel.add(chkActivo);
    mainPanel.add(Box.createRigidArea(new Dimension(0, 15))); // Espacio
    mainPanel.add(buttonPanel);
    
    add(mainPanel, BorderLayout.CENTER);
    
    // ✅ TAMAÑO PREFERIDO PARA MEJOR VISUALIZACIÓN
    setPreferredSize(new Dimension(500, 500));
}
    private void cargarZonas() {
        try {
            comboCiudad.removeAllItems();
            comboProvincia.removeAllItems();
            
            // Cargar ciudades
            List<String> ciudades = zonaController.listarCiudades();
            for (String ciudad : ciudades) {
                comboCiudad.addItem(ciudad);
            }
            
            // Cargar provincias
            List<String> provincias = zonaController.listarProvincias();
            for (String provincia : provincias) {
                comboProvincia.addItem(provincia);
            }
            
            // Seleccionar primeros valores por defecto
            if (comboCiudad.getItemCount() > 0) {
                comboCiudad.setSelectedIndex(0);
            }
            if (comboProvincia.getItemCount() > 0) {
                comboProvincia.setSelectedIndex(0);
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error al cargar zonas: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
            
            // Valores por defecto en caso de error
            comboCiudad.addItem("Buenos Aires");
            comboCiudad.addItem("Córdoba");
            comboCiudad.addItem("Rosario");
            comboCiudad.addItem("Mendoza");
            
            comboProvincia.addItem("Buenos Aires");
            comboProvincia.addItem("Córdoba");
            comboProvincia.addItem("Santa Fe");
            comboProvincia.addItem("Mendoza");
        }
    }

  private void cargarDatosCliente() {
    if (cliente != null) {
        // ✅ Cargar datos básicos directamente del objeto Cliente
        txtNombre.setText(cliente.getNombre() != null ? cliente.getNombre() : "");
        txtApellido.setText(cliente.getApellido() != null ? cliente.getApellido() : "");
        txtEmail.setText(cliente.getEmail() != null ? cliente.getEmail() : "");
        txtTelefono.setText(cliente.getTelefono() != null ? cliente.getTelefono() : "");
        chkActivo.setSelected(cliente.isActivo());
        
        // ✅ Cargar zona si existe
        if (cliente.getIdZona() > 0) {
            try {
                Zona zona = zonaController.buscarZonaPorId(cliente.getIdZona());
                if (zona != null) {
                    // Seleccionar la ciudad y provincia correctas
                    seleccionarZonaEnCombos(zona.getCiudad(), zona.getProvincia());
                }
            } catch (Exception e) {
                System.err.println("Error al cargar zona del cliente: " + e.getMessage());
            }
        }
    }
}

// ✅ Método auxiliar para seleccionar zona en los combos
private void seleccionarZonaEnCombos(String ciudad, String provincia) {
    // Buscar ciudad en el combo
    for (int i = 0; i < comboCiudad.getItemCount(); i++) {
        if (comboCiudad.getItemAt(i).equals(ciudad)) {
            comboCiudad.setSelectedIndex(i);
            break;
        }
    }
    
    // Buscar provincia en el combo
    for (int i = 0; i < comboProvincia.getItemCount(); i++) {
        if (comboProvincia.getItemAt(i).equals(provincia)) {
            comboProvincia.setSelectedIndex(i);
            break;
        }
    }
}
    private void agregarNuevaZona() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JTextField txtNuevaCiudad = new JTextField(15);
        JTextField txtNuevaProvincia = new JTextField(15);
        
        panel.add(new JLabel("Ciudad:"));
        panel.add(txtNuevaCiudad);
        panel.add(new JLabel("Provincia:"));
        panel.add(txtNuevaProvincia);
        
        int result = JOptionPane.showConfirmDialog(this,
            panel,
            "Agregar Nueva Ciudad/Provincia",
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.PLAIN_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION) {
            String nuevaCiudad = txtNuevaCiudad.getText().trim();
            String nuevaProvincia = txtNuevaProvincia.getText().trim();
            
            if (nuevaCiudad.isEmpty() || nuevaProvincia.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "Por favor complete ambos campos (ciudad y provincia)",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            try {
                // Verificar si ya existe
                boolean existe = zonaController.existeZona(nuevaCiudad, nuevaProvincia);
                if (existe) {
                    JOptionPane.showMessageDialog(this,
                        "La combinación Ciudad/Provincia ya existe en el sistema",
                        "Zona Existente",
                        JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                // Crear la nueva zona
                Zona nuevaZona = new Zona(nuevaCiudad, nuevaProvincia);
                zonaController.crearZona(nuevaZona);
                
                // Agregar a los combos si no existen
                boolean ciudadExiste = false;
                boolean provinciaExiste = false;
                
                for (int i = 0; i < comboCiudad.getItemCount(); i++) {
                    if (comboCiudad.getItemAt(i).equals(nuevaCiudad)) {
                        ciudadExiste = true;
                        break;
                    }
                }
                
                for (int i = 0; i < comboProvincia.getItemCount(); i++) {
                    if (comboProvincia.getItemAt(i).equals(nuevaProvincia)) {
                        provinciaExiste = true;
                        break;
                    }
                }
                
                if (!ciudadExiste) {
                    comboCiudad.addItem(nuevaCiudad);
                }
                if (!provinciaExiste) {
                    comboProvincia.addItem(nuevaProvincia);
                }
                
                // Seleccionar la nueva zona
                comboCiudad.setSelectedItem(nuevaCiudad);
                comboProvincia.setSelectedItem(nuevaProvincia);
                
                JOptionPane.showMessageDialog(this,
                    "Zona agregada correctamente:\n" + nuevaCiudad + ", " + nuevaProvincia,
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);
                    
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                    "Error al agregar zona: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public boolean isGuardadoExitoso() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    private class GuardarActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (validarCampos()) {
                guardarCliente();
            }
        }
    }

    private boolean validarCampos() {
        // Validar nombre
        if (txtNombre.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "El nombre del cliente es obligatorio.",
                "Validación",
                JOptionPane.WARNING_MESSAGE);
            txtNombre.requestFocus();
            return false;
        }
        
        // Validar email
        String email = txtEmail.getText().trim();
        if (email.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "El email es obligatorio.",
                "Validación",
                JOptionPane.WARNING_MESSAGE);
            txtEmail.requestFocus();
            return false;
        }
        
        // Validar formato de email básico
        if (!email.contains("@") || !email.contains(".")) {
            JOptionPane.showMessageDialog(this,
                "Por favor ingrese un email válido.",
                "Validación",
                JOptionPane.WARNING_MESSAGE);
            txtEmail.requestFocus();
            return false;
        }
        
        // Validar teléfono
        if (txtTelefono.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "El teléfono es obligatorio.",
                "Validación",
                JOptionPane.WARNING_MESSAGE);
            txtTelefono.requestFocus();
            return false;
        }
        
        // Validar que se haya seleccionado ciudad y provincia
        if (comboCiudad.getSelectedItem() == null || comboProvincia.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this,
                "Por favor seleccione una ciudad y provincia.",
                "Validación",
                JOptionPane.WARNING_MESSAGE);
            return false;
        }
        
        return true;
    }

    private void guardarCliente() {
    try {
        if (cliente == null) {
            cliente = new Cliente();
        }
        
        // Obtener datos del formulario
        cliente.setNombre(txtNombre.getText().trim());
        cliente.setApellido(txtApellido.getText().trim());
        cliente.setEmail(txtEmail.getText().trim());
        cliente.setTelefono(txtTelefono.getText().trim());
        cliente.setEdad(0); // ✅ Establecer edad en 0 por defecto
        cliente.setActivo(chkActivo.isSelected());
        
        // Obtener y guardar la zona
        String ciudad = (String) comboCiudad.getSelectedItem();
        String provincia = (String) comboProvincia.getSelectedItem();
        
        if (ciudad != null && provincia != null) {
            // Buscar o crear la zona
            Zona zona = zonaController.buscarZonaPorCiudadYProvincia(ciudad, provincia);
            if (zona == null) {
                // Si no existe, crear nueva zona
                zona = new Zona(ciudad, provincia);
                zonaController.crearZona(zona);
            }
            cliente.setIdZona(zona.getId());
        }
        
        // Guardar cliente
        if (cliente.getId() == 0) {
            clienteController.crearCliente(cliente);
        } else {
            clienteController.actualizarCliente(cliente);
        }
        
        guardadoExitoso = true;
        dispose();
        
    } catch (ServiceException e) {
        JOptionPane.showMessageDialog(this,
            "Error al guardar cliente: " + e.getMessage(),
            "Error",
            JOptionPane.ERROR_MESSAGE);
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this,
            "Error inesperado: " + e.getMessage(),
            "Error",
            JOptionPane.ERROR_MESSAGE);
    }
}
}