package controller;

import Dao.ProveedorDao;
import model.proveedor;
import java.util.List;

/**
 * CONTROLADOR: Proveedor Controller
 * Descripción: Gestiona la información de proveedores
 * Funcionalidades: CRUD de proveedores, búsqueda, filtrado
 */
public class ProveedorController {

    private final ProveedorDao proveedorDao;

    public ProveedorController() {
        this.proveedorDao = new ProveedorDao();
    }

    /**
     * BLOQUE: Agregar Proveedor
     * Para: Insertar nuevo proveedor
     * @param nombre Nombre del proveedor
     * @param telefono Teléfono
     * @param correo Correo
     * @param direccion Dirección
     */
    public boolean agregar(String nombre, String telefono, String correo, String direccion) {
        try {
            return proveedorDao.agregar(nombre, telefono, correo, direccion);
        } catch (Exception e) {
            System.out.println("Error al agregar proveedor: " + e.getMessage());
            return false;
        }
    }

    /**
     * BLOQUE: Obtener Todos los Proveedores
     * Para: Recuperar lista de proveedores
     * @return Lista de proveedores
     */
    public List<proveedor> obtenerTodos() {
        try {
            return proveedorDao.obtenerTodos();
        } catch (Exception e) {
            System.out.println("Error al obtener proveedores: " + e.getMessage());
            return List.of();
        }
    }

    /**
     * BLOQUE: Obtener Proveedor por ID
     * Para: Buscar un proveedor específico
     * @param id ID del proveedor
     * @return Proveedor encontrado o null
     */
    public proveedor obtenerPorId(int id) {
        return proveedorDao.obtenerPorId(id);
    }

    /**
     * BLOQUE: Actualizar Proveedor
     * Para: Modificar datos de un proveedor
     * @param id ID del proveedor
     * @param nombre Nuevo nombre
     * @param telefono Nuevo teléfono
     * @param correo Nuevo correo
     * @param direccion Nueva dirección
     */
    public boolean actualizar(int id, String nombre, String telefono, String correo, String direccion) {
        try {
            return proveedorDao.actualizar(id, nombre, telefono, correo, direccion);
        } catch (Exception e) {
            System.out.println("Error al actualizar proveedor: " + e.getMessage());
            return false;
        }
    }

    /**
     * BLOQUE: Eliminar Proveedor
     * Para: Eliminar un proveedor
     * @param id ID del proveedor
     */
    public boolean eliminar(int id) {
        try {
            return proveedorDao.eliminar(id);
        } catch (Exception e) {
            System.out.println("Error al eliminar proveedor: " + e.getMessage());
            return false;
        }
    }

    /**
     * BLOQUE: Buscar Proveedor por Nombre
     * Para: Búsqueda rápida por nombre
     * @param nombre Nombre del proveedor
     * @return Lista de proveedores que coinciden
     */
    public List<proveedor> buscarPorNombre(String nombre) {
        return proveedorDao.buscarPorNombre(nombre);
    }
}
