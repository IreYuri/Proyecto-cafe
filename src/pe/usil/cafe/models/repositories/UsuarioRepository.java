package pe.usil.cafe.models.repositories;

import java.sql.SQLException;
import pe.usil.cafe.models.entities.Usuario;

/** Patrón Repository: el servicio no sabe si los datos vienen de PostgreSQL o de otra fuente. */
public interface UsuarioRepository {
    Usuario buscarPorNombreUsuario(String nombreUsuario) throws SQLException;
    void registrarFallo(int idUsuario, int intentos, boolean bloqueado) throws SQLException;
    void registrarAccesoExitoso(int idUsuario) throws SQLException;
}
