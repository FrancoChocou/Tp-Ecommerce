package com.idra.gestionpeluqueria.view.panels;

import com.idra.gestionpeluqueria.controller.ClienteController;
import com.idra.gestionpeluqueria.controller.ProductoController;
import com.idra.gestionpeluqueria.controller.VentaController;
import com.idra.gestionpeluqueria.exception.ServiceException;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Panel principal (dashboard) de la aplicacion de e-commerce
 * Muestra estadisticas generales del negocio incluyendo metricas de ventas,
 * analisis estadisticos y graficos para el trabajo practico de Estadistica.
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
        quickActionsPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        quickActionsPanel.setBackground(new Color(240, 240, 240));
        quickActionsPanel.setPreferredSize(new Dimension(250, 0));
        quickActionsPanel.setBorder(BorderFactory.createTitledBorder("🚀 Acciones Rápidas"));

        JButton btnNuevaVenta = createActionButton("🛒 Nueva Venta", new Color(39, 174, 96));
        JButton btnVerReportes = createActionButton("📈 Ver Reportes", new Color(41, 128, 185));
        JButton btnActualizar = createActionButton("🔄 Actualizar Datos", new Color(243, 156, 18));

        quickActionsPanel.add(btnNuevaVenta);
        quickActionsPanel.add(btnVerReportes);
        quickActionsPanel.add(btnActualizar);

        // Event listeners
        btnNuevaVenta.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, 
                "Funcionalidad: Abrir diálogo para nueva venta", 
                "Nueva Venta", 
                JOptionPane.INFORMATION_MESSAGE);
        });
        
        btnVerReportes.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, 
                "Funcionalidad: Generar reportes estadísticos", 
                "Reportes", 
                JOptionPane.INFORMATION_MESSAGE);
        });
        
        btnActualizar.addActionListener(e -> {
            actualizarDatos();
            JOptionPane.showMessageDialog(this, 
                "Datos actualizados correctamente", 
                "Actualización", 
                JOptionPane.INFORMATION_MESSAGE);
        });
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
     * Actualiza todos los datos mostrados en el dashboard.
     * Refresca las estadísticas y análisis con información actualizada desde la base de datos.
     */
    public void actualizarDatos() {
    actualizarFecha();
    
    try {
        // SOLO DATOS BÁSICOS - QUITAR ANÁLISIS ESTADÍSTICOS
        int totalClientes = clienteController.listarTodos().size();
        int totalProductos = productoController.buscarProductosActivos().size();
        int ventasHoy = ventaController.buscarVentasPorFecha(LocalDate.now()).size();
        double ingresosHoy = ventaController.calcularTotalVentasHoy();

        // Actualizar labels SOLO con datos básicos
        lblTotalClientes.setText(String.valueOf(totalClientes));
        lblTotalProductos.setText(String.valueOf(totalProductos));
        lblVentasHoy.setText(String.valueOf(ventasHoy));
        lblIngresosHoy.setText("$" + String.format("%.2f", ingresosHoy));
        
    } catch (ServiceException e) {
        System.err.println("❌ Error al actualizar datos del dashboard: " + e.getMessage());
        // Valores por defecto en caso de error
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
}