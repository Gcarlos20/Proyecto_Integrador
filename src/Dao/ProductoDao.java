package Dao;

import conexion.conexionBD;
import model.producto;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoDao {

    // BLOQUE: Listar productos
    // Para: Consultar todos los productos guardados en la base de datos.
    public List<producto> listarTodos() {
        List<producto> lista = new ArrayList<>();
        String sql = "SELECT id_producto, nombre, precio, cantidad FROM productos ORDER BY id_producto";

        try (Connection conn = conexionBD.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                producto p = new producto(
                    rs.getInt("id_producto"),
                    rs.getString("nombre"),
                    rs.getDouble("precio"),
                    rs.getInt("cantidad")
                );
                lista.add(p);
            }

        } catch (SQLException e) {
            System.out.println("Error al listar productos: " + e.getMessage());
        }
        return lista;
    }

    // BLOQUE: Agregar producto
    // Para: Insertar un producto nuevo en la tabla productos.
    public boolean agregar(String nombre, double precio, int stock) {
        String sql = "INSERT INTO productos (nombre, precio, cantidad) VALUES (?, ?, ?)";

        try (Connection conn = conexionBD.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nombre);
            ps.setDouble(2, precio);
            ps.setInt(3, stock);
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Error al agregar producto: " + e.getMessage());
            return false;
        }
    }

    // BLOQUE: Actualizar producto
    // Para: Modificar nombre, precio y cantidad de un producto existente.
    public boolean actualizar(int id, String nombre, double precio, int stock) {
        String sql = "UPDATE productos SET nombre = ?, precio = ?, cantidad = ? WHERE id_producto = ?";

        try (Connection conn = conexionBD.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nombre);
            ps.setDouble(2, precio);
            ps.setInt(3, stock);
            ps.setInt(4, id);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al actualizar producto: " + e.getMessage());
            return false;
        }
    }

    // BLOQUE: Eliminar producto
    // Para: Borrar un producto por su identificador.
    public boolean eliminar(int id) {
        String sql = "DELETE FROM productos WHERE id_producto = ?";

        try (Connection conn = conexionBD.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al eliminar producto: " + e.getMessage());
            return false;
        }
    }

    // BLOQUE: Buscar producto
    // Para: Obtener un producto concreto por ID.
    public producto buscarPorId(int id) {
        String sql = "SELECT id_producto, nombre, precio, cantidad FROM productos WHERE id_producto = ?";

        try (Connection conn = conexionBD.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new producto(
                        rs.getInt("id_producto"),
                        rs.getString("nombre"),
                        rs.getDouble("precio"),
                        rs.getInt("cantidad")
                    );
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al buscar producto: " + e.getMessage());
        }
        return null;
    }
}
