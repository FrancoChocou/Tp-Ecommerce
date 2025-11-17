package com.idra.gestionpeluqueria.view.panels;

import com.idra.gestionpeluqueria.controller.ClienteController;
import com.idra.gestionpeluqueria.controller.ProductoController;
import com.idra.gestionpeluqueria.controller.VentaController;
import com.idra.gestionpeluqueria.exception.ServiceException;
import com.idra.gestionpeluqueria.service.DataGeneratorService;
import com.idra.gestionpeluqueria.view.MainFrame;
import com.idra.gestionpeluqueria.view.dialogs.VentaDialog;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Panel principal (dashboard) de la aplicacion de e-commerce Muestra
 * estadisticas generales del negocio incluyendo metricas de ventas, analisis
 * estadisticos y graficos para el trabajo practico de Estadistica.
 *
 * @author Idra
 */
public class DashboardPanel extends JPanel {

    private JLabel lblTotalClientes, lblTotalProductos, lblVentasHoy, lblIngresosHoy;
    private JLabel lblFechaActual;
    private JPanel statsPanel, quickActionsPanel;
    private ClienteController clienteController;
    private ProductoController productoController;
    private VentaController ventaController;

    /**
     * Constructor que inicializa el panel del dashboard y sus componentes.
     */
    public DashboardPanel() {
        this.clienteController = new ClienteController();
        this.productoController = new ProductoController();
        this.ventaController = new VentaController();
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(230, 240, 255));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Panel superior con fecha y título
        createHeaderPanel();

        // Panel de estadísticas básicas
        createStatsPanel();

        // Panel de acciones rápidas
        createQuickActionsPanel();

        // Layout principal - SOLO ESTADÍSTICAS BÁSICAS
        JPanel contentPanel = new JPanel(new BorderLayout(0, 20));
        contentPanel.setBackground(new Color(230, 240, 255));

        JPanel topPanel = new JPanel(new BorderLayout(0, 20));
        topPanel.add(statsPanel, BorderLayout.CENTER);
        topPanel.add(quickActionsPanel, BorderLayout.EAST);

        contentPanel.add(topPanel, BorderLayout.NORTH);

        // Mensaje informativo
        JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.setBackground(new Color(230, 240, 255));
        JLabel infoLabel = new JLabel("<html><center><h3>📊 Análisis Estadísticos</h3><p>Los análisis estadísticos completos están disponibles en el panel 'Gráficos'</p></center></html>", SwingConstants.CENTER);
        infoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        infoLabel.setForeground(new Color(100, 100, 100));
        infoPanel.add(infoLabel, BorderLayout.CENTER);

        contentPanel.add(infoPanel, BorderLayout.CENTER);

        add(contentPanel, BorderLayout.CENTER);

        // Cargar datos iniciales
        actualizarDatos();
    }

    private void createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(230, 240, 255)); // CELESTE CLARO

        JLabel titleLabel = new JLabel("📊 Dashboard E-commerce - Análisis Estadístico");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(new Color(50, 50, 50));

        lblFechaActual = new JLabel();
        lblFechaActual.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblFechaActual.setForeground(new Color(100, 100, 100));
        actualizarFecha();

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(lblFechaActual, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);
    }

    private void createStatsPanel() {
        statsPanel = new JPanel(new GridLayout(2, 2, 15, 15));
        statsPanel.setBackground(new Color(240, 240, 240));

        // Tarjeta 1: Total Clientes
        JPanel cardClientes = createStatCard("👥 Total Clientes", "0", new Color(41, 128, 185));
        lblTotalClientes = (JLabel) ((JPanel) cardClientes.getComponent(1)).getComponent(0);

        // Tarjeta 2: Total Productos
        JPanel cardProductos = createStatCard("📦 Productos Activos", "0", new Color(39, 174, 96));
        lblTotalProductos = (JLabel) ((JPanel) cardProductos.getComponent(1)).getComponent(0);

        // Tarjeta 3: Ventas Hoy
        JPanel cardVentas = createStatCard("🛒 Ventas Hoy", "0", new Color(243, 156, 18));
        lblVentasHoy = (JLabel) ((JPanel) cardVentas.getComponent(1)).getComponent(0);

        // Tarjeta 4: Ingresos Hoy
        JPanel cardIngresos = createStatCard("💰 Ingresos Hoy", "$0.00", new Color(231, 76, 60));
        lblIngresosHoy = (JLabel) ((JPanel) cardIngresos.getComponent(1)).getComponent(0);

        statsPanel.add(cardClientes);
        statsPanel.add(cardProductos);
        statsPanel.add(cardVentas);
        statsPanel.add(cardIngresos);
    }

    private JPanel createStatCard(String title, String value, Color color) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        titleLabel.setForeground(new Color(100, 100, 100));

        JLabel valueLabel = new JLabel(value, SwingConstants.CENTER);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        valueLabel.setForeground(color);

        JPanel valuePanel = new JPanel(new BorderLayout());
        valuePanel.setBackground(Color.WHITE);
        valuePanel.add(valueLabel, BorderLayout.CENTER);

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valuePanel, BorderLayout.CENTER);

        return card;
    }

    private void createQuickActionsPanel() {
        quickActionsPanel = new JPanel(new GridLayout(5, 1, 10, 10)); // Cambiado a 5 filas
        quickActionsPanel.setBackground(new Color(240, 240, 240));
        quickActionsPanel.setPreferredSize(new Dimension(250, 0));
        quickActionsPanel.setBorder(BorderFactory.createTitledBorder("Acciones Rapidas"));

        JButton btnNuevaVenta = createActionButton("Nueva Venta", new Color(39, 174, 96));
        JButton btnVerReportes = createActionButton("Ver Reportes", new Color(41, 128, 185));
        JButton btnGenerarDatos = createActionButton("Generar Datos Prueba", new Color(155, 89, 182));
        JButton btnLimpiarDatos = createActionButton("Limpiar Datos Prueba", new Color(231, 76, 60)); // NUEVO BOTÓN
        JButton btnActualizar = createActionButton("Actualizar Datos", new Color(243, 156, 18));

        quickActionsPanel.add(btnNuevaVenta);
        quickActionsPanel.add(btnVerReportes);
        quickActionsPanel.add(btnGenerarDatos);
        quickActionsPanel.add(btnLimpiarDatos); // NUEVO BOTÓN
        quickActionsPanel.add(btnActualizar);

        // Event listeners
        btnNuevaVenta.addActionListener(e -> {
            // Abrir diálogo REAL de nueva venta
            Window parentWindow = SwingUtilities.getWindowAncestor(DashboardPanel.this);
            JFrame parentFrame = null;
            if (parentWindow instanceof JFrame) {
                parentFrame = (JFrame) parentWindow;
            }

            VentaDialog dialog = new VentaDialog(parentFrame, "Nueva Venta", null);
            dialog.setVisible(true);

            if (dialog.isGuardadoExitoso()) {
                actualizarDatos(); // Refrescar dashboard
                JOptionPane.showMessageDialog(DashboardPanel.this,
                        "Venta registrada exitosamente!",
                        "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });

        btnVerReportes.addActionListener(e -> {
            // Navegar al panel de Gráficos
            Container parent = getParent();
            while (parent != null && !(parent instanceof MainFrame)) {
                parent = parent.getParent();
            }
            if (parent instanceof MainFrame) {
                ((MainFrame) parent).mostrarPanelGraficos();
            }
        });

        btnGenerarDatos.addActionListener(e -> generarDatosPrueba());

        btnActualizar.addActionListener(e -> {
            actualizarDatos();
            JOptionPane.showMessageDialog(this,
                    "Datos actualizados correctamente",
                    "Actualización",
                    JOptionPane.INFORMATION_MESSAGE);
        });

        btnLimpiarDatos.addActionListener(e -> limpiarDatosPrueba());
    }

    private void limpiarDatosPrueba() {
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "<html><b>¿Estás seguro de limpiar los datos de prueba?</b><br><br>"
                + "Esto eliminará:<br>"
                + "• Todas las ventas generadas<br>"
                + "• Productos de prueba<br>"
                + "• Clientes de prueba<br><br>"
                + "<font color='red'>Esta acción no se puede deshacer</font></html>",
                "Confirmar Limpieza de Datos",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) {
            new Thread(() -> {
                try {
                    DataGeneratorService generator = new DataGeneratorService();
                    generator.limpiarDatosPrueba();

                    SwingUtilities.invokeLater(() -> {
                        actualizarDatos(); // Actualizar estadísticas
                        JOptionPane.showMessageDialog(this,
                                "✅ Datos de prueba eliminados exitosamente!\n\n"
                                + "Todos los datos generados han sido limpiados.\n"
                                + "Puedes generar nuevos datos cuando quieras.",
                                "Limpieza Completada",
                                JOptionPane.INFORMATION_MESSAGE);
                    });
                } catch (Exception e) {
                    SwingUtilities.invokeLater(() -> {
                        JOptionPane.showMessageDialog(this,
                                "❌ Error al limpiar datos: " + e.getMessage(),
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                    });
                }
            }).start();
        }
    }

    private JButton createActionButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
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

    /**
     * Actualiza todos los datos mostrados en el dashboard. Refresca las
     * estadísticas y análisis con información actualizada desde la base de
     * datos.
     */
    public void actualizarDatos() {
        actualizarFecha();

        try {
            int totalClientes = clienteController.listarTodos().size();
            int totalProductos = productoController.buscarProductosActivos().size();
            int ventasHoy = ventaController.buscarVentasPorFecha(LocalDate.now()).size();
            double ingresosHoy = ventaController.calcularTotalVentasHoy();

            lblTotalClientes.setText(String.valueOf(totalClientes));
            lblTotalProductos.setText(String.valueOf(totalProductos));
            lblVentasHoy.setText(String.valueOf(ventasHoy));
            lblIngresosHoy.setText("$" + String.format("%.2f", ingresosHoy));

        } catch (ServiceException e) {
            System.err.println("❌ Error al actualizar datos del dashboard: " + e.getMessage());
            setValoresPorDefecto();
        }
    }

    private void setValoresPorDefecto() {
        lblTotalClientes.setText("0");
        lblTotalProductos.setText("0");
        lblVentasHoy.setText("0");
        lblIngresosHoy.setText("$0.00");
    }

    private void actualizarFecha() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, d 'de' MMMM 'de' yyyy");
        String fechaFormateada = LocalDate.now().format(formatter);
        lblFechaActual.setText(fechaFormateada);
    }

    private void generarDatosPrueba() {
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "¿Generar datos de prueba? Esto creará clientes, productos y ventas ficticias para análisis estadístico.",
                "Generar Datos Prueba",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            new Thread(() -> {
                try {
                    DataGeneratorService generator = new DataGeneratorService();
                    generator.generarDatosPrueba();

                    SwingUtilities.invokeLater(() -> {
                        actualizarDatos(); // Actualizar estadísticas
                        JOptionPane.showMessageDialog(this,
                                "✅ Datos de prueba generados exitosamente!\n\n"
                                + "• 20 clientes creados\n"
                                + "• 25 productos creados\n"
                                + "• 80 ventas generadas\n\n"
                                + "Ahora puedes usar el panel 'Gráficos' para analizar los datos estadísticos.",
                                "Generación Completada",
                                JOptionPane.INFORMATION_MESSAGE);
                    });
                } catch (Exception e) {
                    SwingUtilities.invokeLater(() -> {
                        JOptionPane.showMessageDialog(this,
                                "❌ Error al generar datos: " + e.getMessage(),
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                    });
                }
            }).start();
        }
    }
}
