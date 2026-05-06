package controller;

import model.producto;
import java.util.ArrayList;
import java.util.List;

/**
 * CONTROLADOR: Producto Controller
 * Descripción: Gestiona la lógica de negocio para productos
 * Funcionalidades: CRUD de productos, validaciones, estadísticas
 * Nota: Conectar a base de datos en los métodos comentados
 */
public class productoController {

    // BLOQUE DE BASE DE DATOS (COMENTADO - DESCOMENTAR CUANDO SE CONECTE BD)
    // private DatabaseConnection db;
    
    private List<producto> productos = new ArrayList<>();
    private int contadorId = 4;

    public productoController() {
        // BLOQUE DE BASE DE DATOS (COMENTADO - DESCOMENTAR CUANDO SE CONECTE BD)
        // this.db = new DatabaseConnection();
        
        // Datos de prueba (ELIMINAR CUANDO SE CONECTE BD)
        productos.add(new producto(1, "Laptop", 999.99, 5));
        productos.add(new producto(2, "Mouse", 25.99, 50));
        productos.add(new producto(3, "Teclado", 79.99, 20));
    }

    /**
     * BLOQUE: Agregar Producto
     * Para: Insertar nuevo producto en la base de datos
     * @param nombre Nombre del producto
     * @param precio Precio del producto
     * @param stock Stock inicial
     */
    public boolean agregar(String nombre, double precio, int stock) {
        try {
            // BLOQUE DE BASE DE DATOS (COMENTADO - DESCOMENTAR CUANDO SE CONECTE BD)
            // String query = "INSERT INTO productos (nombre, precio, stock) VALUES (?, ?, ?)";
            // db.ejecutar(query, nombre, precio, stock);
            // return true;

            productos.add(new producto(contadorId++, nombre, precio, stock));
            return true;
        } catch (Exception e) {
            System.out.println("Error al agregar producto: " + e.getMessage());
            return false;
        }
    }

    /**
     * BLOQUE: Obtener Todos los Productos
     * Para: Recuperar lista de productos desde la base de datos
     * @return Lista de productos
     */
    public List<producto> listar() {
        try {
            // BLOQUE DE BASE DE DATOS (COMENTADO - DESCOMENTAR CUANDO SE CONECTE BD)
            // String query = "SELECT * FROM productos";
            // return db.obtenerProductos(query);

            return productos;
        } catch (Exception e) {
            System.out.println("Error al obtener productos: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * BLOQUE: Obtener Producto por ID
     * Para: Buscar un producto específico
     * @param id ID del producto
     * @return Producto encontrado o null
     */
    public producto obtenerPorId(int id) {
        try {
            // BLOQUE DE BASE DE DATOS (COMENTADO - DESCOMENTAR CUANDO SE CONECTE BD)
            // String query = "SELECT * FROM productos WHERE id = ?";
            // return db.obtenerProducto(query, id);

            for (producto p : productos) {
                if (p.getId() == id) return p;
            }
            return null;
        } catch (Exception e) {
            System.out.println("Error al obtener producto: " + e.getMessage());
            return null;
        }
    }

    /**
     * BLOQUE: Actualizar Producto
     * Para: Modificar datos de un producto existente
     * @param id ID del producto
     * @param nombre Nuevo nombre
     * @param precio Nuevo precio
     * @param stock Nuevo stock
     */
    public boolean actualizar(int id, String nombre, double precio, int stock) {
        try {
            // BLOQUE DE BASE DE DATOS (COMENTADO - DESCOMENTAR CUANDO SE CONECTE BD)
            // String query = "UPDATE productos SET nombre=?, precio=?, stock=? WHERE id=?";
            // db.ejecutar(query, nombre, precio, stock, id);
            // return true;

            for (producto p : productos) {
                if (p.getId() == id) {
                    p.setNombre(nombre);
                    p.setPrecio(precio);
                    p.setStock(stock);
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            System.out.println("Error al actualizar producto: " + e.getMessage());
            return false;
        }
    }

    /**
     * BLOQUE: Eliminar Producto
     * Para: Borrar un producto de la base de datos
     * @param id ID del producto
     */
    public boolean eliminar(int id) {
        try {
            // BLOQUE DE BASE DE DATOS (COMENTADO - DESCOMENTAR CUANDO SE CONECTE BD)
            // String query = "DELETE FROM productos WHERE id=?";
            // db.ejecutar(query, id);
            // return true;

            return productos.removeIf(p -> p.getId() == id);
        } catch (Exception e) {
            System.out.println("Error al eliminar producto: " + e.getMessage());
            return false;
        }
    }

    /**
     * BLOQUE: Validar Stock Bajo
     * Para: Identificar productos con stock bajo
     * @param minimo Cantidad mínima de stock
     * @return Lista de productos con stock bajo
     */
    public List<producto> obtenerProductosConStockBajo(int minimo) {
        List<producto> stockBajo = new ArrayList<>();
        for (producto p : listar()) {
            if (p.getStock() <= minimo) {
                stockBajo.add(p);
            }
        }
        return stockBajo;
    }

    /**
     * BLOQUE: Obtener Cantidad Total
     * Para: Sumar total de unidades en inventario
     * @return Total de unidades
     */
    public int obtenerCantidadTotal() {
        int total = 0;
        for (producto p : listar()) {
            total += p.getStock();
        }
        return total;
    }

    /**
     * BLOQUE: Obtener Valor Total
     * Para: Calcular valor total del inventario
     * @return Valor total en dinero
     */
    public double obtenerValorTotal() {
        double total = 0;
        for (producto p : listar()) {
            total += p.getPrecio() * p.getStock();
        }
        return total;
    }
}
