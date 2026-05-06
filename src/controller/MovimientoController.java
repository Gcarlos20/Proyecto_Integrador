package controller;

import model.movimiento;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * CONTROLADOR: Movimiento Controller
 * Descripción: Gestiona el registro de movimientos de inventario
 * Funcionalidades: Registrar entradas, salidas, ajustes; obtener historial
 * Nota: Conectar a base de datos en los métodos comentados
 */
public class MovimientoController {

    // BLOQUE DE BASE DE DATOS (COMENTADO - DESCOMENTAR CUANDO SE CONECTE BD)
    // private DatabaseConnection db;
    
    private List<movimiento> movimientos = new ArrayList<>();
    private int contadorId = 5;

    public MovimientoController() {
        // BLOQUE DE BASE DE DATOS (COMENTADO - DESCOMENTAR CUANDO SE CONECTE BD)
        // this.db = new DatabaseConnection();
        
        // Datos de prueba
        movimientos.add(new movimiento(1, 1, "ENTRADA", 10, LocalDateTime.now(), "admin", "Compra inicial"));
        movimientos.add(new movimiento(2, 2, "SALIDA", 5, LocalDateTime.now(), "usuario1", "Venta"));
        movimientos.add(new movimiento(3, 3, "AJUSTE", -2, LocalDateTime.now(), "admin", "Corrección"));
    }

    /**
     * BLOQUE: Registrar Movimiento
     * Para: Insertar nuevo movimiento en la base de datos
     * @param productoId ID del producto
     * @param tipo Tipo de movimiento (ENTRADA, SALIDA, AJUSTE)
     * @param cantidad Cantidad movida
     * @param usuario Usuario que realiza el movimiento
     * @param observaciones Notas adicionales
     */
    public boolean registrar(int productoId, String tipo, int cantidad, String usuario, String observaciones) {
        try {
            // BLOQUE DE BASE DE DATOS (COMENTADO - DESCOMENTAR CUANDO SE CONECTE BD)
            // String query = "INSERT INTO movimientos (producto_id, tipo, cantidad, fecha, usuario, observaciones) VALUES (?, ?, ?, ?, ?, ?)";
            // db.ejecutar(query, productoId, tipo, cantidad, LocalDateTime.now(), usuario, observaciones);
            // return true;

            movimientos.add(new movimiento(contadorId++, productoId, tipo, cantidad, LocalDateTime.now(), usuario, observaciones));
            return true;
        } catch (Exception e) {
            System.out.println("Error al registrar movimiento: " + e.getMessage());
            return false;
        }
    }

    /**
     * BLOQUE: Obtener Todos los Movimientos
     * Para: Recuperar lista de movimientos
     * @return Lista de movimientos
     */
    public List<movimiento> obtenerTodos() {
        try {
            // BLOQUE DE BASE DE DATOS (COMENTADO - DESCOMENTAR CUANDO SE CONECTE BD)
            // String query = "SELECT * FROM movimientos ORDER BY fecha DESC";
            // return db.obtenerMovimientos(query);

            return movimientos;
        } catch (Exception e) {
            System.out.println("Error al obtener movimientos: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * BLOQUE: Obtener Movimientos Recientes
     * Para: Obtener los últimos N movimientos
     * @param limite Número de movimientos a recuperar
     * @return Lista de movimientos recientes
     */
    public List<movimiento> obtenerRecientes(int limite) {
        List<movimiento> recientes = new ArrayList<>(obtenerTodos());
        if (recientes.size() > limite) {
            return recientes.subList(recientes.size() - limite, recientes.size());
        }
        return recientes;
    }

    /**
     * BLOQUE: Obtener Movimientos por Producto
     * Para: Ver historial de un producto específico
     * @param productoId ID del producto
     * @return Lista de movimientos del producto
     */
    public List<movimiento> obtenerPorProducto(int productoId) {
        List<movimiento> porProducto = new ArrayList<>();
        for (movimiento m : obtenerTodos()) {
            if (m.getProductoId() == productoId) {
                porProducto.add(m);
            }
        }
        return porProducto;
    }

    /**
     * BLOQUE: Obtener Movimientos por Tipo
     * Para: Filtrar movimientos por tipo
     * @param tipo Tipo de movimiento (ENTRADA, SALIDA, AJUSTE)
     * @return Lista de movimientos del tipo especificado
     */
    public List<movimiento> obtenerPorTipo(String tipo) {
        List<movimiento> porTipo = new ArrayList<>();
        for (movimiento m : obtenerTodos()) {
            if (m.getTipo().equals(tipo)) {
                porTipo.add(m);
            }
        }
        return porTipo;
    }

    /**
     * BLOQUE: Calcular Saldo de Producto
     * Para: Sumar todas las entradas y salidas de un producto
     * @param productoId ID del producto
     * @return Saldo neto del producto
     */
    public int calcularSaldo(int productoId) {
        int saldo = 0;
        for (movimiento m : obtenerPorProducto(productoId)) {
            if (m.getTipo().equals("ENTRADA")) {
                saldo += m.getCantidad();
            } else if (m.getTipo().equals("SALIDA")) {
                saldo -= m.getCantidad();
            } else if (m.getTipo().equals("AJUSTE")) {
                saldo += m.getCantidad();
            }
        }
        return saldo;
    }
}