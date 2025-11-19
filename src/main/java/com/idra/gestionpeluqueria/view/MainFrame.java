package com.idra.gestionpeluqueria.view;

import com.idra.gestionpeluqueria.view.panels.*;
import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private JPanel mainPanel;
    private CardLayout cardLayout;

    // Paneles
    private DashboardPanel dashboardPanel;
    private ClientePanel clientePanel;
    private ProductoPanel servicioPanel;
    private VentaPanel VentaPanel;
    private GraphPanel graphPanel;

    // Barra de navegación 
    private JButton btnDashboard, btnClientes, btnServicios, btnVentas, btnGraficos, btnSalir;

    public MainFrame() {
        initializeUI();
        setupEventListeners();
    }

    public void mostrarPanelGraficos() {
        showPanel("GRAFICOS", btnGraficos);
    }

    private void initializeUI() {
        setTitle("Sistema de Gestión de E-commerce - Idra");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1400, 900);
        setLocationRelativeTo(null);

        // Configurar layout principal
        setLayout(new BorderLayout());

        // Crear barra de navegación
        createNavigationBar();

        // Crear panel principal con CardLayout
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Inicializar paneles
        dashboardPanel = new DashboardPanel();
        clientePanel = new ClientePanel();
        servicioPanel = new ProductoPanel();
        VentaPanel = new VentaPanel();
        graphPanel = new GraphPanel();

        mainPanel.add(dashboardPanel, "DASHBOARD");
        mainPanel.add(clientePanel, "CLIENTES");
        mainPanel.add(servicioPanel, "SERVICIOS");
        mainPanel.add(VentaPanel, "Ventas");
        mainPanel.add(graphPanel, "GRAFICOS"); 

        add(mainPanel, BorderLayout.CENTER);

        // Mostrar dashboard por defecto
        cardLayout.show(mainPanel, "DASHBOARD");
        btnDashboard.setBackground(new Color(70, 130, 180));
        btnDashboard.setForeground(Color.WHITE);
    }

    private void createNavigationBar() {
        JPanel navPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        navPanel.setBackground(new Color(50, 50, 50));
        navPanel.setPreferredSize(new Dimension(getWidth(), 60));
        navPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        btnDashboard = createNavButton("Dashboard", new Color(70, 130, 180));
        btnClientes = createNavButton("Clientes", new Color(39, 174, 96));
        btnServicios = createNavButton("Productos", new Color(155, 89, 182));
        btnVentas = createNavButton("Ventas", new Color(241, 196, 15));
        btnGraficos = createNavButton("Gráficos", new Color(230, 126, 34));
        btnSalir = createNavButton("Salir", new Color(231, 76, 60));

        navPanel.add(btnDashboard);
        navPanel.add(Box.createHorizontalStrut(10));
        navPanel.add(btnClientes);
        navPanel.add(Box.createHorizontalStrut(10));
        navPanel.add(btnServicios);
        navPanel.add(Box.createHorizontalStrut(10));
        navPanel.add(btnVentas);
        navPanel.add(Box.createHorizontalStrut(10));
        navPanel.add(btnGraficos);
        navPanel.add(Box.createHorizontalStrut(50));
        navPanel.add(btnSalir);

        add(navPanel, BorderLayout.NORTH);
    }

    private JButton createNavButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(color);
        button.setForeground(Color.BLACK); 
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(100, 100, 100)),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setOpaque(true);
        button.setContentAreaFilled(true);
        button.setBorderPainted(true);

        // Efecto hover
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (!button.getBackground().equals(new Color(70, 130, 180))) {
                    Color hoverColor = color.brighter();
                    button.setBackground(hoverColor);
                    button.setForeground(Color.BLACK); 
                }
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (!button.getBackground().equals(new Color(70, 130, 180))) {
                    button.setBackground(color);
                    button.setForeground(Color.BLACK);  
                }
            }
        });

        return button;
    }

    private void setupEventListeners() {
        btnDashboard.addActionListener(e -> showPanel("DASHBOARD", btnDashboard));
        btnClientes.addActionListener(e -> showPanel("CLIENTES", btnClientes));
        btnServicios.addActionListener(e -> showPanel("SERVICIOS", btnServicios));
        btnVentas.addActionListener(e -> showPanel("Ventas", btnVentas));
        btnGraficos.addActionListener(e -> showPanel("GRAFICOS", btnGraficos));

        btnSalir.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "¿Está seguro que desea salir del sistema?",
                    "Confirmar Salida",
                    JOptionPane.YES_NO_OPTION
            );
            if (confirm == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });
    }

    private void showPanel(String panelName, JButton activeButton) {
        // Resetear todos los botones
        resetNavButtons();

        // Resaltar botón activo
        activeButton.setBackground(new Color(70, 130, 180));
        activeButton.setForeground(Color.WHITE);

        // Mostrar panel
        cardLayout.show(mainPanel, panelName);

        // Actualizar datos si es necesario
        updatePanelData(panelName);
    }

    private void resetNavButtons() {
        // Restaurar colores originales
        btnDashboard.setBackground(new Color(70, 130, 180));
        btnClientes.setBackground(new Color(39, 174, 96));
        btnServicios.setBackground(new Color(155, 89, 182));
        btnVentas.setBackground(new Color(241, 196, 15));
        btnGraficos.setBackground(new Color(230, 126, 34));

        btnDashboard.setForeground(Color.WHITE);
        btnClientes.setForeground(Color.WHITE);
        btnServicios.setForeground(Color.WHITE);
        btnVentas.setForeground(Color.WHITE);
        btnGraficos.setForeground(Color.WHITE);
    }

    private void updatePanelData(String panelName) {
        switch (panelName) {
            case "DASHBOARD":
                if (dashboardPanel != null) {
                    dashboardPanel.actualizarDatos();
                }
                break;
            case "CLIENTES":
                if (clientePanel != null) {
                    clientePanel.actualizarTabla();
                }
                break;
            case "SERVICIOS":
                if (servicioPanel != null) {
                    servicioPanel.actualizarTabla();
                }
                break;
            case "Ventas":
                if (VentaPanel != null) {
                    VentaPanel.actualizarTabla();
                }
                break;
            case "GRAFICOS":
                if (graphPanel != null) {
                    graphPanel.actualizarGraficos();  
                }
                break;
        }
    }
}
