package com.idra.gestionpeluqueria.view.panels;

import com.idra.gestionpeluqueria.controller.ClienteController;
import com.idra.gestionpeluqueria.model.Cliente;
import com.idra.gestionpeluqueria.view.dialogs.ClienteDialog;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import com.idra.gestionpeluqueria.exception.ServiceException;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * Panel para la gestion de clientes en el e-commerce. Proporciona una interfaz
 * para visualizar, agregar, editar, eliminar y buscar clientes. Incluye una
 * tabla con todos los clientes y botones para realizar las operaciones CRUD.
 *
 * @author Idra
 */
public class ClientePanel extends JPanel {

    private JTable tablaClientes;
    private DefaultTableModel tableModel;
    private JButton btnAgregar, btnEditar, btnEliminar, btnBuscar;
    private JTextField txtBuscar;
    private ClienteController clienteController;

    /**
     * Constructor que inicializa el panel de clientes y sus componentes.
     */
    public ClientePanel() {
        this.clienteController = new ClienteController();
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(230, 240, 255)); // CELESTE CLARO EN LUGAR DE GRIS
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Crear componentes
        createHeaderPanel();
        createTablePanel();
        createToolbar();

        // Cargar datos iniciales
        actualizarTabla();
    }

    private void createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(230, 240, 255)); // CELESTE CLARO

        JLabel titleLabel = new JLabel("Gestión de Clientes");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(50, 50, 50));

        JLabel subtitleLabel = new JLabel("Administre la información de los clientes del e-commerce");
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

        // Botón Agregar
        btnAgregar = createToolbarButton("➕ Agregar Cliente", new Color(39, 174, 96));
        btnAgregar.addActionListener(e -> abrirDialogoCliente(null));

        // Botón Editar
        btnEditar = createToolbarButton("✏️ Editar", new Color(41, 128, 185));
        btnEditar.addActionListener(e -> editarClienteSeleccionado());

        // Botón Eliminar
        btnEliminar = createToolbarButton("🗑️ Eliminar", new Color(231, 76, 60));
        btnEliminar.addActionListener(e -> eliminarClienteSeleccionado());

        // Campo de búsqueda
        JPanel searchPanel = new JPanel(new BorderLayout(5, 0));
        searchPanel.setBackground(new Color(240, 240, 240));

        JLabel lblBuscar = new JLabel("Buscar:");
        lblBuscar.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        txtBuscar = new JTextField(20);
        txtBuscar.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtBuscar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));

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
        // Modelo de tabla
        String[] columnNames = {"ID", "Nombre", "Apellido", "Teléfono", "Email", "Edad", "Ciudad", "Fecha Registro", "Activo"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaClientes = new JTable(tableModel);

        // MEJORAS EN EL DISEÑO DE LA TABLA
        tablaClientes.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tablaClientes.setRowHeight(35);
        tablaClientes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // HEADER MEJORADO
        tablaClientes.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tablaClientes.getTableHeader().setBackground(new Color(50, 50, 50));
        tablaClientes.getTableHeader().setForeground(Color.WHITE);
        tablaClientes.getTableHeader().setReorderingAllowed(false);

        // COLORES DE LA TABLA
        tablaClientes.setBackground(Color.WHITE);
        tablaClientes.setForeground(Color.BLACK);
        tablaClientes.setGridColor(new Color(220, 220, 220));
        tablaClientes.setSelectionBackground(new Color(70, 130, 180));
        tablaClientes.setSelectionForeground(Color.WHITE);
        tablaClientes.setFillsViewportHeight(true);

        // ✅ RENDERER CORREGIDO - FORZAR COLOR NEGRO
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
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

        // Aplicar el renderer a todas las columnas
        for (int i = 0; i < tablaClientes.getColumnCount(); i++) {
            tablaClientes.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }

        JScrollPane scrollPane = new JScrollPane(tablaClientes);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        scrollPane.getViewport().setBackground(Color.WHITE);

        add(scrollPane, BorderLayout.CENTER);
    }

    private void abrirDialogoCliente(Object[] datosCliente) {
        Cliente cliente = null;
        String titulo = "Agregar Cliente";

        if (datosCliente != null) {
            // Crear cliente desde datos de la tabla
            cliente = new Cliente();
            cliente.setId((Integer) datosCliente[0]);
            cliente.setNombre(datosCliente[1].toString());
            cliente.setApellido(datosCliente[2].toString());
            cliente.setTelefono(datosCliente[3].toString());
            cliente.setEmail(datosCliente[4].toString());
            cliente.setEdad((Integer) datosCliente[5]);
            cliente.setIdZona((Integer) datosCliente[6]);
            titulo = "Editar Cliente";
        }

        // Buscar el JFrame padre
        Window parentWindow = SwingUtilities.getWindowAncestor(this);
        JFrame parentFrame = null;
        if (parentWindow instanceof JFrame) {
            parentFrame = (JFrame) parentWindow;
        }

        ClienteDialog dialog = new ClienteDialog(parentFrame, titulo, cliente);
        dialog.setVisible(true);

        if (dialog.isGuardadoExitoso()) {
            actualizarTabla(); // Refrescar la tabla
        }
    }

    private void editarClienteSeleccionado() {
        int filaSeleccionada = tablaClientes.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this,
                    "Por favor, seleccione un cliente para editar.",
                    "Selección Requerida",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Obtener datos del cliente seleccionado
        Object[] datosCliente = new Object[tableModel.getColumnCount()];
        for (int i = 0; i < tableModel.getColumnCount(); i++) {
            datosCliente[i] = tableModel.getValueAt(filaSeleccionada, i);
        }

        abrirDialogoCliente(datosCliente);
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

        // Obtener el ID del cliente seleccionado
        int idCliente = (Integer) tableModel.getValueAt(filaSeleccionada, 0);
        String nombreCliente = tableModel.getValueAt(filaSeleccionada, 1) + " "
                + tableModel.getValueAt(filaSeleccionada, 2);

        int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Está seguro que desea eliminar al cliente:\n" + nombreCliente + "?",
                "Confirmar Eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirmacion == JOptionPane.YES_OPTION) {
            try {
                // Eliminar de la base de datos
                clienteController.eliminarCliente(idCliente);

                // Eliminar de la tabla visual
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

    private void buscarClientes() {
        String textoBusqueda = txtBuscar.getText().trim().toLowerCase();
        if (textoBusqueda.isEmpty()) {
            actualizarTabla();
            return;
        }

        try {
            // Usar el método corregido del controller
            List<Cliente> clientesEncontrados = clienteController.buscarPorNombre(textoBusqueda);

            // Limpiar tabla y mostrar resultados
            tableModel.setRowCount(0);
            for (Cliente cliente : clientesEncontrados) {
                Object[] fila = {
                    cliente.getId(),
                    cliente.getNombre(),
                    cliente.getApellido(),
                    cliente.getTelefono(),
                    cliente.getEmail(),
                    cliente.getEdad(),
                    cliente.getIdZona(),
                    cliente.getFechaRegistro(),
                    cliente.isActivo() ? "Sí" : "No"
                };
                tableModel.addRow(fila);
            }

            if (tableModel.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this,
                        "No se encontraron clientes que coincidan con la búsqueda: " + textoBusqueda,
                        "Búsqueda Sin Resultados",
                        JOptionPane.INFORMATION_MESSAGE);
                actualizarTabla();
            }

        } catch (ServiceException e) {
            JOptionPane.showMessageDialog(this,
                    "Error al buscar clientes: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Actualiza la tabla de clientes con los datos más recientes de la base de
     * datos. Limpia la tabla actual y la llena con todos los clientes
     * registrados.
     */
    public void actualizarTabla() {
        try {
            tableModel.setRowCount(0); // Limpiar tabla

            // Obtener clientes reales de la base de datos - USANDO MÉTODO CORREGIDO
            List<Cliente> clientes = clienteController.listarTodos();

            // Llenar la tabla con datos reales
            for (Cliente cliente : clientes) {
                Object[] fila = {
                    cliente.getId(),
                    cliente.getNombre(),
                    cliente.getApellido(),
                    cliente.getTelefono(),
                    cliente.getEmail(),
                    cliente.getEdad(),
                    cliente.getIdZona(),
                    cliente.getFechaRegistro(),
                    cliente.isActivo() ? "Sí" : "No"
                };
                tableModel.addRow(fila);
            }

        } catch (ServiceException e) {
            JOptionPane.showMessageDialog(this,
                    "Error al cargar clientes: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}
