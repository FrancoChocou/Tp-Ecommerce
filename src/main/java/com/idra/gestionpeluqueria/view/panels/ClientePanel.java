package com.idra.gestionpeluqueria.view.panels;

import com.idra.gestionpeluqueria.controller.ClienteController;
import com.idra.gestionpeluqueria.exception.ServiceException;
import java.util.List;
import com.idra.gestionpeluqueria.model.Cliente;
import com.idra.gestionpeluqueria.view.dialogs.ClienteDialog;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;

public class ClientePanel extends JPanel {

    private JTable tablaClientes;
    private DefaultTableModel tableModel;
    private JButton btnAgregar, btnEditar, btnEliminar, btnActivarDesactivar, btnBuscar;
    private JTextField txtBuscar;
    private ClienteController clienteController;

    public ClientePanel() {
        this.clienteController = new ClienteController();
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(245, 245, 245));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        createHeaderPanel();
        createTablePanel();
        createToolbar();
        actualizarTabla();
    }

    private void createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(245, 245, 245));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        JLabel titleLabel = new JLabel("Gestión de Clientes");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(new Color(40, 40, 40));

        JLabel subtitleLabel = new JLabel("Administre los clientes del sistema");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(100, 100, 100));

        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(new Color(245, 245, 245));
        titlePanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
        titlePanel.add(titleLabel, BorderLayout.NORTH);
        titlePanel.add(subtitleLabel, BorderLayout.CENTER);

        headerPanel.add(titlePanel, BorderLayout.WEST);
        add(headerPanel, BorderLayout.NORTH);
    }

    private void createToolbar() {
        JPanel toolbarPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        toolbarPanel.setBackground(new Color(245, 245, 245));
        toolbarPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));

        btnAgregar = createToolbarButton("➕ Agregar Cliente", new Color(46, 204, 113));
        btnEditar = createToolbarButton("✏️ Editar", new Color(52, 152, 219));
        btnEliminar = createToolbarButton("🗑️ Eliminar", new Color(231, 76, 60));
        btnActivarDesactivar = createToolbarButton("⚡ Estado", new Color(243, 156, 18));

        btnAgregar.addActionListener(e -> { abrirDialogoCliente((Cliente) null);
});     btnEditar.addActionListener(e -> editarClienteSeleccionado());
        btnEliminar.addActionListener(e -> eliminarClienteSeleccionado());
        btnActivarDesactivar.addActionListener(e -> cambiarEstadoCliente());

        // Búsqueda
    JPanel searchPanel = new JPanel(new BorderLayout(5, 0));
        searchPanel.setBackground(new Color(245, 245, 245));

        JLabel lblBuscar = new JLabel("Buscar:");
        lblBuscar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblBuscar.setForeground(new Color(80, 80, 80));

        txtBuscar = new JTextField(15);
        txtBuscar.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtBuscar.setPreferredSize(new Dimension(150, 30));

        btnBuscar = createToolbarButton("🔍 Buscar", new Color(155, 89, 182));
        btnBuscar.addActionListener(e -> buscarClientes());

        searchPanel.add(lblBuscar, BorderLayout.WEST);
        searchPanel.add(txtBuscar, BorderLayout.CENTER);
        searchPanel.add(btnBuscar, BorderLayout.EAST);

        toolbarPanel.add(btnAgregar);
        toolbarPanel.add(Box.createHorizontalStrut(10));
        toolbarPanel.add(btnEditar);
        toolbarPanel.add(Box.createHorizontalStrut(10));
        toolbarPanel.add(btnEliminar);
        toolbarPanel.add(Box.createHorizontalStrut(10));
        toolbarPanel.add(btnActivarDesactivar);
        toolbarPanel.add(Box.createHorizontalStrut(30));
        toolbarPanel.add(searchPanel);

        add(toolbarPanel, BorderLayout.SOUTH);
    }

    private JButton createToolbarButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(color.darker(), 1),
            BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(color.darker());
                button.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(color.darker().darker(), 1),
                    BorderFactory.createEmptyBorder(8, 15, 8, 15)
                ));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(color);
                button.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(color.darker(), 1),
                    BorderFactory.createEmptyBorder(8, 15, 8, 15)
                ));
            }
        });
        return button;
    }

    private void createTablePanel() {
        String[] columnNames = {"ID", "Nombre", "Apellido", "Email", "Teléfono", "Estado"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaClientes = new JTable(tableModel);

        // ESTILOS DE TABLA INTEGRADOS
        tablaClientes.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tablaClientes.setRowHeight(35);
        tablaClientes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // HEADER CON COLOR OSCURO
        JTableHeader header = tablaClientes.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(new Color(45, 45, 45));
        header.setForeground(Color.WHITE);
        header.setReorderingAllowed(false);

        tablaClientes.setBackground(Color.WHITE);
        tablaClientes.setForeground(new Color(60, 60, 60));
        tablaClientes.setGridColor(new Color(240, 240, 240));
        tablaClientes.setSelectionBackground(new Color(70, 130, 180));
        tablaClientes.setSelectionForeground(Color.WHITE);
        tablaClientes.setFillsViewportHeight(true);

        // RENDERER PARA FILAS ALTERNADAS
        tablaClientes.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                if (!isSelected) {
                    c.setForeground(new Color(60, 60, 60));
                    c.setBackground(row % 2 == 0 ? new Color(250, 250, 250) : Color.WHITE);
                } else {
                    c.setForeground(Color.WHITE);
                    c.setBackground(new Color(70, 130, 180));
                }
                
                return c;
            }
        });

        // RENDERER ESPECIAL PARA COLUMNA ESTADO
        tablaClientes.getColumnModel().getColumn(4).setCellRenderer(new DefaultTableCellRenderer() {
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

        JScrollPane scrollPane = new JScrollPane(tablaClientes);
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        add(scrollPane, BorderLayout.CENTER);
    }

   private void abrirDialogoCliente(Cliente cliente) {  // ✅ Cambiar parámetro a Cliente
    String titulo = "Agregar Cliente";

    if (cliente != null) {
        titulo = "Editar Cliente";
    }

    Window parentWindow = SwingUtilities.getWindowAncestor(this);
    JFrame parentFrame = null;
    if (parentWindow instanceof JFrame) {
        parentFrame = (JFrame) parentWindow;
    }

    // ✅ Pasar el objeto Cliente directamente
    ClienteDialog dialog = new ClienteDialog(parentFrame, titulo, cliente);
    dialog.setVisible(true);

    if (dialog.isGuardadoExitoso()) {
        actualizarTabla();
        JOptionPane.showMessageDialog(this,
                "Los cambios se han guardado correctamente.",
                "Guardado Exitoso",
                JOptionPane.INFORMATION_MESSAGE);
    }
}

// ✅ Sobrecargar el método para mantener compatibilidad (si es necesario)

   private void editarClienteSeleccionado() {
    int filaSeleccionada = tablaClientes.getSelectedRow();
    if (filaSeleccionada == -1) {
        JOptionPane.showMessageDialog(this,
                "Por favor, seleccione un cliente para editar.",
                "Selección Requerida",
                JOptionPane.WARNING_MESSAGE);
        return;
    }

    int idCliente = (Integer) tableModel.getValueAt(filaSeleccionada, 0);
    
    try {
        // ✅ CORREGIDO: Obtener el cliente COMPLETO desde la base de datos
        Cliente cliente = clienteController.buscarClientePorId(idCliente);
        
        if (cliente != null) {
            // ✅ Pasar el objeto Cliente completo, no los datos de la tabla
            abrirDialogoCliente(cliente);
        } else {
            JOptionPane.showMessageDialog(this,
                    "No se pudo encontrar el cliente seleccionado.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
        
    } catch (ServiceException e) {
        JOptionPane.showMessageDialog(this,
                "Error al cargar cliente: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
    }
}
    private void eliminarClienteSeleccionado() {
        int filaSeleccionada = tablaClientes.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this,
                    "Por favor, seleccione un cliente para eliminar.",
                    "Selección Requerida",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int idCliente = (Integer) tableModel.getValueAt(filaSeleccionada, 0);
        String nombreCliente = tableModel.getValueAt(filaSeleccionada, 1).toString();

        int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Está seguro que desea eliminar el cliente:\n" + nombreCliente + "?",
                "Confirmar Eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirmacion == JOptionPane.YES_OPTION) {
            try {
                clienteController.eliminarCliente(idCliente);
                tableModel.removeRow(filaSeleccionada);
                JOptionPane.showMessageDialog(this,
                        "Cliente eliminado correctamente.",
                        "Eliminación Exitosa",
                        JOptionPane.INFORMATION_MESSAGE);

            } catch (ServiceException e) {
                JOptionPane.showMessageDialog(this,
                        "Error al eliminar cliente: " + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void cambiarEstadoCliente() {
        int fila = tablaClientes.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un cliente", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int idCliente = (Integer) tableModel.getValueAt(fila, 0);
        String estadoActual = tableModel.getValueAt(fila, 4).toString();
        String nuevoEstado = estadoActual.equals("Activo") ? "Inactivo" : "Activo";

        try {
            Cliente cliente = clienteController.buscarClientePorId(idCliente);
            cliente.setActivo(nuevoEstado.equals("Activo"));
            clienteController.actualizarCliente(cliente);

            tableModel.setValueAt(nuevoEstado, fila, 4);

        } catch (ServiceException e) {
            JOptionPane.showMessageDialog(this,
                    "Error al cambiar estado del cliente: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buscarClientes() {
    String textoBusqueda = txtBuscar.getText().trim().toLowerCase();
    if (textoBusqueda.isEmpty()) {
        actualizarTabla();
        return;
    }

    try {
        tableModel.setRowCount(0);
        // ✅ CORREGIDO: Cambiar buscarClientesPorNombre() por buscarPorNombre()
        List<Cliente> clientes = clienteController.buscarPorNombre(textoBusqueda);

        for (Cliente cliente : clientes) {
            Object[] fila = {
                cliente.getId(),
                cliente.getNombre(),
                cliente.getApellido(),
                cliente.getEmail(),
                cliente.getTelefono(),
                cliente.isActivo() ? "Activo" : "Inactivo"
            };
            tableModel.addRow(fila);
        }

    } catch (ServiceException e) {
        JOptionPane.showMessageDialog(this,
                "Error al buscar clientes: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
    }
}

    public void actualizarTabla() {
    try {
        tableModel.setRowCount(0);
        List<Cliente> clientes = clienteController.listarTodos();

        for (Cliente cliente : clientes) {
            Object[] fila = {
                cliente.getId(),
                cliente.getNombre(),
                cliente.getApellido(),    // ✅ Columna 2: Apellido
                cliente.getEmail(),       // ✅ Columna 3: Email  
                cliente.getTelefono(),    // ✅ Columna 4: Teléfono
                cliente.isActivo() ? "Activo" : "Inactivo" // ✅ Columna 5: Estado
            };
            tableModel.addRow(fila);
        }

    } catch (ServiceException e) {
        JOptionPane.showMessageDialog(this,
                "Error al cargar clientes: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
    }
}
}