package com.idra.gestionpeluqueria.view.panels;

import com.idra.gestionpeluqueria.controller.VentaController;
import com.idra.gestionpeluqueria.model.Venta;
import com.idra.gestionpeluqueria.exception.ServiceException;
import com.idra.gestionpeluqueria.view.dialogs.VentaDialog;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * Panel para la gestion de ventas del e-commerce. Proporciona una interfaz para
 * visualizar, crear, editar y eliminar ventas. Incluye funcionalidades de
 * filtrado por producto y busqueda por cliente.
 *
 * @author Idra
 */
public class VentaPanel extends JPanel {

    private JTable tablaVentas;
    private DefaultTableModel tableModel;
    private JButton btnNuevaVenta, btnEditar, btnEliminar, btnBuscar;
    private JTextField txtBuscar;
    private JComboBox<String> comboFiltroProducto;

    /**
     * Constructor que inicializa el panel de ventas, sus componentes y carga
     * los datos iniciales.
     */
    public VentaPanel() {
        initializeUI();
        inicializarDatos();
    }

    private void initializeUI() {
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(230, 240, 255)); // CELESTE CLARO EN LUGAR DE GRIS
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        createHeaderPanel();
        createTablePanel();
        createToolbar();
    }

    private void inicializarDatos() {
        actualizarTabla();
    }

    private void createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(230, 240, 255)); // CELESTE CLARO

        JLabel titleLabel = new JLabel("Gestión de Ventas");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(50, 50, 50));

        JLabel subtitleLabel = new JLabel("Administre las ventas y transacciones del e-commerce");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(100, 100, 100));

        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(new Color(240, 240, 240));
        titlePanel.add(titleLabel, BorderLayout.NORTH);
        titlePanel.add(subtitleLabel, BorderLayout.CENTER);

        headerPanel.add(titlePanel, BorderLayout.WEST);
        add(headerPanel, BorderLayout.NORTH);
    }

    private void createToolbar() {
        JPanel toolbarPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        toolbarPanel.setBackground(new Color(240, 240, 240));
        toolbarPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        btnNuevaVenta = createToolbarButton("🛒 Nueva Venta", new Color(39, 174, 96));
        btnEditar = createToolbarButton("✏️ Editar", new Color(41, 128, 185));
        btnEliminar = createToolbarButton("🗑️ Eliminar", new Color(231, 76, 60));

        btnNuevaVenta.addActionListener(e -> abrirDialogoVenta(null));
        btnEditar.addActionListener(e -> editarVentaSeleccionada());
        btnEliminar.addActionListener(e -> eliminarVentaSeleccionada());

        // Filtros
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        filterPanel.setBackground(new Color(240, 240, 240));

        JLabel lblProducto = new JLabel("Producto:");
        lblProducto.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        // Productos de ejemplo - en una implementación real se cargarían desde BD
        comboFiltroProducto = new JComboBox<>(new String[]{"Todos", "Laptop", "Smartphone", "Tablet", "Auriculares", "Monitor"});
        comboFiltroProducto.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        comboFiltroProducto.addActionListener(e -> filtrarPorProducto());

        // Búsqueda
        JPanel searchPanel = new JPanel(new BorderLayout(5, 0));
        searchPanel.setBackground(new Color(240, 240, 240));

        JLabel lblBuscar = new JLabel("Buscar cliente:");
        lblBuscar.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        txtBuscar = new JTextField(15);
        txtBuscar.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        btnBuscar = createToolbarButton("🔍 Buscar", new Color(155, 89, 182));
        btnBuscar.addActionListener(e -> buscarVentas());

        searchPanel.add(lblBuscar, BorderLayout.WEST);
        searchPanel.add(txtBuscar, BorderLayout.CENTER);
        searchPanel.add(btnBuscar, BorderLayout.EAST);

        filterPanel.add(lblProducto);
        filterPanel.add(comboFiltroProducto);
        filterPanel.add(Box.createHorizontalStrut(20));
        filterPanel.add(searchPanel);

        toolbarPanel.add(btnNuevaVenta);
        toolbarPanel.add(Box.createHorizontalStrut(10));
        toolbarPanel.add(btnEditar);
        toolbarPanel.add(Box.createHorizontalStrut(10));
        toolbarPanel.add(btnEliminar);
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
    String[] columnNames = {"ID", "Fecha/Hora", "Cliente", "Producto", "Cantidad", "Precio Unit.", "Total", "Método Pago"};
    tableModel = new DefaultTableModel(columnNames, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    tablaVentas = new JTable(tableModel);
    
    // MEJORAS EN EL DISEÑO DE LA TABLA
    tablaVentas.setFont(new Font("Segoe UI", Font.PLAIN, 12));
    tablaVentas.setRowHeight(35);
    tablaVentas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    
    // HEADER MEJORADO
    tablaVentas.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
    tablaVentas.getTableHeader().setBackground(new Color(50, 50, 50));
    tablaVentas.getTableHeader().setForeground(Color.WHITE);
    tablaVentas.getTableHeader().setReorderingAllowed(false);
    
    // COLORES DE LA TABLA
    tablaVentas.setBackground(new Color(250, 250, 250));
    tablaVentas.setForeground(Color.BLACK);
    tablaVentas.setGridColor(new Color(220, 220, 220));
    tablaVentas.setSelectionBackground(new Color(70, 130, 180));
    tablaVentas.setSelectionForeground(Color.WHITE);
    tablaVentas.setFillsViewportHeight(true);

    JScrollPane scrollPane = new JScrollPane(tablaVentas);
    scrollPane.getViewport().setBackground(new Color(250, 250, 250));
    
    // COLOR DE FONDO DEL PANEL PRINCIPAL
    setBackground(new Color(230, 240, 255));

    add(scrollPane, BorderLayout.CENTER);
    
    // ⭐⭐ RENDERER DEBE IR AL FINAL - DESPUÉS DE QUE LA TABLA ESTÉ COMPLETAMENTE CREADA ⭐⭐
    tablaVentas.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
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
    });
}

    /**
     * Actualiza la tabla de ventas con los datos más recientes de la base de
     * datos. Limpia la tabla actual y la llena con todas las ventas
     * registradas, mostrando información completa de cada venta incluyendo
     * cliente, producto y montos.
     */
    public final void actualizarTabla() {
        try {
            tableModel.setRowCount(0); // Limpiar tabla

            // Obtener ventas REALES de la base de datos
            VentaController controller = new VentaController();
            List<Venta> ventas = controller.buscarTodasVentas();

            System.out.println("🔄 VentaPanel - Ventas encontradas en BD: " + ventas.size());

            // Llenar la tabla con datos REALES
            for (Venta venta : ventas) {
                Object[] fila = {
                    venta.getId(),
                    venta.getFecha().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                    venta.getCliente().getNombre() + " " + venta.getCliente().getApellido(),
                    venta.getProducto().getNombre(),
                    venta.getCantidad(),
                    String.format("$%.2f", venta.getPrecioUnitario()),
                    String.format("$%.2f", venta.getTotal()),
                    obtenerNombreMetodoPago(venta.getIdMetodoPago())
                };
                tableModel.addRow(fila);
            }

            System.out.println("✅ VentaPanel - Tabla actualizada con " + tableModel.getRowCount() + " ventas");

        } catch (ServiceException e) {
            System.err.println("❌ Error al actualizar tabla de ventas: " + e.getMessage());
            JOptionPane.showMessageDialog(this,
                    "Error al cargar ventas: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void abrirDialogoVenta(Object[] datosVenta) {
        Venta venta = null;
        String titulo = "Nueva Venta";

        if (datosVenta != null) {
            try {
                // Para editar, buscar la venta completa de la BD
                int idVenta = (Integer) datosVenta[0];
                VentaController controller = new VentaController();
                venta = controller.buscarVentaPorId(idVenta);
                titulo = "Editar Venta";
            } catch (ServiceException e) {
                JOptionPane.showMessageDialog(this,
                        "Error al cargar venta: " + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        Window parentWindow = SwingUtilities.getWindowAncestor(this);
        JFrame parentFrame = null;
        if (parentWindow instanceof JFrame) {
            parentFrame = (JFrame) parentWindow;
        }

        VentaDialog dialog = new VentaDialog(parentFrame, titulo, venta);
        dialog.setVisible(true);

        if (dialog.isGuardadoExitoso()) {
            actualizarTabla(); // Refrescar la tabla
        }
    }

    private void editarVentaSeleccionada() {
        int filaSeleccionada = tablaVentas.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this,
                    "Por favor, seleccione una venta para editar.",
                    "Selección Requerida",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Obtener datos de la venta seleccionada
        Object[] datosVenta = new Object[tableModel.getColumnCount()];
        for (int i = 0; i < tableModel.getColumnCount(); i++) {
            datosVenta[i] = tableModel.getValueAt(filaSeleccionada, i);
        }

        abrirDialogoVenta(datosVenta);
    }

    private void eliminarVentaSeleccionada() {
        int filaSeleccionada = tablaVentas.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this,
                    "Por favor, seleccione una venta para eliminar.",
                    "Selección Requerida",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int idVenta = (Integer) tableModel.getValueAt(filaSeleccionada, 0);
        String cliente = tableModel.getValueAt(filaSeleccionada, 2).toString();
        String producto = tableModel.getValueAt(filaSeleccionada, 3).toString();

        int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Está seguro que desea eliminar la venta de " + producto + " para " + cliente + "?",
                "Confirmar Eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirmacion == JOptionPane.YES_OPTION) {
            try {
                // Eliminar en la base de datos
                VentaController controller = new VentaController();
                controller.eliminarVenta(idVenta);

                // Actualizar la tabla completa desde BD
                actualizarTabla();

                JOptionPane.showMessageDialog(this,
                        "Venta eliminada correctamente.",
                        "Eliminación Exitosa",
                        JOptionPane.INFORMATION_MESSAGE);

            } catch (ServiceException e) {
                JOptionPane.showMessageDialog(this,
                        "Error al eliminar venta: " + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void filtrarPorProducto() {
        String productoSelleccionado = (String) comboFiltroProducto.getSelectedItem();
        if (productoSelleccionado.equals("Todos")) {
            actualizarTabla();
            return;
        }

        try {
            tableModel.setRowCount(0); // Limpiar tabla

            VentaController controller = new VentaController();
            List<Venta> todasVentas = controller.buscarTodasVentas();

            // Filtrar localmente por nombre de producto
            for (Venta venta : todasVentas) {
                if (venta.getProducto().getNombre().equals(productoSelleccionado)) {
                    Object[] fila = {
                        venta.getId(),
                        venta.getFecha().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                        venta.getCliente().getNombre() + " " + venta.getCliente().getApellido(),
                        venta.getProducto().getNombre(),
                        venta.getCantidad(),
                        String.format("$%.2f", venta.getPrecioUnitario()),
                        String.format("$%.2f", venta.getTotal()),
                        obtenerNombreMetodoPago(venta.getIdMetodoPago())
                    };
                    tableModel.addRow(fila);
                }
            }

            if (tableModel.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this,
                        "No se encontraron ventas para el producto: " + productoSelleccionado,
                        "Sin Resultados",
                        JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (ServiceException e) {
            JOptionPane.showMessageDialog(this,
                    "Error al filtrar ventas: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buscarVentas() {
        String textoBusqueda = txtBuscar.getText().trim().toLowerCase();
        if (textoBusqueda.isEmpty()) {
            actualizarTabla();
            return;
        }

        try {
            tableModel.setRowCount(0); // Limpiar tabla

            VentaController controller = new VentaController();
            List<Venta> todasVentas = controller.buscarTodasVentas();

            // Filtrar localmente por nombre de cliente
            for (Venta venta : todasVentas) {
                String nombreCliente = venta.getCliente().getNombre().toLowerCase() + " "
                        + venta.getCliente().getApellido().toLowerCase();

                if (nombreCliente.contains(textoBusqueda)) {
                    Object[] fila = {
                        venta.getId(),
                        venta.getFecha().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                        venta.getCliente().getNombre() + " " + venta.getCliente().getApellido(),
                        venta.getProducto().getNombre(),
                        venta.getCantidad(),
                        String.format("$%.2f", venta.getPrecioUnitario()),
                        String.format("$%.2f", venta.getTotal()),
                        obtenerNombreMetodoPago(venta.getIdMetodoPago())
                    };
                    tableModel.addRow(fila);
                }
            }

            if (tableModel.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this,
                        "No se encontraron ventas que coincidan con: " + textoBusqueda,
                        "Sin Resultados",
                        JOptionPane.INFORMATION_MESSAGE);
                actualizarTabla();
            }

        } catch (ServiceException e) {
            JOptionPane.showMessageDialog(this,
                    "Error al buscar ventas: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Método auxiliar para obtener nombre de método de pago (simulado)
     */
    private String obtenerNombreMetodoPago(int idMetodoPago) {
        // En una implementación real, esto vendría de la base de datos
        String[] metodos = {"", "Tarjeta Crédito", "Tarjeta Débito", "Efectivo", "Transferencia", "Mercado Pago"};
        return idMetodoPago >= 0 && idMetodoPago < metodos.length ? metodos[idMetodoPago] : "Desconocido";
    }
}
