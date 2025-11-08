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
    private JLabel lblPromedioVentas, lblDesviacionEstandar, lblCorrelacionPrecioCantidad, lblCorrelacionDiaCantidad;
    private JLabel lblFechaActual;
    private JPanel statsPanel, analyticsPanel, quickActionsPanel, chartsPanel;
    
    /**
     * Constructor que inicializa el panel del dashboard y sus componentes.
     */
    public DashboardPanel() {
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout(20, 20));
        setBackground(new Color(240, 240, 240));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Panel superior con fecha y título
        createHeaderPanel();

        // Panel de estadísticas básicas
        createStatsPanel();

        // Panel de análisis estadístico
        createAnalyticsPanel();

        // Panel de acciones rápidas
        createQuickActionsPanel();

        // Panel de gráficos (placeholder)
        createChartsPanel();

        // Layout principal
        JPanel contentPanel = new JPanel(new BorderLayout(0, 20));
        contentPanel.setBackground(new Color(240, 240, 240));

        JPanel topPanel = new JPanel(new BorderLayout(0, 20));
        topPanel.add(statsPanel, BorderLayout.CENTER);
        topPanel.add(quickActionsPanel, BorderLayout.EAST);

        JPanel middlePanel = new JPanel(new BorderLayout(0, 20));
        middlePanel.add(analyticsPanel, BorderLayout.CENTER);
        middlePanel.add(chartsPanel, BorderLayout.EAST);

        contentPanel.add(topPanel, BorderLayout.NORTH);
        contentPanel.add(middlePanel, BorderLayout.CENTER);

        add(contentPanel, BorderLayout.CENTER);
        
        // Cargar datos iniciales
        actualizarDatos();
    }

    private void createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(240, 240, 240));

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

    private void createAnalyticsPanel() {
        analyticsPanel = new JPanel(new GridLayout(2, 2, 15, 15));
        analyticsPanel.setBackground(new Color(240, 240, 240));
        analyticsPanel.setBorder(BorderFactory.createTitledBorder("📈 Análisis Estadístico - TP Estadística"));

        // Tarjeta 1: Promedio Ventas Diarias (MEDIA)
        JPanel cardPromedio = createAnalyticCard("📊 Promedio Ventas/Día", "0", new Color(52, 152, 219), "Media de ventas diarias");
        lblPromedioVentas = (JLabel) ((JPanel) cardPromedio.getComponent(1)).getComponent(0);

        // Tarjeta 2: Desviación Estándar
        JPanel cardDesviacion = createAnalyticCard("📏 Desviación Estándar", "0", new Color(155, 89, 182), "Variabilidad de ventas");
        lblDesviacionEstandar = (JLabel) ((JPanel) cardDesviacion.getComponent(1)).getComponent(0);

        // Tarjeta 3: Correlación Precio-Cantidad
        JPanel cardCorrelacion1 = createAnalyticCard("🔗 Correlación Precio-Cantidad", "0", new Color(230, 126, 34), "Coef. Pearson: precio vs cantidad");
        lblCorrelacionPrecioCantidad = (JLabel) ((JPanel) cardCorrelacion1.getComponent(1)).getComponent(0);

        // Tarjeta 4: Correlación Día-Cantidad
        JPanel cardCorrelacion2 = createAnalyticCard("📅 Correlación Día-Cantidad", "0", new Color(231, 76, 60), "Coef. Pearson: día vs cantidad");
        lblCorrelacionDiaCantidad = (JLabel) ((JPanel) cardCorrelacion2.getComponent(1)).getComponent(0);

        analyticsPanel.add(cardPromedio);
        analyticsPanel.add(cardDesviacion);
        analyticsPanel.add(cardCorrelacion1);
        analyticsPanel.add(cardCorrelacion2);
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

    private JPanel createAnalyticCard(String title, String value, Color color, String descripcion) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        titleLabel.setForeground(new Color(80, 80, 80));

        JLabel valueLabel = new JLabel(value, SwingConstants.CENTER);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        valueLabel.setForeground(color);

        JLabel descLabel = new JLabel(descripcion, SwingConstants.CENTER);
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        descLabel.setForeground(new Color(120, 120, 120));

        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);
        contentPanel.add(valueLabel, BorderLayout.CENTER);
        contentPanel.add(descLabel, BorderLayout.SOUTH);

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(contentPanel, BorderLayout.CENTER);

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

    private void createChartsPanel() {
        chartsPanel = new JPanel(new BorderLayout());
        chartsPanel.setBackground(Color.WHITE);
        chartsPanel.setPreferredSize(new Dimension(300, 0));
        chartsPanel.setBorder(BorderFactory.createTitledBorder("📊 Visualizaciones"));

        JLabel placeholder = new JLabel("<html><center>📈<br>Gráficos Estadísticos<br><small>Disponibles en la versión completa</small></center></html>", SwingConstants.CENTER);
        placeholder.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        placeholder.setForeground(new Color(150, 150, 150));
        placeholder.setBorder(BorderFactory.createEmptyBorder(50, 20, 50, 20));

        chartsPanel.add(placeholder, BorderLayout.CENTER);
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
            // Controladores
            ClienteController clienteController = new ClienteController();
            ProductoController productoController = new ProductoController();
            VentaController ventaController = new VentaController();

            // Datos básicos
            int totalClientes = clienteController.buscarTodosClientes().size();
            int totalProductos = productoController.buscarProductosActivos().size();
            int ventasHoy = ventaController.buscarVentasPorFecha(LocalDate.now()).size();
            double ingresosHoy = ventaController.calcularTotalVentasHoy();

            // Análisis estadístico (últimos 30 días)
            LocalDate fechaFin = LocalDate.now();
            LocalDate fechaInicio = fechaFin.minusDays(30);
            
            double promedioVentas = ventaController.calcularPromedioVentasDiarias(fechaInicio, fechaFin);
            double desviacionEstandar = ventaController.calcularDesviacionEstandarVentas(fechaInicio, fechaFin);
            double correlacionPrecioCantidad = ventaController.calcularCorrelacionPrecioCantidad();
            double correlacionDiaCantidad = ventaController.calcularCorrelacionDiaCantidad();

            // Actualizar labels
            lblTotalClientes.setText(String.valueOf(totalClientes));
            lblTotalProductos.setText(String.valueOf(totalProductos));
            lblVentasHoy.setText(String.valueOf(ventasHoy));
            lblIngresosHoy.setText("$" + String.format("%.2f", ingresosHoy));
            
            lblPromedioVentas.setText(String.format("%.2f", promedioVentas));
            lblDesviacionEstandar.setText(String.format("%.2f", desviacionEstandar));
            lblCorrelacionPrecioCantidad.setText(String.format("%.3f", correlacionPrecioCantidad));
            lblCorrelacionDiaCantidad.setText(String.format("%.3f", correlacionDiaCantidad));
            
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
        lblPromedioVentas.setText("0.00");
        lblDesviacionEstandar.setText("0.00");
        lblCorrelacionPrecioCantidad.setText("0.000");
        lblCorrelacionDiaCantidad.setText("0.000");
    }

    private void actualizarFecha() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, d 'de' MMMM 'de' yyyy");
        String fechaFormateada = LocalDate.now().format(formatter);
        lblFechaActual.setText(fechaFormateada);
    }
}