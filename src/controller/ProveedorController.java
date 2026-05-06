package controller;

import model.proveedor;
import java.util.ArrayList;
import java.util.List;

/**
 * CONTROLADOR: Proveedor Controller
 * Descripción: Gestiona la información de proveedores
 * Funcionalidades: CRUD de proveedores, búsqueda, filtrado
 * Nota: Conectar a base de datos en los métodos comentados
 */
public class ProveedorController {

    // BLOQUE DE BASE DE DATOS (COMENTADO - DESCOMENTAR CUANDO SE CONECTE BD)
    // private DatabaseConnection db;
    
    private List<proveedor> proveedores = new ArrayList<>();
    private int contadorId = 4;

    public ProveedorController() {
        // BLOQUE DE BASE DE DATOS (COMENTADO - DESCOMENTAR CUANDO SE CONECTE BD)
        // this.db = new DatabaseConnection();
        
        // Datos de prueba
        proveedores.add(new proveedor(1, "Proveedor A", "Juan López", "555-0001", "juan@proveedora.com", "Calle 1, Ciudad", true));
        proveedores.add(new proveedor(2, "Proveedor B", "María García", "555-0002", "maria@proveedorb.com", "Calle 2, Ciudad", true));
        proveedores.add(new proveedor(3, "Proveedor C", "Carlos Ruiz", "555-0003", "carlos@proveedorc.com", "Calle 3, Ciudad", true));
    }

    /**
     * BLOQUE: Agregar Proveedor
     * Para: Insertar nuevo proveedor
     * @param nombre Nombre del proveedor
     * @param contacto Persona de contacto
     * @param telefono Teléfono
     * @param email Email
     * @param direccion Dirección
     */
    public boolean agregar(String nombre, String contacto, String telefono, String email, String direccion) {
        try {
            // BLOQUE DE BASE DE DATOS (COMENTADO - DESCOMENTAR CUANDO SE CONECTE BD)
            // String query = "INSERT INTO proveedores (nombre, contacto, telefono, email, direccion, activo) VALUES (?, ?, ?, ?, ?, ?)";
            // db.ejecutar(query, nombre, contacto, telefono, email, direccion, true);
            // return true;

            proveedores.add(new proveedor(contadorId++, nombre, contacto, telefono, email, direccion, true));
            return true;
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
            // BLOQUE DE BASE DE DATOS (COMENTADO - DESCOMENTAR CUANDO SE CONECTE BD)
            // String query = "SELECT * FROM proveedores WHERE activo = true";
            // return db.obtenerProveedores(query);

            return proveedores;
        } catch (Exception e) {
            System.out.println("Error al obtener proveedores: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * BLOQUE: Obtener Proveedor por ID
     * Para: Buscar un proveedor específico
     * @param id ID del proveedor
     * @return Proveedor encontrado o null
     */
    public proveedor obtenerPorId(int id) {
        for (proveedor p : obtenerTodos()) {
            if (p.getId() == id) return p;
        }
        return null;
    }

    /**
     * BLOQUE: Actualizar Proveedor
     * Para: Modificar datos de un proveedor
     * @param id ID del proveedor
     * @param nombre Nuevo nombre
     * @param contacto Nuevo contacto
     * @param telefono Nuevo teléfono
     * @param email Nuevo email
     * @param direccion Nueva dirección
     */
    public boolean actualizar(int id, String nombre, String contacto, String telefono, String email, String direccion) {
        try {
            for (proveedor p : proveedores) {
                if (p.getId() == id) {
                    p.setNombre(nombre);
                    p.setContacto(contacto);
                    p.setTelefono(telefono);
                    p.setEmail(email);
                    p.setDireccion(direccion);
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            System.out.println("Error al actualizar proveedor: " + e.getMessage());
            return false;
        }
    }

    /**
     * BLOQUE: Eliminar Proveedor
     * Para: Desactivar un proveedor (soft delete)
     * @param id ID del proveedor
     */
    public boolean eliminar(int id) {
        try {
            // BLOQUE DE BASE DE DATOS (COMENTADO - DESCOMENTAR CUANDO SE CONECTE BD)
            // String query = "UPDATE proveedores SET activo = false WHERE id = ?";
            // db.ejecutar(query, id);
            // return true;

            return proveedores.removeIf(p -> p.getId() == id);
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
        List<proveedor> resultado = new ArrayList<>();
        for (proveedor p : obtenerTodos()) {
            if (p.getNombre().toLowerCase().contains(nombre.toLowerCase())) {
                resultado.add(p);
            }
        }
        return resultado;
    }
}