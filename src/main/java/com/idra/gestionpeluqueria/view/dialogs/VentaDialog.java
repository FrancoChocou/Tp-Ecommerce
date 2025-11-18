package com.idra.gestionpeluqueria.view.dialogs;

import com.idra.gestionpeluqueria.model.Venta;
import com.idra.gestionpeluqueria.model.Cliente;
import com.idra.gestionpeluqueria.model.Producto;
import com.idra.gestionpeluqueria.controller.VentaController;
import com.idra.gestionpeluqueria.controller.ClienteController;
import com.idra.gestionpeluqueria.controller.ProductoController;
import com.idra.gestionpeluqueria.exception.ServiceException;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Dialogo para crear y editar ventas del e-commerce.
 * Proporciona un formulario completo para gestionar ventas incluyendo 
 * seleccion de cliente, producto, cantidad, precio y método de pago.
 * 
 * @author Idra
 */
public class VentaDialog extends JDialog {
    private JComboBox<Cliente> comboCliente;
    private JComboBox<Producto> comboProducto;
    private JTextField txtCantidad, txtPrecioUnitario, txtTotal;
    private JComboBox<String> comboMetodoPago;
    private JButton btnGuardar, btnCancelar, btnBuscarCliente, btnCalcularTotal;
    private VentaController ventaController;
    private ClienteController clienteController;
    private ProductoController productoController;
    private Venta ventaEditar;
    private boolean guardadoExitoso;
    
    /**
     * Constructor que crea el diálogo para agregar o editar una venta.
     * 
     * @param parent El frame padre del diálogo
     * @param titulo El título a mostrar en el diálogo
     * @param ventaEditar La venta a editar, o null para crear una nueva
     */
    public VentaDialog(JFrame parent, String titulo, Venta ventaEditar) {
        super(parent, titulo, true);
        this.ventaEditar = ventaEditar;
        this.ventaController = new VentaController();
        this.clienteController = new ClienteController();
        this.productoController = new ProductoController();
        this.guardadoExitoso = false;
        
        initializeUI();
        cargarCombos();
        if (ventaEditar != null) {
            cargarDatosVenta();
        } else {
            // Valores por defecto para nueva venta
            txtCantidad.setText("1");
            txtPrecioUnitario.setText("0.00");
            txtTotal.setText("0.00");
            comboMetodoPago.setSelectedIndex(0);
        }
    }

    private void initializeUI() {
        setSize(500, 400);
        setLocationRelativeTo(getParent());
        setResizable(false);
        setLayout(new BorderLayout(10, 10));
        
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JPanel formPanel = createFormPanel();
        JPanel buttonPanel = createButtonPanel();
        
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }

    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Datos de la Venta"));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        
        // Cliente
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Cliente:*"), gbc);
        gbc.gridx = 1;
        JPanel clientePanel = new JPanel(new BorderLayout(5, 0));
        comboCliente = new JComboBox<>();
        clientePanel.add(comboCliente, BorderLayout.CENTER);
        btnBuscarCliente = new JButton("🔍");
        btnBuscarCliente.addActionListener(e -> buscarCliente());
        clientePanel.add(btnBuscarCliente, BorderLayout.EAST);
        formPanel.add(clientePanel, gbc);
        
        // Producto
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Producto:*"), gbc);
        gbc.gridx = 1;
        comboProducto = new JComboBox<>();
        comboProducto.addActionListener(e -> actualizarPrecio());
        formPanel.add(comboProducto, gbc);
        
        // Cantidad
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Cantidad:*"), gbc);
        gbc.gridx = 1;
        txtCantidad = new JTextField();
        txtCantidad.setText("1");
        txtCantidad.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent e) { calcularTotal(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { calcularTotal(); }
            public void insertUpdate(javax.swing.event.DocumentEvent e) { calcularTotal(); }
        });
        formPanel.add(txtCantidad, gbc);
        
        // Precio Unitario
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Precio Unitario:*"), gbc);
        gbc.gridx = 1;
        txtPrecioUnitario = new JTextField();
        txtPrecioUnitario.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent e) { calcularTotal(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { calcularTotal(); }
            public void insertUpdate(javax.swing.event.DocumentEvent e) { calcularTotal(); }
        });
        formPanel.add(txtPrecioUnitario, gbc);
        
        // Total
        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(new JLabel("Total:*"), gbc);
        gbc.gridx = 1;
        JPanel totalPanel = new JPanel(new BorderLayout(5, 0));
        txtTotal = new JTextField();
        txtTotal.setEditable(false);
        txtTotal.setBackground(new Color(240, 240, 240));
        totalPanel.add(txtTotal, BorderLayout.CENTER);
        btnCalcularTotal = new JButton("🔄");
        btnCalcularTotal.addActionListener(e -> calcularTotal());
        totalPanel.add(btnCalcularTotal, BorderLayout.EAST);
        formPanel.add(totalPanel, gbc);
        
        // Método de Pago
        gbc.gridx = 0; gbc.gridy = 5;
        formPanel.add(new JLabel("Método de Pago:*"), gbc);
        gbc.gridx = 1;
        comboMetodoPago = new JComboBox<>(new String[]{"Tarjeta Crédito", "Tarjeta Débito", "Efectivo", "Transferencia", "Mercado Pago"});
        formPanel.add(comboMetodoPago, gbc);
        
        return formPanel;
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        btnGuardar = new JButton("💾 Guardar");
        btnCancelar = new JButton("❌ Cancelar");
        
        btnGuardar.setBackground(new Color(39, 174, 96));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFocusPainted(false);
        
        btnCancelar.setBackground(new Color(231, 76, 60));
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setFocusPainted(false);
        
        btnGuardar.addActionListener(e -> guardarVenta());
        btnCancelar.addActionListener(e -> dispose());
        
        buttonPanel.add(btnCancelar);
        buttonPanel.add(btnGuardar);
        
        return buttonPanel;
    }

    private void cargarCombos() {
        try {
            // Cargar clientes 
            List<Cliente> clientes = clienteController.listarTodos();
            comboCliente.removeAllItems();
            for (Cliente cliente : clientes) {
                comboCliente.addItem(cliente);
            }
            
            // Cargar productos activos
            List<Producto> productos = productoController.buscarProductosActivos();
            comboProducto.removeAllItems();
            for (Producto producto : productos) {
                comboProducto.addItem(producto);
            }
            
        } catch (ServiceException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar datos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarDatosVenta() {
        if (ventaEditar != null) {
            // Seleccionar cliente
            for (int i = 0; i < comboCliente.getItemCount(); i++) {
                if (comboCliente.getItemAt(i).getId() == ventaEditar.getCliente().getId()) {
                    comboCliente.setSelectedIndex(i);
                    break;
                }
            }
            
            // Seleccionar producto
            for (int i = 0; i < comboProducto.getItemCount(); i++) {
                if (comboProducto.getItemAt(i).getId() == ventaEditar.getProducto().getId()) {
                    comboProducto.setSelectedIndex(i);
                    break;
                }
            }
            
            txtCantidad.setText(String.valueOf(ventaEditar.getCantidad()));
            txtPrecioUnitario.setText(String.valueOf(ventaEditar.getPrecioUnitario()));
            txtTotal.setText(String.valueOf(ventaEditar.getTotal()));
            
            // Método de pago 
            String[] metodos = {"Tarjeta Crédito", "Tarjeta Débito", "Efectivo", "Transferencia", "Mercado Pago"};
            if (ventaEditar.getIdMetodoPago() > 0 && ventaEditar.getIdMetodoPago() <= metodos.length) {
                comboMetodoPago.setSelectedItem(metodos[ventaEditar.getIdMetodoPago() - 1]);
            }
        }
    }

    private void buscarCliente() {
        String nombre = JOptionPane.showInputDialog(this, "Buscar cliente por nombre:");
        if (nombre != null && !nombre.trim().isEmpty()) {
            try {
                
                List<Cliente> clientes = clienteController.buscarPorNombre(nombre.trim());
                if (clientes.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "No se encontraron clientes", "Búsqueda", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    // Mostrar diálogo de selección
                    Cliente[] clienteArray = clientes.toArray(new Cliente[0]);
                    Cliente seleccionado = (Cliente) JOptionPane.showInputDialog(
                        this,
                        "Seleccione un cliente:",
                        "Seleccionar Cliente",
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        clienteArray,
                        clienteArray[0]
                    );
                    if (seleccionado != null) {
                        comboCliente.setSelectedItem(seleccionado);
                    }
                }
            } catch (ServiceException e) {
                JOptionPane.showMessageDialog(this, "Error al buscar cliente: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void guardarVenta() {
        try {
            if (!validarCampos()) {
                return;
            }
            
            if (ventaEditar == null) {
                // Crear nueva venta
                Venta nuevaVenta = new Venta(
                    (Cliente) comboCliente.getSelectedItem(),
                    (Producto) comboProducto.getSelectedItem(),
                    Integer.parseInt(txtCantidad.getText().trim()),
                    Double.parseDouble(txtPrecioUnitario.getText().trim()),
                    comboMetodoPago.getSelectedIndex() + 1  // ID del método de pago
                );
                
                ventaController.crearVenta(nuevaVenta);
                
            } else {
                // Actualizar venta existente
                ventaEditar.setCliente((Cliente) comboCliente.getSelectedItem());
                ventaEditar.setProducto((Producto) comboProducto.getSelectedItem());
                ventaEditar.setCantidad(Integer.parseInt(txtCantidad.getText().trim()));
                ventaEditar.setPrecioUnitario(Double.parseDouble(txtPrecioUnitario.getText().trim()));
                ventaEditar.setIdMetodoPago(comboMetodoPago.getSelectedIndex() + 1);
                ventaEditar.setTotal(Double.parseDouble(txtTotal.getText().trim()));
                
                ventaController.actualizarVenta(ventaEditar);
            }
            
            JOptionPane.showMessageDialog(this, 
                ventaEditar == null ? "Venta creada exitosamente!" : "Venta actualizada exitosamente!", 
                "Éxito", 
                JOptionPane.INFORMATION_MESSAGE);
                
            guardadoExitoso = true;
            dispose();
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, 
                "Error: " + ex.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean validarCampos() {
        if (comboCliente.getSelectedItem() == null) {
            mostrarError("Debe seleccionar un cliente");
            return false;
        }
        
        if (comboProducto.getSelectedItem() == null) {
            mostrarError("Debe seleccionar un producto");
            return false;
        }
        
        try {
            int cantidad = Integer.parseInt(txtCantidad.getText().trim());
            if (cantidad <= 0) {
                mostrarError("La cantidad debe ser mayor a 0");
                txtCantidad.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            mostrarError("La cantidad debe ser un número válido");
            txtCantidad.requestFocus();
            return false;
        }
        
        try {
            double precio = Double.parseDouble(txtPrecioUnitario.getText().trim());
            if (precio <= 0) {
                mostrarError("El precio unitario debe ser mayor a 0");
                txtPrecioUnitario.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            mostrarError("El precio unitario debe ser un número válido");
            txtPrecioUnitario.requestFocus();
            return false;
        }
        
        return true;
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error de Validación", JOptionPane.WARNING_MESSAGE);
    }
    
    private void actualizarPrecio() {
        if (comboProducto.getSelectedItem() instanceof Producto) {
            Producto producto = (Producto) comboProducto.getSelectedItem();
            txtPrecioUnitario.setText(String.valueOf(producto.getPrecioUnitario()));
            calcularTotal();
        }
    }
    
    private void calcularTotal() {
        try {
            int cantidad = Integer.parseInt(txtCantidad.getText().trim());
            double precio = Double.parseDouble(txtPrecioUnitario.getText().trim());
            double total = cantidad * precio;
            txtTotal.setText(String.format("%.2f", total));
        } catch (NumberFormatException e) {
            txtTotal.setText("0.00");
        }
    }
    
    /**
     * Verifica si la venta fue guardada exitosamente.
     * 
     * @return true si el guardado fue exitoso, false en caso contrario
     */
    public boolean isGuardadoExitoso() {
        return guardadoExitoso;
    }
}