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
        String sql = "SELECT id_producto, codigo, nombre, categoria, descripcion, precio, cantidad, stock_minimo, id_proveedor "
                + "FROM PRODUCTOS ORDER BY id_producto";

        try (Connection conn = conexionBD.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                producto p = new producto(
                    rs.getInt("id_producto"),
                    rs.getString("codigo"),
                    rs.getString("nombre"),
                    rs.getString("categoria"),
                    rs.getString("descripcion"),
                    rs.getDouble("precio"),
                    rs.getInt("cantidad"),
                    rs.getInt("stock_minimo"),
                    obtenerIntegerNullable(rs, "id_proveedor")
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
        return agregar(null, nombre, null, null, precio, stock, 0, null);
    }

    public boolean agregar(String codigo, String nombre, String categoria, String descripcion,
                           double precio, int stock, int stockMinimo, Integer proveedorId) {
        String sql = "INSERT INTO PRODUCTOS (codigo, nombre, categoria, descripcion, precio, cantidad, stock_minimo, id_proveedor) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = conexionBD.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, codigo);
            ps.setString(2, nombre);
            ps.setString(3, categoria);
            ps.setString(4, descripcion);
            ps.setDouble(5, precio);
            ps.setInt(6, stock);
            ps.setInt(7, stockMinimo);
            setIntegerNullable(ps, 8, proveedorId);
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
        return actualizar(id, null, nombre, null, null, precio, stock, 0, null);
    }

    public boolean actualizar(int id, String codigo, String nombre, String categoria, String descripcion,
                              double precio, int stock, int stockMinimo, Integer proveedorId) {
        String sql = "UPDATE PRODUCTOS SET codigo = ?, nombre = ?, categoria = ?, descripcion = ?, precio = ?, "
                + "cantidad = ?, stock_minimo = ?, id_proveedor = ? WHERE id_producto = ?";

        try (Connection conn = conexionBD.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, codigo);
            ps.setString(2, nombre);
            ps.setString(3, categoria);
            ps.setString(4, descripcion);
            ps.setDouble(5, precio);
            ps.setInt(6, stock);
            ps.setInt(7, stockMinimo);
            setIntegerNullable(ps, 8, proveedorId);
            ps.setInt(9, id);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al actualizar producto: " + e.getMessage());
            return false;
        }
    }

    // BLOQUE: Eliminar producto
    // Para: Borrar un producto por su identificador.
    public boolean eliminar(int id) {
        String sql = "DELETE FROM PRODUCTOS WHERE id_producto = ?";

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
        String sql = "SELECT id_producto, codigo, nombre, categoria, descripcion, precio, cantidad, stock_minimo, id_proveedor "
                + "FROM productos WHERE id_producto = ?";

        try (Connection conn = conexionBD.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new producto(
                        rs.getInt("id_producto"),
                        rs.getString("codigo"),
                        rs.getString("nombre"),
                        rs.getString("categoria"),
                        rs.getString("descripcion"),
                        rs.getDouble("precio"),
                        rs.getInt("cantidad"),
                        rs.getInt("stock_minimo"),
                        obtenerIntegerNullable(rs, "id_proveedor")
                    );
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al buscar producto: " + e.getMessage());
        }
        return null;
    }

    private Integer obtenerIntegerNullable(ResultSet rs, String columna) throws SQLException {
        int valor = rs.getInt(columna);
        return rs.wasNull() ? null : valor;
    }

    private void setIntegerNullable(PreparedStatement ps, int parametro, Integer valor) throws SQLException {
        if (valor == null) {
            ps.setNull(parametro, Types.INTEGER);
        } else {
            ps.setInt(parametro, valor);
        }
    }
}
