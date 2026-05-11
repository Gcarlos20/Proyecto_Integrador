package Dao;

import conexion.conexionBD;
import model.proveedor;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProveedorDao {

    // BLOQUE: Agregar proveedor
    // Para: Guardar un proveedor activo en la base de datos.
    public boolean agregar(String nombre, String contacto, String telefono, String email, String direccion) {
        String sql = "INSERT INTO PROVEEDORES (nombre, contacto, telefono, email, direccion, activo) "
                + "VALUES (?, ?, ?, ?, ?, TRUE)";

        try (Connection conn = conexionBD.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nombre);
            ps.setString(2, contacto);
            ps.setString(3, telefono);
            ps.setString(4, email);
            ps.setString(5, direccion);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al agregar proveedor: " + e.getMessage());
            return false;
        }
    }

    // BLOQUE: Listar proveedores
    // Para: Traer solo proveedores activos.
    public List<proveedor> obtenerTodos() {
        List<proveedor> lista = new ArrayList<>();
        String sql = "SELECT ID_PROVEEDORES, NOMBRE, CONTACTO, TELEFONO, CORRREO, DIRECCION, ACTIVO "
                + "FROM PROVEEDORES WHERE activo = TRUE ORDER BY nombre";

        try (Connection conn = conexionBD.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapearProveedor(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error al listar proveedores: " + e.getMessage());
        }
        return lista;
    }

    // BLOQUE: Buscar proveedor
    // Para: Obtener un proveedor por su ID.
    public proveedor obtenerPorId(int id) {
        String sql = "SELECT id_proveedor, nombre, contacto, telefono, email, direccion, activo "
                + "FROM proveedores WHERE id_proveedor = ?";

        try (Connection conn = conexionBD.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearProveedor(rs);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar proveedor: " + e.getMessage());
        }
        return null;
    }

    // BLOQUE: Actualizar proveedor
    // Para: Modificar datos principales de un proveedor.
    public boolean actualizar(int id, String nombre, String contacto, String telefono, String email, String direccion) {
        String sql = "UPDATE PROVEEDORES SET nombre = ?, contacto = ?, telefono = ?, email = ?, direccion = ? "
                + "WHERE ID_PROVEEDOR = ?";

        try (Connection conn = conexionBD.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nombre);
            ps.setString(2, contacto);
            ps.setString(3, telefono);
            ps.setString(4, email);
            ps.setString(5, direccion);
            ps.setInt(6, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al actualizar proveedor: " + e.getMessage());
            return false;
        }
    }

    // BLOQUE: Desactivar proveedor
    // Para: Ocultar un proveedor sin borrar su historial.
    public boolean eliminar(int id) {
        String sql = "UPDATE PROVEEDORES SET activo = FALSE WHERE ID_PROVEEDOR = ?";

        try (Connection conn = conexionBD.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al eliminar proveedor: " + e.getMessage());
            return false;
        }
    }

    // BLOQUE: Buscar por nombre
    // Para: Filtrar proveedores desde la base de datos.
    public List<proveedor> buscarPorNombre(String nombre) {
        List<proveedor> lista = new ArrayList<>();
        String sql = "SELECT id_proveedor, nombre, contacto, telefono, email, direccion, activo "
                + "FROM proveedores WHERE activo = TRUE AND LOWER(nombre) LIKE LOWER(?) ORDER BY nombre";

        try (Connection conn = conexionBD.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, "%" + nombre + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearProveedor(rs));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar proveedores: " + e.getMessage());
        }
        return lista;
    }

    // BLOQUE: Mapear resultado
    // Para: Convertir una fila SQL en un objeto proveedor.
    private proveedor mapearProveedor(ResultSet rs) throws SQLException {
        return new proveedor(
            rs.getInt("id_proveedor"),
            rs.getString("nombre"),
            rs.getString("contacto"),
            rs.getString("telefono"),
            rs.getString("email"),
            rs.getString("direccion"),
            rs.getBoolean("activo")
        );
    }
}
