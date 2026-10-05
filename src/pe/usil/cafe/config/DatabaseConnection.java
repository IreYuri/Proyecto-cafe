package pe.usil.cafe.config;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Patrón Singleton: una sola configuración de conexión para toda la app.
 * Lee WEB-INF/classes/db.properties (copiado desde src/db.properties al compilar);
 * si falta algún dato, intenta las variables de entorno DB_URL, DB_USER, DB_PASSWORD.
 */
public class DatabaseConnection {

    private static DatabaseConnection instance;
    private final String url;
    private final String user;
    private final String password;

    private DatabaseConnection() {
        Properties props = new Properties();
        try (InputStream in = DatabaseConnection.class.getResourceAsStream("/db.properties")) {
            if (in != null) {
                props.load(in);
            }
        } catch (IOException e) {
            throw new IllegalStateException("No se pudo leer db.properties", e);
        }
        url = valor(props, "db.url", "DB_URL", "jdbc:postgresql://localhost:5432/cosecha_comun");
        user = valor(props, "db.user", "DB_USER", "postgres");
        password = valor(props, "db.password", "DB_PASSWORD", "postgres");
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("Falta el driver de PostgreSQL en WEB-INF/lib", e);
        }
    }

    public static synchronized DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    private static String valor(Properties props, String clavePropiedad, String claveEnv, String porDefecto) {
        String v = props.getProperty(clavePropiedad);
        if (v == null || v.isEmpty()) {
            v = System.getenv(claveEnv);
        }
        return (v == null || v.isEmpty()) ? porDefecto : v;
    }
}
