package com.idra.gestionpeluqueria.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Clase de configuracion para la gestion de conexiones a la base de datos.
 * Implementa el patron Singleton para asegurar una unica instancia de conexion.
 * Gestiona la conexion a MySQL para el sistema gestion de e-commerce.
 * 
 * @author Idra
 */
public class DatabaseConfig {
    private static final String URL = "jdbc:mysql://localhost:3306/ecommerce_stats";
    private static final String USER = "root";
    private static final String PASSWORD = "4899380aaron";
    
    private static DatabaseConfig instance;
    private Connection connection;
    
    // Constructor privado para Singleton
    private DatabaseConfig() {
        try {
            // Cargar el driver de MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // Propiedades adicionales para la conexión
            Properties properties = new Properties();
            properties.setProperty("user", USER);
            properties.setProperty("password", PASSWORD);
            properties.setProperty("useSSL", "false");
            properties.setProperty("serverTimezone", "UTC");
            properties.setProperty("allowPublicKeyRetrieval", "true");
            
            // Crear la conexión
            this.connection = DriverManager.getConnection(URL, properties);
            System.out.println("Conexión a la base de datos establecida correctamente");
            
        } catch (ClassNotFoundException e) {
            System.err.println("Error: No se encontró el driver de MySQL");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Error al conectar con la base de datos: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Obtiene la instancia unica de DatabaseConfig (patron Singleton).
     * Si no existe una instancia, la crea de forma sincronizada.
     * 
     * @return Instancia de DatabaseConfig
     */
    public static DatabaseConfig getInstance() {
        if (instance == null) {
            synchronized (DatabaseConfig.class) {
                if (instance == null) {
                    instance = new DatabaseConfig();
                }
            }
        }
        return instance;
    }
    
    /**
     * Obtiene la conexion activa a la base de datos.
     * Si la conexion esta cerrada o es nula, intenta reconectar automaticamente.
     * 
     * @return Conexión a la base de datos
     */
    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                // Reconectar si la conexión está cerrada
                Properties properties = new Properties();
                properties.setProperty("user", USER);
                properties.setProperty("password", PASSWORD);
                properties.setProperty("useSSL", "false");
                properties.setProperty("serverTimezone", "UTC");
                properties.setProperty("allowPublicKeyRetrieval", "true");
                
                connection = DriverManager.getConnection(URL, properties);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener conexión: " + e.getMessage());
            e.printStackTrace();
        }
        return connection;
    }
    
    /**
     * Cierra la conexion activa a la base de datos de forma segura.
     * Verifica que la conexion existe y este abierta antes de cerrarla.
     */
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Conexión a la base de datos cerrada");
            }
        } catch (SQLException e) {
            System.err.println("Error al cerrar la conexión: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Realiza una prueba de conexion a la base de datos.
     * Verifica que la conexion pueda establecerse y este operativa.
     * 
     * @return true si la conexión es exitosa, false en caso contrario
     */
    public static boolean testConnection() {
        try (Connection conn = getInstance().getConnection()) {
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            System.err.println("Error en test de conexión: " + e.getMessage());
            return false;
        }
    }
}