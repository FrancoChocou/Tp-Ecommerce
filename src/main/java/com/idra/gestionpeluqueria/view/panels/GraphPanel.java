package com.idra.gestionpeluqueria.view.panels;

import javax.swing.*;
import java.awt.*;

public class GraphPanel extends JPanel {
    
    public GraphPanel() {
        initializeUI();
    }
    
    private void initializeUI() {
        setLayout(new BorderLayout());
        setBackground(new Color(230, 240, 255));
        
        JLabel titleLabel = new JLabel("📊 Panel de Gráficos Estadísticos", JLabel.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(50, 50, 50));
        
        add(titleLabel, BorderLayout.NORTH);
        
        JPanel contentPanel = new JPanel();
        contentPanel.setBackground(new Color(230, 240, 255));
        contentPanel.add(new JLabel("Los gráficos estadísticos se cargarán aquí..."));
        
        add(contentPanel, BorderLayout.CENTER);
    }
    
    public void actualizarGraficos() {
        System.out.println("Actualizando gráficos...");
    }
}