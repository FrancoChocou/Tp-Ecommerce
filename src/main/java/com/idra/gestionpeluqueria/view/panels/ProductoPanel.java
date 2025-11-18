package com.idra.gestionpeluqueria.view.panels;

import com.idra.gestionpeluqueria.controller.ProductoController;
import com.idra.gestionpeluqueria.exception.ServiceException;
import java.util.List;
import com.idra.gestionpeluqueria.model.Producto;
import com.idra.gestionpeluqueria.view.dialogs.ProductoDialog;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * Panel para la gestion de productos del e-commerce. Proporciona una interfaz
 * para visualizar, agregar, editar, eliminar y buscar productos. Incluye
 * funcionalidades de filtrado por categoría y control de stock.
 *
 * @author Idra
 */
public class ProductoPanel extends JPanel {

    private JTable tablaProductos;
    private DefaultTableModel tableModel;
    private JButton btnAgregar, btnEditar, btnEliminar, btnActivarDesactivar, btnBuscar;
    private JTextField txtBuscar;
    private JComboBox<String> comboFiltroCategoria;
    private ProductoController productoController;

    /**
     * Constructor que inicializa el panel de productos y sus componentes.
     */
    public ProductoPanel() {
        this.productoController = new ProductoController();
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(230, 240, 255)); 
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        createHeaderPanel();
        createTablePanel();
        createToolbar();
        actualizarTabla(); 
    }

    private void createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(230, 240, 255)); 

        JLabel titleLabel = new JLabel("Gestión de Productos");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(50, 50, 50));

        JLabel subtitleLabel = new JLabel("Administre los productos, precios y stock del e-commerce");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(100, 100, 100));

        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(new Color(230, 240, 255)); 
        titlePanel.add(titleLabel, BorderLayout.NORTH);
        titlePanel.add(subtitleLabel, BorderLayout.CENTER);

        headerPanel.add(titlePanel, BorderLayout.WEST);
        add(headerPanel, BorderLayout.NORTH);
    }

    private void createToolbar() {
        JPanel toolbarPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        toolbarPanel.setBackground(new Color(230, 240, 255)); 
        toolbarPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        btnAgregar = createToolbarButton("➕ Agregar Producto", new Color(39, 174, 96));
        btnEditar = createToolbarButton("✏️ Editar", new Color(41, 128, 185));
        btnEliminar = createToolbarButton("🗑️ Eliminar", new Color(231, 76, 60));
        btnActivarDesactivar = createToolbarButton("⚡ Estado", new Color(243, 156, 18));

        btnAgregar.addActionListener(e -> abrirDialogoProducto(null));
        btnEditar.addActionListener(e -> editarProductoSeleccionado());
        btnEliminar.addActionListener(e -> eliminarProductoSeleccionado());
        btnActivarDesactivar.addActionListener(e -> cambiarEstadoProducto());

        // Filtros
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        filterPanel.setBackground(new Color(230, 240, 255)); 

        JLabel lblFiltro = new JLabel("Filtrar por categoría:");
        lblFiltro.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        // Categorias de Electrodomesticos
        String[] categoriasElectrodomesticos = {
            "Todas", "Audio", "Barberia", "Bicicletas", "Camaras", "Celulares",
            "Cocinas", "Colchones", "Freezers", "Heladeras", "Impresoras",
            "Jardin", "Lavarropas", "Notebooks", "Secarropas", "Soportes",
            "Tablets", "Tv"
        };
        comboFiltroCategoria = new JComboBox<>(categoriasElectrodomesticos);
        comboFiltroCategoria.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        comboFiltroCategoria.addActionListener(e -> filtrarPorCategoria());

        // Búsqueda
        JPanel searchPanel = new JPanel(new BorderLayout(5, 0));
        searchPanel.setBackground(new Color(230, 240, 255));

        JLabel lblBuscar = new JLabel("Buscar:");
        lblBuscar.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        txtBuscar = new JTextField(15);
        txtBuscar.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        btnBuscar = createToolbarButton("🔍 Buscar", new Color(155, 89, 182));
        btnBuscar.addActionListener(e -> buscarProductos());

        searchPanel.add(lblBuscar, BorderLayout.WEST);
        searchPanel.add(txtBuscar, BorderLayout.CENTER);
        searchPanel.add(btnBuscar, BorderLayout.EAST);

        filterPanel.add(lblFiltro);
        filterPanel.add(comboFiltroCategoria);
        filterPanel.add(Box.createHorizontalStrut(20));
        filterPanel.add(searchPanel);

        toolbarPanel.add(btnAgregar);
        toolbarPanel.add(Box.createHorizontalStrut(10));
        toolbarPanel.add(btnEditar);
        toolbarPanel.add(Box.createHorizontalStrut(10));
        toolbarPanel.add(btnEliminar);
        toolbarPanel.add(Box.createHorizontalStrut(10));
        toolbarPanel.add(btnActivarDesactivar);
        toolbarPanel.add(Box.createHorizontalStrut(30));
        toolbarPanel.add(filterPanel);

        add(toolbarPanel, BorderLayout.SOUTH);
    }

    private JButton createToolbarButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(color.darker());
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(color);
            }
        });
        return button;
    }

    private void createTablePanel() {
    String[] columnNames = {"ID", "Nombre", "Descripción", "Categoría", "Precio", "Stock", "Estado"};
    tableModel = new DefaultTableModel(columnNames, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    tablaProductos = new JTable(tableModel);

   
    tablaProductos.setFont(new Font("Segoe UI", Font.PLAIN, 12));
    tablaProductos.setRowHeight(35);
    tablaProductos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    tablaProductos.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
    tablaProductos.getTableHeader().setBackground(new Color(50, 50, 50));
    tablaProductos.getTableHeader().setForeground(Color.WHITE);
    tablaProductos.getTableHeader().setReorderingAllowed(false);
    tablaProductos.setBackground(Color.WHITE);
    tablaProductos.setForeground(Color.BLACK);
    tablaProductos.setGridColor(new Color(220, 220, 220));
    tablaProductos.setSelectionBackground(new Color(70, 130, 180));
    tablaProductos.setSelectionForeground(Color.WHITE);
    tablaProductos.setFillsViewportHeight(true);

   
    DefaultTableCellRenderer mainRenderer = new DefaultTableCellRenderer() {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            
            if (!isSelected) {
                c.setForeground(Color.BLACK);
                c.setBackground(Color.WHITE);
            } else {
                c.setForeground(Color.WHITE);
                c.setBackground(new Color(70, 130, 180));
            }
            
            return c;
        }
    };

    // Aplicar renderer principal a todas las columnas 
    for (int i = 0; i < tablaProductos.getColumnCount(); i++) {
        if (i != 5 && i != 6) { // No aplicar a stock (5) y estado (6)
            tablaProductos.getColumnModel().getColumn(i).setCellRenderer(mainRenderer);
        }
    }

    // Renderer para columna de stock 
    tablaProductos.getColumnModel().getColumn(5).setCellRenderer(new DefaultTableCellRenderer() {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            
            if (!isSelected && value != null && value instanceof Integer) {
                int stock = (Integer) value;
                if (stock == 0) {
                    c.setBackground(new Color(255, 220, 220)); 
                    c.setForeground(Color.RED);
                } else if (stock <= 5) {
                    c.setBackground(new Color(255, 245, 200)); 
                    c.setForeground(Color.ORANGE);
                } else {
                    c.setBackground(new Color(220, 255, 220)); 
                    c.setForeground(Color.BLACK);
                }
            } else if (isSelected) {
                c.setBackground(table.getSelectionBackground());
                c.setForeground(table.getSelectionForeground());
            }
            
            setHorizontalAlignment(SwingConstants.CENTER);
            return c;
        }
    });

    // Renderer para columna de estado 
    tablaProductos.getColumnModel().getColumn(6).setCellRenderer(new DefaultTableCellRenderer() {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            
            if (!isSelected && value != null) {
                String estado = value.toString();
                if (estado.equals("Activo")) {
                    c.setBackground(new Color(220, 255, 220));
                    c.setForeground(new Color(0, 128, 0));
                } else {
                    c.setBackground(new Color(255, 220, 220));
                    c.setForeground(new Color(128, 0, 0));
                }
            } else if (isSelected) {
                c.setBackground(table.getSelectionBackground());
                c.setForeground(table.getSelectionForeground());
            }
            
            setHorizontalAlignment(SwingConstants.CENTER);
            return c;
        }
    });

    JScrollPane scrollPane = new JScrollPane(tablaProductos);
    scrollPane.getViewport().setBackground(Color.WHITE);

    add(scrollPane, BorderLayout.CENTER);
}

    private void abrirDialogoProducto(Object[] datosProducto) {
        Producto producto = null;
        String titulo = "Agregar Producto";

        if (datosProducto != null) {
            producto = new Producto();
            producto.setId((Integer) datosProducto[0]);
            producto.setNombre(datosProducto[1].toString());
            producto.setDescripcion(datosProducto[2].toString());
            producto.setIdCategoria(obtenerIdCategoriaPorNombre(datosProducto[3].toString()));
            producto.setPrecioUnitario((Double) datosProducto[4]);
            producto.setStock((Integer) datosProducto[5]);
            producto.setActivo(datosProducto[6].toString().equals("Activo"));
            titulo = "Editar Producto";
        }

        Window parentWindow = SwingUtilities.getWindowAncestor(this);
        JFrame parentFrame = null;
        if (parentWindow instanceof JFrame) {
            parentFrame = (JFrame) parentWindow;
        }

        ProductoDialog dialog = new ProductoDialog(parentFrame, titulo, producto);
        dialog.setVisible(true);

        if (dialog.isGuardadoExitoso()) {
            actualizarTabla();
            JOptionPane.showMessageDialog(this,
                    "Los cambios se han guardado correctamente.",
                    "Guardado Exitoso",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void editarProductoSeleccionado() {
        int filaSeleccionada = tablaProductos.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this,
                    "Por favor, seleccione un producto para editar.",
                    "Selección Requerida",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        Object[] datosProducto = new Object[tableModel.getColumnCount()];
        for (int i = 0; i < tableModel.getColumnCount(); i++) {
            datosProducto[i] = tableModel.getValueAt(filaSeleccionada, i);
        }

        abrirDialogoProducto(datosProducto);
    }

    private void eliminarProductoSeleccionado() {
        int filaSeleccionada = tablaProductos.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this,
                    "Por favor, seleccione un producto para eliminar.",
                    "Selección Requerida",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int idProducto = (Integer) tableModel.getValueAt(filaSeleccionada, 0);
        String nombreProducto = tableModel.getValueAt(filaSeleccionada, 1).toString();

        int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Está seguro que desea eliminar el producto:\n" + nombreProducto + "?\n\n"
                + "Esta acción marcará el producto como inactivo.",
                "Confirmar Eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirmacion == JOptionPane.YES_OPTION) {
            try {
                productoController.eliminarProducto(idProducto);

                tableModel.removeRow(filaSeleccionada);

                JOptionPane.showMessageDialog(this,
                        "Producto eliminado correctamente.",
                        "Eliminación Exitosa",
                        JOptionPane.INFORMATION_MESSAGE);

            } catch (ServiceException e) {
                JOptionPane.showMessageDialog(this,
                        "Error al eliminar producto: " + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void cambiarEstadoProducto() {
        int fila = tablaProductos.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un producto", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int idProducto = (Integer) tableModel.getValueAt(fila, 0);
        String estadoActual = tableModel.getValueAt(fila, 6).toString();
        String nuevoEstado = estadoActual.equals("Activo") ? "Inactivo" : "Activo";

        try {
            Producto producto = productoController.buscarProductoPorId(idProducto);
            producto.setActivo(nuevoEstado.equals("Activo"));
            productoController.actualizarProducto(producto);

            tableModel.setValueAt(nuevoEstado, fila, 6);

        } catch (ServiceException e) {
            JOptionPane.showMessageDialog(this,
                    "Error al cambiar estado del producto: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void filtrarPorCategoria() {
        String categoriaSeleccionada = (String) comboFiltroCategoria.getSelectedItem();
        if (categoriaSeleccionada.equals("Todas")) {
            actualizarTabla();
            return;
        }

        
        try {
            tableModel.setRowCount(0);
            List<Producto> productos = productoController.listarTodos();

            for (Producto producto : productos) {
                String nombreCategoria = obtenerNombreCategoriaPorId(producto.getIdCategoria());
                if (nombreCategoria.equals(categoriaSeleccionada)) {
                    Object[] fila = {
                        producto.getId(),
                        producto.getNombre(),
                        producto.getDescripcion(),
                        nombreCategoria,
                        producto.getPrecioUnitario(),
                        producto.getStock(),
                        producto.isActivo() ? "Activo" : "Inactivo"
                    };
                    tableModel.addRow(fila);
                }
            }

        } catch (ServiceException e) {
            JOptionPane.showMessageDialog(this,
                    "Error al filtrar productos: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buscarProductos() {
        String textoBusqueda = txtBuscar.getText().trim().toLowerCase();
        if (textoBusqueda.isEmpty()) {
            actualizarTabla();
            return;
        }

        try {
            tableModel.setRowCount(0);
           
            List<Producto> productos = productoController.buscarPorNombre(textoBusqueda);

            for (Producto producto : productos) {
                Object[] fila = {
                    producto.getId(),
                    producto.getNombre(),
                    producto.getDescripcion(),
                    obtenerNombreCategoriaPorId(producto.getIdCategoria()),
                    producto.getPrecioUnitario(),
                    producto.getStock(),
                    producto.isActivo() ? "Activo" : "Inactivo"
                };
                tableModel.addRow(fila);
            }

        } catch (ServiceException e) {
            JOptionPane.showMessageDialog(this,
                    "Error al buscar productos: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Actualiza la tabla de productos con los datos más recientes de la base de
     * datos.
     */
    public void actualizarTabla() {
        try {
            tableModel.setRowCount(0);
           
            List<Producto> productos = productoController.listarTodos();

            for (Producto producto : productos) {
                Object[] fila = {
                    producto.getId(),
                    producto.getNombre(),
                    producto.getDescripcion(),
                    obtenerNombreCategoriaPorId(producto.getIdCategoria()),
                    producto.getPrecioUnitario(),
                    producto.getStock(),
                    producto.isActivo() ? "Activo" : "Inactivo"
                };
                tableModel.addRow(fila);
            }

        } catch (ServiceException e) {
            JOptionPane.showMessageDialog(this,
                    "Error al cargar productos: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    
    private String obtenerNombreCategoriaPorId(int idCategoria) {
        String[] categorias = {
            "", "Audio", "Barberia", "Bicicletas", "Camaras", "Celulares",
            "Cocinas", "Colchones", "Freezers", "Heladeras", "Impresoras",
            "Jardin", "Lavarropas", "Notebooks", "Secarropas", "Soportes",
            "Tablets", "Tv"
        };
        return idCategoria >= 0 && idCategoria < categorias.length ? categorias[idCategoria] : "Desconocida";
    }

    private int obtenerIdCategoriaPorNombre(String nombreCategoria) {
        String[] categorias = {
            "", "Audio", "Barberia", "Bicicletas", "Camaras", "Celulares",
            "Cocinas", "Colchones", "Freezers", "Heladeras", "Impresoras",
            "Jardin", "Lavarropas", "Notebooks", "Secarropas", "Soportes",
            "Tablets", "Tv"
        };
        for (int i = 1; i < categorias.length; i++) {
            if (categorias[i].equals(nombreCategoria)) {
                return i;
            }
        }
        return 1; 
    }
}
