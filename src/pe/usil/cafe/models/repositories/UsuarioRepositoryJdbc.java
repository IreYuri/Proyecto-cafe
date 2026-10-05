package pe.usil.cafe.models.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import pe.usil.cafe.config.DatabaseConnection;
import pe.usil.cafe.models.entities.Usuario;

public class UsuarioRepositoryJdbc implements UsuarioRepository {

    private static final String SQL_BUSCAR =
            "SELECT u.id_usuario, u.nombre_usuario, u.contrasena, u.intentos_fallidos, u.bloqueado, u.estado, r.nombre_rol "
          + "FROM usuarios u JOIN rol r ON r.rol_id = u.rol_id WHERE u.nombre_usuario = ?";

    @Override
    public Usuario buscarPorNombreUsuario(String nombreUsuario) throws SQLException {
        try (Connection cn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = cn.prepareStatement(SQL_BUSCAR)) {
            ps.setString(1, nombreUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }
                return new Usuario(rs.getInt("id_usuario"), rs.getString("nombre_usuario"),
                        rs.getString("contrasena"), rs.getString("nombre_rol"),
                        rs.getInt("intentos_fallidos"), rs.getBoolean("bloqueado"), rs.getString("estado"));
            }
        }
    }

    @Override
    public void registrarFallo(int idUsuario, int intentos, boolean bloqueado) throws SQLException {
        String sql = "UPDATE usuarios SET intentos_fallidos = ?, bloqueado = ?, fecha_actualizacion = NOW() WHERE id_usuario = ?";
        try (Connection cn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, intentos);
            ps.setBoolean(2, bloqueado);
            ps.setInt(3, idUsuario);
            ps.executeUpdate();
        }
    }

    @Override
    public void registrarAccesoExitoso(int idUsuario) throws SQLException {
        String sql = "UPDATE usuarios SET intentos_fallidos = 0, ultimo_acceso = NOW() WHERE id_usuario = ?";
        try (Connection cn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            ps.executeUpdate();
        }
    }
}
