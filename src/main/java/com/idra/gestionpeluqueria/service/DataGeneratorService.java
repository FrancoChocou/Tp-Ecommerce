package com.idra.gestionpeluqueria.service;

import com.idra.gestionpeluqueria.dao.ClienteDAO;
import com.idra.gestionpeluqueria.dao.ProductoDAO;
import com.idra.gestionpeluqueria.dao.VentaDAO;
import com.idra.gestionpeluqueria.dao.impl.ClienteDAOImpl;
import com.idra.gestionpeluqueria.dao.impl.ProductoDAOImpl;
import com.idra.gestionpeluqueria.dao.impl.VentaDAOImpl;
import com.idra.gestionpeluqueria.model.Cliente;
import com.idra.gestionpeluqueria.model.Producto;
import com.idra.gestionpeluqueria.model.Venta;
import com.idra.gestionpeluqueria.exception.DAOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Servicio para generar datos de prueba realistas para análisis estadísticos
 * Genera clientes, productos y ventas con distribución balanceada
 */
public class DataGeneratorService {
    
    private ClienteDAO clienteDAO;
    private ProductoDAO productoDAO;
    private VentaDAO ventaDAO;
    private Random random;
    
    // Arrays de datos realistas para Argentina
    private final String[] NOMBRES = {"Juan", "María", "Carlos", "Ana", "Luis", "Laura", "Diego", "Sofía", "José", "Marta", 
                                     "Miguel", "Elena", "Jorge", "Claudia", "Ricardo", "Patricia", "Fernando", "Lucía", 
                                     "Roberto", "Andrea", "Daniel", "Silvia", "Pablo", "Carolina", "Héctor", "Gabriela"};
    
    private final String[] APELLIDOS = {"Gómez", "Rodríguez", "Fernández", "López", "Martínez", "Pérez", "García", "Sánchez", 
                                       "Romero", "Álvarez", "Torres", "Díaz", "Vargas", "Castro", "Ortiz", "Silva", "Núñez", 
                                       "Juárez", "Morales", "Ortega", "Delgado", "Ramírez", "Acosta", "Flores", "Mendoza"};
    
    private final String[] ZONAS = {"Centro", "Norte", "Sur", "Este", "Oeste", "Palermo", "Recoleta", "Belgrano", "Caballito", "Flores"};
    
    private final String[] CATEGORIAS_PRODUCTOS = {"Electrodomésticos", "Tecnología", "Hogar", "Deportes", "Moda"};
    
    private final String[][] PRODUCTOS_POR_CATEGORIA = {
        {"Heladera", "Lavarropas", "Microondas", "Cocina", "Horno", "Aire Acondicionado", "Ventilador", "Aspiradora"},
        {"Smartphone", "Tablet", "Laptop", "Smart TV", "Auriculares", "Parlante Bluetooth", "Cámara Digital"},
        {"Sillón", "Mesa", "Silla", "Cama", "Mueble TV", "Escritorio", "Ropero"},
        {"Pelota", "Raqueta", "Bicicleta", "Zapatillas", "Mochila", "Botella", "Colchoneta"},
        {"Remera", "Pantalón", "Vestido", "Camisa", "Zapatos", "Cartera", "Accesorios"}
    };
    
    private final double[][] PRECIOS_POR_CATEGORIA = {
        {150000, 80000, 30000, 120000, 70000, 90000, 15000, 25000},  // Electrodomésticos
        {80000, 40000, 150000, 120000, 15000, 8000, 30000},          // Tecnología
        {50000, 30000, 15000, 40000, 35000, 25000, 45000},           // Hogar
        {5000, 8000, 25000, 12000, 6000, 3000, 4000},                // Deportes
        {8000, 12000, 15000, 10000, 18000, 9000, 5000}               // Moda
    };

    public DataGeneratorService() {
        this.clienteDAO = new ClienteDAOImpl();
        this.productoDAO = new ProductoDAOImpl();
        this.ventaDAO = new VentaDAOImpl();
        this.random = new Random();
    }
    
    /**
     * Genera datos de prueba balanceados para análisis estadístico
     */
    public void generarDatosPrueba() {
        try {
            System.out.println("Iniciando generacion de datos de prueba...");
            
            System.out.println("Generando clientes...");
            List<Cliente> clientes = generarClientes(20);
            
            System.out.println("Generando productos...");
            List<Producto> productos = generarProductos(25);
            
            System.out.println("Generando ventas...");
            generarVentasConPatrones(clientes, productos, 80);
            
            System.out.println("Datos de prueba generados exitosamente!");
            System.out.println("   - " + clientes.size() + " clientes creados");
            System.out.println("   - " + productos.size() + " productos creados"); 
            System.out.println("   - 80 ventas generadas");
            
        } catch (DAOException e) {
            System.err.println("Error al generar datos de prueba: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Genera clientes con datos realistas
     */
    private List<Cliente> generarClientes(int cantidad) throws DAOException {
        List<Cliente> clientes = new ArrayList<>();
        
        for (int i = 0; i < cantidad; i++) {
            Cliente cliente = new Cliente();
            cliente.setNombre(NOMBRES[random.nextInt(NOMBRES.length)]);
            cliente.setApellido(APELLIDOS[random.nextInt(APELLIDOS.length)]);
            cliente.setTelefono(generarTelefono());
            cliente.setEmail(generarEmail(cliente.getNombre(), cliente.getApellido()));
            cliente.setEdad(18 + random.nextInt(47)); // 18-65 años
            cliente.setIdZona(random.nextInt(ZONAS.length) + 1);
            cliente.setFechaRegistro(LocalDate.now().minusDays(random.nextInt(365))); // Registrados en el último año
            cliente.setActivo(true);
            
            clienteDAO.crear(cliente);
            clientes.add(cliente);
        }
        
        return clientes;
    }
    
    /**
     * Genera productos balanceados por categorías
     */
    private List<Producto> generarProductos(int cantidad) throws DAOException {
        List<Producto> productos = new ArrayList<>();
        int productosPorCategoria = cantidad / CATEGORIAS_PRODUCTOS.length;
        
        for (int catIndex = 0; catIndex < CATEGORIAS_PRODUCTOS.length; catIndex++) {
            for (int i = 0; i < productosPorCategoria; i++) {
                Producto producto = new Producto();
                producto.setNombre(PRODUCTOS_POR_CATEGORIA[catIndex][i % PRODUCTOS_POR_CATEGORIA[catIndex].length]);
                producto.setDescripcion("Descripción de " + producto.getNombre());
                producto.setIdCategoria(catIndex + 1);
                producto.setPrecioUnitario(PRECIOS_POR_CATEGORIA[catIndex][i % PRECIOS_POR_CATEGORIA[catIndex].length]);
                producto.setStock(50 + random.nextInt(100)); // Stock entre 50-150
                producto.setActivo(true);
                producto.setFechaCreacion(LocalDate.now().minusDays(random.nextInt(180))); // Creados en los últimos 6 meses
                
                productoDAO.crear(producto);
                productos.add(producto);
            }
        }
        
        return productos;
    }
    
    /**
     * Genera ventas con patrones que creen correlaciones interesantes
     */
    private void generarVentasConPatrones(List<Cliente> clientes, List<Producto> productos, int cantidad) throws DAOException {
        LocalDate fechaInicio = LocalDate.now().minusDays(15); // Últimos 15 días para tener más ventas hoy
        
        // Crear algunos patrones de correlación
        Map<Integer, Double> tendenciaPrecioCantidad = new HashMap<>();
        Map<Integer, Double> tendenciaDiaSemana = new HashMap<>();
        
        // Patrón 1: Productos más caros → menos cantidad vendida (correlación negativa)
        for (Producto p : productos) {
            // A mayor precio, menor probabilidad de venta en cantidad
            tendenciaPrecioCantidad.put(p.getId(), 1.0 - (p.getPrecioUnitario() / 200000.0));
        }
        
        // Patrón 2: Más ventas los viernes y sábados (correlación positiva con fin de semana)
        tendenciaDiaSemana.put(1, 0.7); // Lunes - menos ventas
        tendenciaDiaSemana.put(2, 0.8); // Martes
        tendenciaDiaSemana.put(3, 0.9); // Miércoles  
        tendenciaDiaSemana.put(4, 1.0); // Jueves
        tendenciaDiaSemana.put(5, 1.5); // Viernes - más ventas
        tendenciaDiaSemana.put(6, 1.8); // Sábado - muchas más ventas
        tendenciaDiaSemana.put(7, 1.2); // Domingo

        for (int i = 0; i < cantidad; i++) {
            Venta venta = new Venta();
            
            // Fecha con patrón de días de semana - MEJORADO para más ventas hoy
            LocalDateTime fecha = generarFechaConPatron(fechaInicio, tendenciaDiaSemana);
            venta.setFecha(fecha);
            
            // Cliente aleatorio
            venta.setCliente(clientes.get(random.nextInt(clientes.size())));
            
            // Producto con patrón precio-cantidad
            Producto producto = productos.get(random.nextInt(productos.size()));
            venta.setProducto(producto);
            
            // Cantidad basada en patrón precio-cantidad
            double factorPrecio = tendenciaPrecioCantidad.getOrDefault(producto.getId(), 1.0);
            int cantidadBase = random.nextInt(5) + 1; // 1-5 unidades base
            int cantidadVenta = (int) Math.max(1, cantidadBase * factorPrecio * (0.5 + random.nextDouble()));
            venta.setCantidad(cantidadVenta);
            
            // Precio con pequeña variación
            double variacionPrecio = 0.9 + (random.nextDouble() * 0.2); // ±10% variación
            venta.setPrecioUnitario(producto.getPrecioUnitario() * variacionPrecio);
            
            // Método de pago aleatorio (1-4)
            venta.setIdMetodoPago(1 + random.nextInt(4));
            
            // Total calculado
            venta.setTotal(venta.getPrecioUnitario() * venta.getCantidad());
            
            ventaDAO.crear(venta);
        }
    }
    
    /**
     * Genera fechas con patrones específicos (más ventas fines de semana y hoy) - MEJORADO
     */
    private LocalDateTime generarFechaConPatron(LocalDate fechaInicio, Map<Integer, Double> tendenciaDiaSemana) {
        int diasDesdeInicio;
        LocalDate fecha;
        
        double probabilidad = random.nextDouble();
        
        if (probabilidad < 0.4) {
            // 40% de ventas sean de hoy
            diasDesdeInicio = (int) (fechaInicio.until(LocalDate.now()).getDays());
        } else if (probabilidad < 0.8) {
            // 40% seguir patrón de días de semana
            List<Integer> dias = new ArrayList<>(tendenciaDiaSemana.keySet());
            List<Double> probabilidades = new ArrayList<>(tendenciaDiaSemana.values());
            int diaSeleccionado = seleccionarPorProbabilidad(dias, probabilidades);
            diasDesdeInicio = encontrarDiaEnPeriodo(fechaInicio, diaSeleccionado);
        } else {
            // 20% completamente aleatorio
            int totalDias = (int) (fechaInicio.until(LocalDate.now()).getDays()) + 1;
            diasDesdeInicio = random.nextInt(totalDias);
        }
        
        fecha = fechaInicio.plusDays(diasDesdeInicio);
        
        // Hora del día (más ventas en la tarde)
        int hora = 9 + random.nextInt(10); // 9-18hs
        if (random.nextDouble() > 0.7) hora = 18 + random.nextInt(4); // Algunas ventas en la noche
        
        return fecha.atTime(hora, random.nextInt(60));
    }
    
    /**
     * Selecciona un elemento basado en probabilidades
     */
    private int seleccionarPorProbabilidad(List<Integer> elementos, List<Double> probabilidades) {
        double total = 0.0;
        for (Double prob : probabilidades) {
            total += prob;
        }
        
        double randomValue = random.nextDouble() * total;
        double acumulado = 0.0;
        
        for (int i = 0; i < elementos.size(); i++) {
            acumulado += probabilidades.get(i);
            if (randomValue <= acumulado) {
                return elementos.get(i);
            }
        }
        
        return elementos.get(elementos.size() - 1);
    }
    
    /**
     * Encuentra un día específico dentro del período de 30 días
     */
    private int encontrarDiaEnPeriodo(LocalDate fechaInicio, int diaSemanaDeseado) {
        // Buscar el primer día de la semana deseado dentro del período
        for (int i = 0; i < 31; i++) {
            LocalDate fecha = fechaInicio.plusDays(i);
            if (fecha.getDayOfWeek().getValue() == diaSemanaDeseado) {
                return i;
            }
        }
        return random.nextInt(31); // Fallback
    }
    
    /**
     * Elimina TODOS los datos de prueba generados (ventas, productos, clientes)
     * Método SIMPLIFICADO - elimina todo sin verificar
     */
    public void limpiarDatosPrueba() {
        try {
            System.out.println("Iniciando limpieza COMPLETA de datos...");
            
            // 1. Primero eliminar TODAS las ventas
            limpiarTodasLasVentas();
            
            // 2. Luego eliminar TODOS los productos
            limpiarTodosLosProductos();
            
            // 3. Finalmente eliminar TODOS los clientes
            limpiarTodosLosClientes();
            
            System.out.println("Limpieza COMPLETA de datos exitosa!");
            System.out.println("   - Todas las ventas eliminadas");
            System.out.println("   - Todos los productos eliminados");
            System.out.println("   - Todos los clientes eliminados");
            
        } catch (DAOException e) {
            System.err.println("Error al limpiar datos de prueba: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Elimina TODAS las ventas sin excepción
     */
    private void limpiarTodasLasVentas() throws DAOException {
        List<Venta> todasLasVentas = ventaDAO.buscarTodos();
        for (Venta venta : todasLasVentas) {
            ventaDAO.eliminar(venta.getId());
        }
    }
    
    /**
     * Elimina TODOS los productos sin excepción
     */
    private void limpiarTodosLosProductos() throws DAOException {
        List<Producto> todosProductos = productoDAO.buscarTodos();
        for (Producto producto : todosProductos) {
            productoDAO.eliminar(producto.getId());
        }
    }
    
    /**
     * Elimina TODOS los clientes sin excepción
     */
    private void limpiarTodosLosClientes() throws DAOException {
        List<Cliente> todosClientes = clienteDAO.buscarTodos();
        for (Cliente cliente : todosClientes) {
            clienteDAO.eliminar(cliente.getId());
        }
    }
    
    /**
     * Genera un número de teléfono argentino realista
     */
    private String generarTelefono() {
        return "11" + (3000000 + random.nextInt(7000000));
    }
    
    /**
     * Genera un email basado en nombre y apellido
     */
    private String generarEmail(String nombre, String apellido) {
        return nombre.toLowerCase() + "." + apellido.toLowerCase() + 
               (random.nextInt(100)) + "@gmail.com";
    }
}