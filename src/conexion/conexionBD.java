package conexion;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class conexionBD {

    private static final Properties CONFIG = cargarConfiguracion();
    private static final String SERVIDOR = obtenerConfig("DB_SERVIDOR", "db.servidor", "localhost");
    private static final String PUERTO = obtenerConfig("DB_PUERTO", "db.puerto", "3306");
    private static final String BASE_DATOS = obtenerConfig("DB_BASE_DATOS", "db.base_datos", "inventario");
    private static final String USUARIO = obtenerConfig("DB_USUARIO", "db.usuario", "root");
    private static final String PASSWORD = obtenerConfig("DB_PASSWORD", "db.password", "");

    private static final String URL = "jdbc:mysql://" + SERVIDOR + ":" + PUERTO + "/" + BASE_DATOS //para evitar errores de timezone y SSL en MySQL Connector/J 8.x
            + "?useSSL=false&serverTimezone=America/Bogota&allowPublicKeyRetrieval=true"; // URL de conexion con parametros recomendados

    private static Properties cargarConfiguracion() {
        Properties propiedades = new Properties();
        try (FileInputStream archivo = new FileInputStream("config/database.properties")) {
            propiedades.load(archivo);
        } catch (IOException e) {
            System.out.println("No se encontro config/database.properties; se usaran variables de entorno y valores por defecto.");
        }
        return propiedades;
    }

    private static String obtenerConfig(String variableEntorno, String propiedad, String valorPorDefecto) {
        String valor = System.getenv(variableEntorno);
        if (valor != null && !valor.isBlank()) {
            return valor;
        }
        return CONFIG.getProperty(propiedad, valorPorDefecto);
    }

    // BLOQUE: Abrir conexion
    // Para: Entregar una conexion MySQL lista para consultas de los DAO.
    public static Connection getConexion() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // Cargar el driver MySQL Connector/J 8.x
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
