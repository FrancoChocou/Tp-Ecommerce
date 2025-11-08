package com.idra.gestionpeluqueria.view;

import com.idra.gestionpeluqueria.view.panels.*; // ← ESTE IMPORT FALTABA
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Ventana principal de la aplicacion de gestion de peluqueria.
 * Implementa la navegacion entre diferentes paneles utilizando CardLayout
 * y proporciona una barra de navegacion para acceder a las diferentes secciones.
 * 
 * @author Idra
 */
public class MainFrame extends JFrame {
    private JPanel mainPanel;
    private CardLayout cardLayout;

    // Paneles
    private DashboardPanel dashboardPanel;
    private ClientePanel clientePanel;
    private ProductoPanel servicioPanel;
    private VentaPanel turnoPanel;

    // Barra de navegación
    private JButton btnDashboard, btnClientes, btnServicios, btnTurnos, btnSalir;
    
     /**
     * Constructor que inicializa la ventana principal y configura todos sus componentes.
     */
    public MainFrame() {
        initializeUI();
        setupEventListeners();
    }

    private void initializeUI() {
        setTitle("Sistema de Gestión de Peluquería - Idra");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1400, 900);
        setLocationRelativeTo(null);

        // Configurar layout principal
        setLayout(new BorderLayout());
        
        // Crear barra de navegación
        createNavigationBar(); // ← CORREGÍ EL NOMBRE (estaba createNavigacionBar)

        // Crear panel principal con CardLayout
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Inicializar paneles
        dashboardPanel = new DashboardPanel();
        clientePanel = new ClientePanel();
        servicioPanel = new ProductoPanel();
        turnoPanel = new VentaPanel();

        // Agregar paneles al CardLayout
        mainPanel.add(dashboardPanel, "DASHBOARD");
        mainPanel.add(clientePanel, "CLIENTES");
        mainPanel.add(servicioPanel, "SERVICIOS");
        mainPanel.add(turnoPanel, "TURNOS");

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

        // Crear botones con estilo
        btnDashboard = createNavButton("🏠 Dashboard");
        btnClientes = createNavButton("👥 Clientes");
        btnServicios = createNavButton("✂️ Servicios");
        btnTurnos = createNavButton("📅 Turnos");
        btnSalir = createNavButton("🚪 Salir");
        btnSalir.setBackground(new Color(220, 53, 69));

        navPanel.add(btnDashboard);
        navPanel.add(Box.createHorizontalStrut(10));
        navPanel.add(btnClientes);
        navPanel.add(Box.createHorizontalStrut(10));
        navPanel.add(btnServicios);
        navPanel.add(Box.createHorizontalStrut(10));
        navPanel.add(btnTurnos);
        navPanel.add(Box.createHorizontalStrut(50));
        navPanel.add(btnSalir);

        add(navPanel, BorderLayout.NORTH);
    }

    private JButton createNavButton(String text) {
    JButton button = new JButton(text);
    button.setFont(new Font("Segoe UI", Font.BOLD, 14));
    button.setBackground(new Color(70, 70, 70));
    button.setForeground(Color.WHITE);
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
                button.setBackground(new Color(90, 90, 90));
                button.setForeground(Color.WHITE); // Forzar color blanco
            }
        }
        
        public void mouseExited(java.awt.event.MouseEvent evt) {
            if (!button.getBackground().equals(new Color(70, 130, 180))) {
                button.setBackground(new Color(70, 70, 70));
                button.setForeground(Color.WHITE); // Forzar color blanco
            }
        }
    });

    return button;
}

    private void setupEventListeners() {
        btnDashboard.addActionListener(e -> showPanel("DASHBOARD", btnDashboard));
        btnClientes.addActionListener(e -> showPanel("CLIENTES", btnClientes));
        btnServicios.addActionListener(e -> showPanel("SERVICIOS", btnServicios));
        btnTurnos.addActionListener(e -> showPanel("TURNOS", btnTurnos));
        
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
        Color defaultBg = new Color(70, 70, 70);
        Color defaultFg = Color.WHITE;
        
        btnDashboard.setBackground(defaultBg);
        btnClientes.setBackground(defaultBg);
        btnServicios.setBackground(defaultBg);
        btnTurnos.setBackground(defaultBg);
        
        btnDashboard.setForeground(defaultFg);
        btnClientes.setForeground(defaultFg);
        btnServicios.setForeground(defaultFg);
        btnTurnos.setForeground(defaultFg);
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
                clientePanel.actualizarTabla(); // ← Esto debe llamarse
            }
            break;
        case "SERVICIOS":
            if (servicioPanel != null) {
                servicioPanel.actualizarTabla();
            }
            break;
        case "TURNOS":
            if (turnoPanel != null) {
                turnoPanel.actualizarTabla();
            }
            break;
    }
}
}