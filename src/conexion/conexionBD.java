package conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class conexionBD {

    private static final String SERVIDOR = "localhost";
    private static final String PUERTO = "3306";
    private static final String BASE_DATOS = "inventario";
    private static final String USUARIO = "root";
    private static final String PASSWORD = "123456";

    private static final String URL = "jdbc:mysql://" + SERVIDOR + ":" + PUERTO + "/" + BASE_DATOS
            + "?useSSL=false&serverTimezone=America/Bogota&allowPublicKeyRetrieval=true";

    // BLOQUE: Abrir conexion
    // Para: Entregar una conexion MySQL lista para consultas de los DAO.
    public static Connection getConexion() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver MySQL no encontrado. Agregue mysql-connector-j al classpath.", e);
        }

        Connection conn = DriverManager.getConnection(URL, USUARIO, PASSWORD);
        System.out.println("Conexion establecida");
        return conn;
    }

    // BLOQUE: Conexion para objetos
    // Para: Permitir usar new conexionBD().establecerConexion() si una vista lo necesita.
    public Connection establecerConexion() {
        try {
            return getConexion();
        } catch (SQLException e) {
            System.out.println("Error de conexion: " + e.getMessage());
            return null;
        }
    }

    // BLOQUE: Alias de conexion
    // Para: Mantener compatibilidad con codigo que use el nombre getConnection.
    public static Connection getConnection() throws SQLException {
        return getConexion();
    }

    // BLOQUE: Probar conexion
    // Para: Validar rapidamente si la base de datos esta disponible.
    public static boolean probarConexion() {
        try (Connection conn = getConexion()) {
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            System.out.println("Error de conexion: " + e.getMessage());
            return false;
        }
    }

    // BLOQUE: Cerrar conexion
    // Para: Cerrar conexiones creadas fuera de try-with-resources.
    public static void cerrarConexion(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                System.out.println("Error al cerrar: " + e.getMessage());
            }
        }
    }
}
