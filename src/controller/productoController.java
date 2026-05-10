package controller;

import Dao.ProductoDao;
import model.producto;
import java.util.ArrayList;
import java.util.List;

/**
 * CONTROLADOR: Producto Controller
 * Descripción: Gestiona la lógica de negocio para productos
 * Funcionalidades: CRUD de productos, validaciones, estadísticas
 */
public class productoController {
    
    private final ProductoDao productoDao;

    public productoController() {
        this.productoDao = new ProductoDao();
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
            return productoDao.agregar(nombre, precio, stock);
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
            return productoDao.listarTodos();
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
            return productoDao.buscarPorId(id);
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
            return productoDao.actualizar(id, nombre, precio, stock);
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
            return productoDao.eliminar(id);
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
