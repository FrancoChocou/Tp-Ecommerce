package com.idra.gestionpeluqueria.view.dialogs;

import com.idra.gestionpeluqueria.controller.CategoriaController;
import com.idra.gestionpeluqueria.controller.ProductoController;
import com.idra.gestionpeluqueria.exception.ServiceException;
import com.idra.gestionpeluqueria.model.Producto;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ProductoDialog extends JDialog {
    private JTextField txtNombre;
    private JTextArea txtDescripcion;
    private JComboBox<String> comboCategoria;
    private JTextField txtPrecio;
    private JTextField txtStock;
    private JCheckBox chkActivo;
    private JButton btnGuardar;
    private JButton btnCancelar;
    
    private Producto producto;
    private boolean guardadoExitoso;
    private CategoriaController categoriaController;

    public ProductoDialog(JFrame parent, String titulo, Producto producto) {
        super(parent, titulo, true);
        this.producto = producto;
        this.guardadoExitoso = false;
        this.categoriaController = new CategoriaController();
        
        initializeUI();
        cargarCategorias();
        if (producto != null) {
            cargarDatosProducto();
        }
        
        pack();
        setLocationRelativeTo(parent);
        setResizable(false);
    }

    private void initializeUI() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        
        // Panel principal
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(Color.WHITE);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        
        // Título
        JLabel lblTitulo = new JLabel(getTitle());
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitulo.setForeground(new Color(50, 50, 50));
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        mainPanel.add(lblTitulo, gbc);
        
        // Separador
        JSeparator separator = new JSeparator();
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(separator, gbc);
        
        // Campos del formulario
        gbc.gridwidth = 1;
        gbc.insets = new Insets(10, 5, 5, 5);
        
        // Nombre
        gbc.gridy = 2;
        gbc.gridx = 0;
        mainPanel.add(new JLabel("Nombre:"), gbc);
        
        txtNombre = new JTextField(20);
        gbc.gridx = 1;
        mainPanel.add(txtNombre, gbc);
        
        // Descripción
        gbc.gridy = 3;
        gbc.gridx = 0;
        mainPanel.add(new JLabel("Descripción:"), gbc);
        
        txtDescripcion = new JTextArea(3, 20);
        txtDescripcion.setLineWrap(true);
        txtDescripcion.setWrapStyleWord(true);
        JScrollPane scrollDescripcion = new JScrollPane(txtDescripcion);
        gbc.gridx = 1;
        mainPanel.add(scrollDescripcion, gbc);
        
        // Categoría (COMBOBOX EN LUGAR DE TEXTFIELD)
        gbc.gridy = 4;
        gbc.gridx = 0;
        mainPanel.add(new JLabel("Categoría:"), gbc);
        
        comboCategoria = new JComboBox<>();
        comboCategoria.setPreferredSize(new Dimension(200, 25));
        gbc.gridx = 1;
        mainPanel.add(comboCategoria, gbc);
        
        // Precio
        gbc.gridy = 5;
        gbc.gridx = 0;
        mainPanel.add(new JLabel("Precio:"), gbc);
        
        txtPrecio = new JTextField(10);
        gbc.gridx = 1;
        mainPanel.add(txtPrecio, gbc);
        
        // Stock
        gbc.gridy = 6;
        gbc.gridx = 0;
        mainPanel.add(new JLabel("Stock:"), gbc);
        
        txtStock = new JTextField(10);
        gbc.gridx = 1;
        mainPanel.add(txtStock, gbc);
        
        // Activo
        gbc.gridy = 7;
        gbc.gridx = 0;
        mainPanel.add(new JLabel("Estado:"), gbc);
        
        chkActivo = new JCheckBox("Producto activo");
        chkActivo.setSelected(true);
        chkActivo.setBackground(Color.WHITE);
        gbc.gridx = 1;
        mainPanel.add(chkActivo, gbc);
        
        // Panel de botones
        gbc.gridy = 8;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(Color.WHITE);
        
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
        
        mainPanel.add(buttonPanel, gbc);
        
        add(mainPanel, BorderLayout.CENTER);
    }

    private void cargarCategorias() {
        try {
            comboCategoria.removeAllItems();
            
            List<String> categorias = categoriaController.listarNombresCategorias();
            for (String categoria : categorias) {
                comboCategoria.addItem(categoria);
            }
            
            // Seleccionar la primera categoría por defecto
            if (comboCategoria.getItemCount() > 0) {
                comboCategoria.setSelectedIndex(0);
            }
            
        } catch (ServiceException e) {
            JOptionPane.showMessageDialog(this,
                "Error al cargar categorías: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
            
            // Fallback con categorías por defecto
            comboCategoria.addItem("Electrodomésticos");
            comboCategoria.addItem("Tecnología");
            comboCategoria.addItem("Hogar");
            comboCategoria.addItem("Deportes");
            comboCategoria.addItem("Moda");
        }
    }

    private void cargarDatosProducto() {
        if (producto != null) {
            txtNombre.setText(producto.getNombre());
            txtDescripcion.setText(producto.getDescripcion());
            txtPrecio.setText(String.valueOf(producto.getPrecioUnitario()));
            txtStock.setText(String.valueOf(producto.getStock()));
            chkActivo.setSelected(producto.isActivo());
            
            // Establecer la categoría correcta en el ComboBox
            try {
                String nombreCategoria = categoriaController.obtenerNombreCategoriaPorId(producto.getIdCategoria());
                comboCategoria.setSelectedItem(nombreCategoria);
            } catch (ServiceException e) {
                // Si hay error, dejar la primera categoría seleccionada
                System.err.println("Error al cargar categoría del producto: " + e.getMessage());
            }
        }
    }

    private class GuardarActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (validarCampos()) {
                guardarProducto();
            }
        }
    }

    private boolean validarCampos() {
        // Validar nombre
        if (txtNombre.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "El nombre del producto es obligatorio.",
                "Validación",
                JOptionPane.WARNING_MESSAGE);
            txtNombre.requestFocus();
            return false;
        }
        
        // Validar precio
        try {
            double precio = Double.parseDouble(txtPrecio.getText().trim());
            if (precio <= 0) {
                JOptionPane.showMessageDialog(this,
                    "El precio debe ser mayor a 0.",
                    "Validación",
                    JOptionPane.WARNING_MESSAGE);
                txtPrecio.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                "El precio debe ser un número válido.",
                "Validación",
                JOptionPane.WARNING_MESSAGE);
            txtPrecio.requestFocus();
            return false;
        }
        
        // Validar stock
        try {
            int stock = Integer.parseInt(txtStock.getText().trim());
            if (stock < 0) {
                JOptionPane.showMessageDialog(this,
                    "El stock no puede ser negativo.",
                    "Validación",
                    JOptionPane.WARNING_MESSAGE);
                txtStock.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                "El stock debe ser un número entero válido.",
                "Validación",
                JOptionPane.WARNING_MESSAGE);
            txtStock.requestFocus();
            return false;
        }
        
        return true;
    }

    private void guardarProducto() {
        try {
            ProductoController productoController = new ProductoController();
            
            if (producto == null) {
                // Nuevo producto
                producto = new Producto();
            }
            
            // Obtener datos del formulario
            producto.setNombre(txtNombre.getText().trim());
            producto.setDescripcion(txtDescripcion.getText().trim());
            
            // Obtener ID de la categoría seleccionada
            String categoriaSeleccionada = (String) comboCategoria.getSelectedItem();
            int idCategoria = categoriaController.obtenerIdCategoriaPorNombre(categoriaSeleccionada);
            producto.setIdCategoria(idCategoria);
            
            producto.setPrecioUnitario(Double.parseDouble(txtPrecio.getText().trim()));
            producto.setStock(Integer.parseInt(txtStock.getText().trim()));
            producto.setActivo(chkActivo.isSelected());
            
            // Guardar producto
            if (producto.getId() == 0) {
                productoController.crearProducto(producto);
            } else {
                productoController.actualizarProducto(producto);
            }
            
            guardadoExitoso = true;
            dispose();
            
        } catch (ServiceException e) {
            JOptionPane.showMessageDialog(this,
                "Error al guardar producto: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error inesperado: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean isGuardadoExitoso() {
        return guardadoExitoso;
    }
}