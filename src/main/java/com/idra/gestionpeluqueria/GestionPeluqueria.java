package com.idra.gestionpeluqueria;

import com.idra.gestionpeluqueria.config.DatabaseConfig;
import com.idra.gestionpeluqueria.view.MainFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * Clase principal de la aplicación de Gestión de Peluquería
 * Sistema para administrar clientes, servicios y turnos de una peluquería
 * 
 * @author Idra
 * @version 1.0
 */
public class GestionPeluqueria {
    
    /**
     * Método principal que inicia la aplicación
     * 
     * @param args Argumentos de línea de comandos 
     */
    public static void main(String[] args) {
        // Configurar el look and feel de la aplicación
        setupLookAndFeel();
        
        // Probar la conexión a la base de datos
        testDatabaseConnection();
        
        // Iniciar la interfaz gráfica en el Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            createAndShowGUI();
        });
    }
    
    /**
     * Configura el aspecto visual de la aplicación
     */
    private static void setupLookAndFeel() {
    try {
        // En lugar del look del sistema, usar Nimbus que respeta mejor los colores
        for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
            if ("Nimbus".equals(info.getName())) {
                UIManager.setLookAndFeel(info.getClassName());
                break;
            }
        }
        
    } catch (Exception e) {
        System.err.println("Error al configurar el look and feel: " + e.getMessage());
        // Continuar con el look and feel por defecto
    }
}
    /**
     * Prueba la conexión a la base de datos al iniciar la aplicación
     */
    private static void testDatabaseConnection() {
        System.out.println("=== Iniciando Sistema de Gestión de Peluquería ===");
        System.out.println("Probando conexión a la base de datos...");
        
        boolean connectionSuccess = DatabaseConfig.testConnection();
        
        if (connectionSuccess) {
            System.out.println("✅ Conexión a la base de datos establecida correctamente");
        } else {
            System.err.println("❌ Error: No se pudo conectar a la base de datos");
            System.err.println("Por favor, verifica que:");
            System.err.println("1. MySQL esté instalado y ejecutándose");
            System.err.println("2. La base de datos 'peluqueria_db' exista");
            System.err.println("3. Las credenciales en DatabaseConfig.java sean correctas");
            
            // Preguntar si desea continuar sin base de datos
            System.out.println("\n¿Desea continuar en modo demo (sin base de datos)? (s/n)");
            // En una aplicación real, aquí podrías mostrar un diálogo
        }
        
        System.out.println("=================================================");
    }
    
    /**
     * Crea y muestra la interfaz gráfica principal
     */
    private static void createAndShowGUI() {
        try {
            // Crear y mostrar la ventana principal
            MainFrame mainFrame = new MainFrame();
            mainFrame.setVisible(true);
            
            System.out.println("Interfaz gráfica iniciada correctamente");
            
        } catch (Exception e) {
            System.err.println("Error al crear la interfaz gráfica: " + e.getMessage());
            e.printStackTrace();
            
            // Mostrar mensaje de error al usuario
            javax.swing.JOptionPane.showMessageDialog(
                null,
                "Error al iniciar la aplicación: " + e.getMessage(),
                "Error de Inicio",
                javax.swing.JOptionPane.ERROR_MESSAGE
            );
        }
    }
    
    /**
     * Método para obtener información sobre la aplicación
     * 
     * @return String con la información de la aplicación
     */
    public static String getAppInfo() {
        return """
               Sistema de Gesti\u00f3n de Peluquer\u00eda v1.0
               Desarrollado para administrar clientes, servicios y turnos
               \u00a9 2024 Peluquer\u00eda Alejandro """;
    }
    
    /**
     * Método para cerrar la aplicación de manera controlada
     */
    public static void shutdown() {
        System.out.println("Cerrando aplicación...");
        DatabaseConfig.getInstance().closeConnection();
        System.out.println("Aplicación cerrada correctamente");
        System.exit(0);
    }
}