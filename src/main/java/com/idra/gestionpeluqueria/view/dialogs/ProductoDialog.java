package com.idra.gestionpeluqueria.view.dialogs;

import com.idra.gestionpeluqueria.model.Producto;
import com.idra.gestionpeluqueria.controller.ProductoController;
import com.idra.gestionpeluqueria.exception.ServiceException;
import javax.swing.*;
import java.awt.*;

/**
 * Dialogo para crear y editar productos del e-commerce.
 * Proporciona un formulario con validaciones para ingresar 
 * la informacion de un producto nuevo o modificar uno existente.
 * 
 * @author Idra
 */
public class ProductoDialog extends JDialog {
    private JTextField txtNombre, txtDescripcion, txtPrecio, txtStock, txtCategoria;
    private JCheckBox chkActivo;
    private JButton btnGuardar, btnCancelar;
    private final ProductoController productoController;
    private final Producto productoEditar;
    private boolean guardadoExitoso;
    
    /**
     * Constructor que crea el diálogo para agregar o editar un producto.
     * 
     * @param parent El frame padre del diálogo
     * @param titulo El título a mostrar en el diálogo
     * @param productoEditar El producto a editar, o null para crear uno nuevo
     */
    public ProductoDialog(JFrame parent, String titulo, Producto productoEditar) {
        super(parent, titulo, true);
        this.productoEditar = productoEditar;
        this.productoController = new ProductoController();
        this.guardadoExitoso = false;
        
        initializeUI();
        if (productoEditar != null) {
            cargarDatosProducto();
        }
    }

    private void initializeUI() {
        setSize(450, 400);
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
        formPanel.setBorder(BorderFactory.createTitledBorder("Datos del Producto"));
        
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
        
        // Descripción
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Descripción:"), gbc);
        gbc.gridx = 1;
        txtDescripcion = new JTextField(20);
        formPanel.add(txtDescripcion, gbc);
        
        // Categoría (ID)
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Categoría (ID):"), gbc);
        gbc.gridx = 1;
        txtCategoria = new JTextField(20);
        txtCategoria.setToolTipText("Ingrese el ID numérico de la categoría (1=Electrónicos, 2=Ropa, etc.)");
        formPanel.add(txtCategoria, gbc);
        
        // Precio Unitario
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Precio Unitario:"), gbc);
        gbc.gridx = 1;
        txtPrecio = new JTextField(20);
        formPanel.add(txtPrecio, gbc);
        
        // Stock
        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(new JLabel("Stock:"), gbc);
        gbc.gridx = 1;
        txtStock = new JTextField(20);
        formPanel.add(txtStock, gbc);
        
        // Activo
        gbc.gridx = 0; gbc.gridy = 5;
        formPanel.add(new JLabel("Activo:"), gbc);
        gbc.gridx = 1;
        chkActivo = new JCheckBox();
        chkActivo.setSelected(true);
        formPanel.add(chkActivo, gbc);
        
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
        
        btnGuardar.addActionListener(e -> guardarProducto());
        btnCancelar.addActionListener(e -> dispose());
        
        buttonPanel.add(btnCancelar);
        buttonPanel.add(btnGuardar);
        
        return buttonPanel;
    }

    private void cargarDatosProducto() {
        if (productoEditar != null) {
            txtNombre.setText(productoEditar.getNombre());
            txtDescripcion.setText(productoEditar.getDescripcion());
            txtCategoria.setText(String.valueOf(productoEditar.getIdCategoria()));
            txtPrecio.setText(String.valueOf(productoEditar.getPrecioUnitario()));
            txtStock.setText(String.valueOf(productoEditar.getStock()));
            chkActivo.setSelected(productoEditar.isActivo());
        }
    }

    private void guardarProducto() {
        try {
            if (!validarCampos()) {
                return;
            }
            
            if (productoEditar == null) {
                // Crear nuevo producto
                Producto nuevoProducto = new Producto(
                    txtNombre.getText().trim(),
                    txtDescripcion.getText().trim(),
                    Integer.parseInt(txtCategoria.getText().trim()),
                    Double.parseDouble(txtPrecio.getText().trim()),
                    Integer.parseInt(txtStock.getText().trim())
                );
                nuevoProducto.setActivo(chkActivo.isSelected());
                
                productoController.crearProducto(nuevoProducto);
                
                JOptionPane.showMessageDialog(this, 
                    "Producto creado exitosamente!", 
                    "Éxito", 
                    JOptionPane.INFORMATION_MESSAGE);
            } else {
                // Actualizar producto existente
                productoEditar.setNombre(txtNombre.getText().trim());
                productoEditar.setDescripcion(txtDescripcion.getText().trim());
                productoEditar.setIdCategoria(Integer.parseInt(txtCategoria.getText().trim()));
                productoEditar.setPrecioUnitario(Double.parseDouble(txtPrecio.getText().trim()));
                productoEditar.setStock(Integer.parseInt(txtStock.getText().trim()));
                productoEditar.setActivo(chkActivo.isSelected());
                
                productoController.actualizarProducto(productoEditar);
                
                JOptionPane.showMessageDialog(this, 
                    "Producto actualizado exitosamente!", 
                    "Éxito", 
                    JOptionPane.INFORMATION_MESSAGE);
            }
            
            guardadoExitoso = true;
            dispose();
            
        } catch (ServiceException | NumberFormatException ex) {
        JOptionPane.showMessageDialog(this, 
            "Error: " + ex.getMessage(), 
            "Error", 
            JOptionPane.ERROR_MESSAGE);
    }
} // ← Esta llave cierra el método guardarProducto()

    private boolean validarCampos() {
        if (txtNombre.getText().trim().isEmpty()) {
            mostrarError("El nombre es obligatorio");
            txtNombre.requestFocus();
            return false;
        }
        
        if (txtDescripcion.getText().trim().isEmpty()) {
            mostrarError("La descripción es obligatoria");
            txtDescripcion.requestFocus();
            return false;
        }
        
        try {
            int categoria = Integer.parseInt(txtCategoria.getText().trim());
            if (categoria <= 0) {
                mostrarError("La categoría debe ser un número mayor a 0");
                txtCategoria.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            mostrarError("La categoría debe ser un número válido");
            txtCategoria.requestFocus();
            return false;
        }
        
        try {
            double precio = Double.parseDouble(txtPrecio.getText().trim());
            if (precio <= 0) {
                mostrarError("El precio debe ser mayor a 0");
                txtPrecio.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            mostrarError("El precio debe ser un número válido");
            txtPrecio.requestFocus();
            return false;
        }
        
        try {
            int stock = Integer.parseInt(txtStock.getText().trim());
            if (stock < 0) {
                mostrarError("El stock no puede ser negativo");
                txtStock.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            mostrarError("El stock debe ser un número válido");
            txtStock.requestFocus();
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
     * Verifica si el producto fue guardado exitosamente.
     * 
     * @return true si el guardado fue exitoso, false en caso contrario
     */
    public boolean isGuardadoExitoso() {
        return guardadoExitoso;
    }
}