package util;

/**
 * UTILIDAD: Permisos
 * Para: Centralizar las reglas de roles y evitar repetir condiciones en cada vista.
 */
public class Permisos {

    /**
     * BLOQUE: Validar rol administrador
     * Para: Saber si el usuario puede usar todas las funciones del sistema.
     */
    public static boolean esAdmin() {
        return tieneRol("admin");
    }

    /**
     * BLOQUE: Validar rol usuario
     * Para: Permitir operaciones normales como comprar productos.
     */
    public static boolean esUsuario() {
        return tieneRol("usuario");
    }

    /**
     * BLOQUE: Validar rol consultor
     * Para: Permitir solo consultas y reportes sin modificar datos.
     */
    public static boolean esConsultor() {
        return tieneRol("consultor");
    }

    /**
     * BLOQUE: Permiso para administrar productos
     * Para: Activar CRUD de productos solo a admin y usuario.
     */
    public static boolean puedeGestionarProductos() {
        return esAdmin() || esUsuario();
    }

    /**
     * BLOQUE: Permiso para comprar
     * Para: Activar el registro de compras solo a admin y usuario.
     */
    public static boolean puedeRegistrarCompras() {
        return esAdmin() || esUsuario();
    }

    /**
     * BLOQUE: Permiso para proveedores
     * Para: Dejar proveedores solo para admin porque cambia datos maestros.
     */
    public static boolean puedeGestionarProveedores() {
        return esAdmin();
    }

    /**
     * BLOQUE: Permiso para reportes
     * Para: Permitir reportes a todos los roles autenticados.
     */
    public static boolean puedeVerReportes() {
        return session.usuarioActual != null;
    }

    /**
     * BLOQUE: Comparar rol actual
     * Para: Revisar el rol guardado en la sesion sin repetir validaciones de null.
     */
    private static boolean tieneRol(String rol) {
        return session.usuarioActual != null
                && session.usuarioActual.getRol() != null
                && session.usuarioActual.getRol().equalsIgnoreCase(rol);
    }
}
