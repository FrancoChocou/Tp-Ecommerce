package com.idra.gestionpeluqueria.view.panels;

import com.idra.gestionpeluqueria.controller.VentaController;
import com.idra.gestionpeluqueria.controller.ProductoController;
import com.idra.gestionpeluqueria.controller.ClienteController;
import com.idra.gestionpeluqueria.exception.ServiceException;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GraphPanel extends JPanel {
    
    private JPanel graficosPanel;
    private JTextArea statsTextArea;
    private VentaController ventaController;
    private ProductoController productoController;
    private ClienteController clienteController;
    
    public GraphPanel() {
        this.ventaController = new VentaController();
        this.productoController = new ProductoController();
        this.clienteController = new ClienteController();
        initializeUI();
    }
    
    private void initializeUI() {
        setLayout(new BorderLayout());
        setBackground(new Color(230, 240, 255));
        
        // Título
        JLabel titleLabel = new JLabel("Panel de Gráficos Estadísticos", JLabel.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(50, 50, 50));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        
        add(titleLabel, BorderLayout.NORTH);
        
        // Panel de botones
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(new Color(230, 240, 255));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        
        JButton btnActualizar = new JButton("Actualizar Gráficos");
        btnActualizar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnActualizar.setBackground(new Color(41, 128, 185));
        btnActualizar.setForeground(Color.WHITE);
        btnActualizar.addActionListener(e -> actualizarGraficos());
        
        buttonPanel.add(btnActualizar);
        add(buttonPanel, BorderLayout.SOUTH);
        
        // Panel principal 
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        graficosPanel = new JPanel();
        graficosPanel.setLayout(new BoxLayout(graficosPanel, BoxLayout.Y_AXIS));
        graficosPanel.setBackground(new Color(230, 240, 255));
        graficosPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        
        scrollPane.setViewportView(graficosPanel);
        add(scrollPane, BorderLayout.CENTER);
        
        // Panel de estadísticas
        crearPanelEstadisticas();
        
        // Mostrar mensaje inicial
        mostrarMensajeInicial();
    }
    
    private void crearPanelEstadisticas() {
        JPanel statsPanel = new JPanel(new BorderLayout());
        statsPanel.setBackground(Color.WHITE);
        statsPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("Estadísticas Calculadas"),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        statsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 180));
        
        statsTextArea = new JTextArea();
        statsTextArea.setFont(new Font("Consolas", Font.PLAIN, 12));
        statsTextArea.setBackground(new Color(250, 250, 250));
        statsTextArea.setEditable(false);
        statsTextArea.setText("Haz clic en 'Actualizar Gráficos' para calcular las estadísticas...");
        
        JScrollPane statsScroll = new JScrollPane(statsTextArea);
        statsScroll.setPreferredSize(new Dimension(0, 150));
        
        statsPanel.add(statsScroll, BorderLayout.CENTER);
        graficosPanel.add(statsPanel);
        graficosPanel.add(Box.createVerticalStrut(10));
    }
    
    private void mostrarMensajeInicial() {
        JPanel mensajePanel = new JPanel(new BorderLayout());
        mensajePanel.setBackground(Color.WHITE);
        mensajePanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        mensajePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        
        JLabel mensaje = new JLabel(
            "<html><center><b>Genera datos de prueba primero</b><br>" +
            "Ve al Dashboard y haz clic en 'Generar Datos Prueba' para crear datos de análisis</center></html>", 
            JLabel.CENTER
        );
        mensaje.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        mensaje.setForeground(new Color(100, 100, 100));
        
        mensajePanel.add(mensaje, BorderLayout.CENTER);
        graficosPanel.add(mensajePanel);
    }
    
    private void crearGraficoBarras(String titulo, String[] categorias, double[] valores) {
        JPanel chartPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                int padding = 60;
                int chartWidth = getWidth() - 2 * padding;
                int chartHeight = getHeight() - 2 * padding;
                
                // Encontrar valor máximo para escalar
                double maxVal = 0;
                for (double val : valores) {
                    if (val > maxVal) maxVal = val;
                }
                if (maxVal == 0) maxVal = 1;
                
                // Dibujar ejes
                g2d.setColor(Color.BLACK);
                g2d.drawLine(padding, padding, padding, padding + chartHeight); // Eje Y
                g2d.drawLine(padding, padding + chartHeight, padding + chartWidth, padding + chartHeight); // Eje X
                
                // Dibujar barras
                int barWidth = Math.max(20, chartWidth / Math.max(categorias.length, 1));
                for (int i = 0; i < categorias.length; i++) {
                    int barHeight = (int) ((valores[i] / maxVal) * chartHeight);
                    int x = padding + i * barWidth;
                    int y = padding + chartHeight - barHeight;
                    
                    // Color de la barra
                    Color barColor = new Color(
                        (i * 50) % 255, 
                        (i * 80 + 100) % 255, 
                        (i * 120 + 150) % 255
                    );
                    g2d.setColor(barColor);
                    g2d.fillRect(x + 2, y, barWidth - 4, barHeight);
                    
                    // Etiqueta de valor
                    g2d.setColor(Color.BLACK);
                    g2d.setFont(new Font("Segoe UI", Font.PLAIN, 10));
                    String valueLabel = String.format("$%.0f", valores[i]);
                    int textWidth = g2d.getFontMetrics().stringWidth(valueLabel);
                    g2d.drawString(valueLabel, x + barWidth/2 - textWidth/2, y - 5);
                    
                    // Etiqueta de categoría 
                    g2d.setFont(new Font("Segoe UI", Font.PLAIN, 10));
                    String shortLabel = categorias[i].length() > 8 ? categorias[i].substring(0, 8) + "..." : categorias[i];
                    int labelWidth = g2d.getFontMetrics().stringWidth(shortLabel);
                    g2d.drawString(shortLabel, x + barWidth/2 - labelWidth/2, padding + chartHeight + 15);
                }
                
                // Título
                g2d.setFont(new Font("Segoe UI", Font.BOLD, 14));
                int titleWidth = g2d.getFontMetrics().stringWidth(titulo);
                g2d.drawString(titulo, getWidth()/2 - titleWidth/2, 25);
            }
        };
        
        chartPanel.setPreferredSize(new Dimension(600, 400));
        chartPanel.setBackground(Color.WHITE);
        chartPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        agregarGraficoAlPanel(chartPanel, titulo);
    }
    
    private void crearGraficoLineas(String titulo, String[] labels, double[] valores) {
        JPanel chartPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                int padding = 60;
                int chartWidth = getWidth() - 2 * padding;
                int chartHeight = getHeight() - 2 * padding;
                
                // Encontrar valor máximo para escalar
                double maxVal = 0;
                for (double val : valores) {
                    if (val > maxVal) maxVal = val;
                }
                if (maxVal == 0) maxVal = 1;
                
                // Dibujar ejes
                g2d.setColor(Color.BLACK);
                g2d.drawLine(padding, padding, padding, padding + chartHeight);
                g2d.drawLine(padding, padding + chartHeight, padding + chartWidth, padding + chartHeight);
                
                // Dibujar línea
                g2d.setColor(Color.BLUE);
                int[] xPoints = new int[valores.length];
                int[] yPoints = new int[valores.length];
                
                for (int i = 0; i < valores.length; i++) {
                    xPoints[i] = padding + (i * chartWidth) / (Math.max(valores.length - 1, 1));
                    yPoints[i] = padding + chartHeight - (int)((valores[i] / maxVal) * chartHeight);
                    
                    // Puntos
                    g2d.setColor(Color.RED);
                    g2d.fillOval(xPoints[i] - 3, yPoints[i] - 3, 6, 6);
                    
                    // Etiquetas de valor
                    g2d.setColor(Color.BLACK);
                    g2d.setFont(new Font("Segoe UI", Font.PLAIN, 10));
                    String valueText = String.format("$%.1f", valores[i]);
                    int valueWidth = g2d.getFontMetrics().stringWidth(valueText);
                    g2d.drawString(valueText, xPoints[i] - valueWidth/2, yPoints[i] - 10);
                    
                    // Etiquetas del eje X
                    if (i < labels.length) {
                        g2d.drawString(labels[i], xPoints[i] - 10, padding + chartHeight + 15);
                    }
                }
                
                // Conectar puntos
                g2d.setColor(Color.BLUE);
                g2d.setStroke(new BasicStroke(2));
                for (int i = 0; i < valores.length - 1; i++) {
                    g2d.drawLine(xPoints[i], yPoints[i], xPoints[i + 1], yPoints[i + 1]);
                }
                
                // Título
                g2d.setFont(new Font("Segoe UI", Font.BOLD, 14));
                int titleWidth = g2d.getFontMetrics().stringWidth(titulo);
                g2d.drawString(titulo, getWidth()/2 - titleWidth/2, 25);
            }
        };
        
        chartPanel.setPreferredSize(new Dimension(600, 400));
        chartPanel.setBackground(Color.WHITE);
        chartPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        agregarGraficoAlPanel(chartPanel, titulo);
    }
    
    private void crearGraficoTorta(String titulo, String[] categorias, double[] valores) {
        JPanel chartPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                int centerX = getWidth() / 2;
                int centerY = getHeight() / 2;
                int radius = Math.min(getWidth(), getHeight()) / 3;
                
                double total = 0;
                for (double val : valores) total += val;
                if (total == 0) total = 1; 
                
                int startAngle = 0;
                for (int i = 0; i < valores.length; i++) {
                    int arcAngle = (int) (360 * valores[i] / total);
                    if (arcAngle == 0 && valores[i] > 0) arcAngle = 1; 
                    
                    Color sliceColor = new Color(
                        (i * 50) % 255, 
                        (i * 80 + 100) % 255, 
                        (i * 120 + 150) % 255
                    );
                    g2d.setColor(sliceColor);
                    g2d.fillArc(centerX - radius, centerY - radius, 2 * radius, 2 * radius, startAngle, arcAngle);
                    
                    // Leyenda
                    int legendX = 20;
                    int legendY = 30 + i * 25;
                    g2d.fillRect(legendX, legendY, 15, 15);
                    g2d.setColor(Color.BLACK);
                    g2d.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                    String legendText = String.format("%s: %.1f%% (%.0f)", 
                        categorias[i], (valores[i] / total * 100), valores[i]);
                    g2d.drawString(legendText, legendX + 20, legendY + 12);
                    
                    startAngle += arcAngle;
                }
                
                // Título
                g2d.setFont(new Font("Segoe UI", Font.BOLD, 14));
                int titleWidth = g2d.getFontMetrics().stringWidth(titulo);
                g2d.drawString(titulo, getWidth()/2 - titleWidth/2, 25);
            }
        };
        
        chartPanel.setPreferredSize(new Dimension(500, 400));
        chartPanel.setBackground(Color.WHITE);
        chartPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        agregarGraficoAlPanel(chartPanel, titulo);
    }
    
    private void agregarGraficoAlPanel(JPanel chartPanel, String titulo) {
        JPanel container = new JPanel(new BorderLayout());
        container.setBackground(new Color(230, 240, 255));
        container.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        container.setMaximumSize(new Dimension(Integer.MAX_VALUE, 450));
        
        container.add(chartPanel, BorderLayout.CENTER);
        graficosPanel.add(container);
        graficosPanel.add(Box.createVerticalStrut(10));
    }
    
    private void actualizarEstadisticas() {
        try {
            StringBuilder stats = new StringBuilder();
            stats.append("=== ESTADÍSTICAS CALCULADAS ===\n\n");
            
            // Obtener datos básicos
            int totalClientes = clienteController.listarTodos().size();
            int totalProductos = productoController.buscarProductosActivos().size();
            int totalVentas = ventaController.buscarTodasVentas().size();
            double ventasHoy = ventaController.calcularTotalVentasHoy();
            double ventasMes = ventaController.calcularTotalVentasMes();
            
            stats.append(String.format("Total Clientes: %d\n", totalClientes));
            stats.append(String.format("Total Productos Activos: %d\n", totalProductos));
            stats.append(String.format("Total Ventas Registradas: %d\n", totalVentas));
            stats.append(String.format("Ventas Hoy: $%.2f\n", ventasHoy));
            stats.append(String.format("Ventas Mes Actual: $%.2f\n\n", ventasMes));
            
            // Calcular estadísticas avanzadas (últimos 30 días)
            LocalDate fechaFin = LocalDate.now();
            LocalDate fechaInicio = fechaFin.minusDays(30);
            
            double promedioDiario = ventaController.calcularPromedioVentasDiarias(fechaInicio, fechaFin);
            double desviacionEstandar = ventaController.calcularDesviacionEstandarVentas(fechaInicio, fechaFin);
            double correlacionPrecioCantidad = ventaController.calcularCorrelacionPrecioCantidad();
            double correlacionDiaCantidad = ventaController.calcularCorrelacionDiaCantidad();
            
            stats.append("=== ANÁLISIS ESTADÍSTICO (Últimos 30 días) ===\n\n");
            stats.append(String.format("Promedio Ventas Diarias: $%.2f\n", promedioDiario));
            stats.append(String.format("Desviación Estándar: $%.2f\n", desviacionEstandar));
            stats.append(String.format("Correlación Precio-Cantidad: %.3f\n", correlacionPrecioCantidad));
            stats.append(String.format("Correlación Día-Cantidad: %.3f\n\n", correlacionDiaCantidad));
            
            // Interpretación de correlaciones
            stats.append("=== INTERPRETACIÓN CORRELACIONES ===\n");
            stats.append(interpretarCorrelacion(correlacionPrecioCantidad, "Precio", "Cantidad Vendida"));
            stats.append(interpretarCorrelacion(correlacionDiaCantidad, "Día de Semana", "Cantidad Vendida"));
            
            statsTextArea.setText(stats.toString());
            
        } catch (ServiceException e) {
            statsTextArea.setText("Error al calcular estadísticas: " + e.getMessage());
        } catch (Exception e) {
            statsTextArea.setText("Error inesperado: " + e.getMessage());
        }
    }
    
    private String interpretarCorrelacion(double correlacion, String var1, String var2) {
        String interpretacion = String.format("%s vs %s: ", var1, var2);
        
        if (correlacion > 0.7) {
            interpretacion += "Correlación POSITIVA FUERTE\n";
        } else if (correlacion > 0.3) {
            interpretacion += "Correlación POSITIVA MODERADA\n";
        } else if (correlacion > 0.1) {
            interpretacion += "Correlación POSITIVA DÉBIL\n";
        } else if (correlacion > -0.1) {
            interpretacion += "Prácticamente NO HAY correlación\n";
        } else if (correlacion > -0.3) {
            interpretacion += "Correlación NEGATIVA DÉBIL\n";
        } else if (correlacion > -0.7) {
            interpretacion += "Correlación NEGATIVA MODERADA\n";
        } else {
            interpretacion += "Correlación NEGATIVA FUERTE\n";
        }
        
        return interpretacion;
    }
    
    public void actualizarGraficos() {
        System.out.println("Actualizando gráficos con datos reales...");
        
        try {
            // Limpiar gráficos anteriores
            graficosPanel.removeAll();
            crearPanelEstadisticas();
            
            // Obtener datos reales de la base de datos
            List<Object[]> ventasPorCategoria = ventaController.obtenerVentasPorCategoria();
            List<Object[]> ventasPorMetodoPago = ventaController.obtenerVentasPorMetodoPago();
            List<Object[]> productosMasVendidos = ventaController.obtenerProductosMasVendidos(5);
            List<Object[]> ventasPorDia = obtenerVentasPorDiaDeSemana(); // DATOS REALES
            
            boolean hayDatos = false;
            
            // Gráfico de ventas por categoría
            if (!ventasPorCategoria.isEmpty()) {
                hayDatos = true;
                String[] categorias = new String[ventasPorCategoria.size()];
                double[] ventasCat = new double[ventasPorCategoria.size()];
                
                for (int i = 0; i < ventasPorCategoria.size(); i++) {
                    Object[] fila = ventasPorCategoria.get(i);
                    categorias[i] = truncarTexto(fila[0].toString(), 15); 
                    ventasCat[i] = ((Number) fila[1]).doubleValue(); 
                }
                crearGraficoBarras("Ventas de Productos por Categoría - En barras horizontales ", categorias, ventasCat);
            }
            
            // Gráfico de métodos de pago 
            if (!ventasPorMetodoPago.isEmpty()) {
                hayDatos = true;
                String[] metodos = new String[ventasPorMetodoPago.size()];
                double[] usos = new double[ventasPorMetodoPago.size()];
                
                for (int i = 0; i < ventasPorMetodoPago.size(); i++) {
                    Object[] fila = ventasPorMetodoPago.get(i);
                    metodos[i] = truncarTexto(fila[0].toString(), 15); 
                    usos[i] = ((Number) fila[2]).doubleValue();
                }
                crearGraficoTorta("Métodos de Pago Utilizados - En grafico de torta ", metodos, usos);
            }
            
            // Gráfico de productos más vendidos 
            if (!productosMasVendidos.isEmpty()) {
                hayDatos = true;
                String[] productos = new String[productosMasVendidos.size()];
                double[] cantidades = new double[productosMasVendidos.size()];
                
                for (int i = 0; i < productosMasVendidos.size(); i++) {
                    Object[] fila = productosMasVendidos.get(i);
                    productos[i] = truncarTexto(fila[0].toString(), 15);
                    cantidades[i] = ((Number) fila[1]).doubleValue(); 
                }
                crearGraficoBarras("Productos Más Vendidos - En barras horizontales ", productos, cantidades);
            }
            
            // Gráfico de ventas por día de la semana 
            if (!ventasPorDia.isEmpty()) {
                hayDatos = true;
                String[] dias = {"Lun", "Mar", "Mié", "Jue", "Vie", "Sáb", "Dom"};
                double[] ventasDias = new double[7];
                
                for (Object[] fila : ventasPorDia) {
                    int diaSemana = ((Number) fila[0]).intValue();
                    double total = ((Number) fila[1]).doubleValue();
                    if (diaSemana >= 1 && diaSemana <= 7) {
                        ventasDias[diaSemana - 1] = total;
                    }
                }
                crearGraficoLineas("Ventas en Pesos por Día de Semana - En grafico de lineas ", dias, ventasDias);
            }
            
            // Actualizar estadísticas
            actualizarEstadisticas();
            
            // Si no hay datos, mostrar mensaje
            if (!hayDatos) {
                mostrarMensajeInicial();
            }
            
            // Refrescar panel
            graficosPanel.revalidate();
            graficosPanel.repaint();
            
        } catch (ServiceException e) {
            JOptionPane.showMessageDialog(this, 
                "Error al cargar datos para gráficos: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Error inesperado: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Obtiene ventas agrupadas por día de la semana 
     */
    private List<Object[]> obtenerVentasPorDiaDeSemana() throws ServiceException {
        // Usar el período de los últimos 30 días
        LocalDate fechaFin = LocalDate.now();
        LocalDate fechaInicio = fechaFin.minusDays(30);
        
        List<Object[]> ventasPorDia = ventaController.obtenerTotalVentasPorDia(fechaInicio, fechaFin);
        Map<Integer, Double> ventasPorDiaSemana = new HashMap<>();
        
        // Inicializar todos los días de la semana
        for (int i = 1; i <= 7; i++) {
            ventasPorDiaSemana.put(i, 0.0);
        }
        
        // Agrupar por día de la semana
        for (Object[] fila : ventasPorDia) {
            java.sql.Date sqlDate = (java.sql.Date) fila[0];
            LocalDate fecha = sqlDate.toLocalDate();
            int diaSemana = fecha.getDayOfWeek().getValue();
            double totalDia = ((Number) fila[1]).doubleValue();
            
            ventasPorDiaSemana.put(diaSemana, ventasPorDiaSemana.get(diaSemana) + totalDia);
        }
        
        // Convertir a lista
        List<Object[]> resultado = new ArrayList<>();
        for (Map.Entry<Integer, Double> entry : ventasPorDiaSemana.entrySet()) {
            resultado.add(new Object[]{entry.getKey(), entry.getValue()});
        }
        
        return resultado;
    }
    
    /**
     * Trunca texto si es muy largo para los gráficos
     */
    private String truncarTexto(String texto, int maxLength) {
        if (texto.length() <= maxLength) {
            return texto;
        }
        return texto.substring(0, maxLength - 3) + "...";
    }
}