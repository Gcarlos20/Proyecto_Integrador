package Dao;




import model.producto;
import util.Conexion;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoDao {

    // ===== LISTAR TODOS =====
    public List<producto> listarTodos() {
        List<producto> lista = new ArrayList<>();
        String sql = "SELECT ID_PRODUCTO, NOMBRE, PRECIO, CANTIDAD FROM PRODUCTOS";

        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                producto p = new producto(
                    rs.getInt("ID_PRODUCTO"),
                    rs.getString("NOMBRE"),
                    rs.getDouble("PRECIO"),
                    rs.getInt("CANTIDAD")
                );
                lista.add(p);
            }

        } catch (SQLException e) {
            System.out.println("❌ Error al listar: " + e.getMessage());
        }
        return lista;
    }

    // ===== AGREGAR =====
    public boolean agregar(String nombre, double precio, int stock) {
        String sql = "INSERT INTO PRODUCTOS (NOMBRE, PRECIO, CANTIDAD) VALUES (?, ?, ?)";

        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nombre);
            ps.setDouble(2, precio);
            ps.setInt(3, stock);
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("❌ Error al agregar: " + e.getMessage());
            return false;
        }
    }

    // ===== ACTUALIZAR =====
    public boolean actualizar(int id, String nombre, double precio, int stock) {
        String sql = "UPDATE PRODUCTOS SET NOMBRE=?, PRECIO=?, CANTIDAD=? WHERE ID_PRODUCTO=?";

        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nombre);
            ps.setDouble(2, precio);
            ps.setInt(3, stock);
            ps.setInt(4, id);
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("❌ Error al actualizar: " + e.getMessage());
            return false;
        }
    }

    // ===== ELIMINAR =====
    public boolean eliminar(int id) {
        String sql = "DELETE FROM PRODUCTOS WHERE ID_PRODUCTO=?";

        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("❌ Error al eliminar: " + e.getMessage());
            return false;
        }
    }

    // ===== BUSCAR POR ID =====
    public producto buscarPorId(int id) {
        String sql = "SELECT * FROM PRODUCTOS WHERE ID_PRODUCTO=?";

        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new producto(
                    rs.getInt("ID_PRODUCTO"),
                    rs.getString("NOMBRE"),
                    rs.getDouble("PRECIO"),
                    rs.getInt("CANTIDAD")
                );
            }

        } catch (SQLException e) {
            System.out.println("❌ Error al buscar: " + e.getMessage());
        }
        return null;
    }
}