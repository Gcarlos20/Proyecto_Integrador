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
    // Para: Guardar un proveedor en la base de datos.
    public boolean agregar(String nombre, String telefono, String correo, String direccion) {
        String sql = "INSERT INTO PROVEEDORES (NOMBRE, TELEFONO, CORREO, DIRECCION) "
                + "VALUES (?, ?, ?, ?)";

        try (Connection conn = conexionBD.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nombre);
            ps.setString(2, telefono);
            ps.setString(3, correo);
            ps.setString(4, direccion);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al agregar proveedor: " + e.getMessage());
            return false;
        }
    }

    // BLOQUE: Listar proveedores
    // Para: Traer proveedores existentes.
    public List<proveedor> obtenerTodos() {
        List<proveedor> lista = new ArrayList<>();
        String sql = "SELECT ID_PROVEEDOR, NOMBRE, TELEFONO, CORREO, DIRECCION "
                + "FROM PROVEEDORES ORDER BY nombre";

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
        String sql = "SELECT ID_PROVEEDOR, NOMBRE, TELEFONO, CORREO, DIRECCION "
                + "FROM PROVEEDORES WHERE ID_PROVEEDOR = ?";

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
    public boolean actualizar(int id, String nombre, String telefono, String correo, String direccion) {
        String sql = "UPDATE PROVEEDORES SET NOMBRE = ?, TELEFONO = ?, CORREO = ?, DIRECCION = ? "
                + "WHERE ID_PROVEEDOR = ?";

        try (Connection conn = conexionBD.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nombre);
            ps.setString(2, telefono);
            ps.setString(3, correo);
            ps.setString(4, direccion);
            ps.setInt(5, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al actualizar proveedor: " + e.getMessage());
            return false;
        }
    }

    // BLOQUE: Eliminar proveedor
    // Para: Eliminar un proveedor de la base de datos.
    public boolean eliminar(int id) {
        String sql = "DELETE FROM PROVEEDORES WHERE ID_PROVEEDOR = ?";

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
    // Para: Filtrar proveedores por nombre.
    public List<proveedor> buscarPorNombre(String nombre) {
        List<proveedor> lista = new ArrayList<>();
        String sql = "SELECT ID_PROVEEDOR, NOMBRE, TELEFONO, CORREO, DIRECCION "
                + "FROM PROVEEDORES WHERE LOWER(NOMBRE) LIKE LOWER(?) ORDER BY NOMBRE";

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
            rs.getString("telefono"),
            rs.getString("correo"),
            rs.getString("direccion")
        );
    }
}
