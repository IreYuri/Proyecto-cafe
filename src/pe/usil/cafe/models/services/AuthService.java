package pe.usil.cafe.models.services;

import java.sql.SQLException;
import pe.usil.cafe.models.entities.Usuario;
import pe.usil.cafe.models.repositories.UsuarioRepository;
import pe.usil.cafe.models.services.LoginResult.Status;
import pe.usil.cafe.util.PasswordHasher;

public class AuthService {

    public static final int MAX_INTENTOS = 3;

    private final UsuarioRepository repo;
    private final TokenStore tokens;

    public AuthService(UsuarioRepository repo, TokenStore tokens) {
        this.repo = repo;
        this.tokens = tokens;
    }

    /**
     * @param perfilSeleccionado el valor de la pestaña elegida en el login (CAFICULTOR, COMPRADOR o
     *                           ADMINISTRADOR). Si no coincide con el rol real del usuario, se avisa
     *                           sin contar el intento como fallido: la contraseña sí era correcta.
     */
    public LoginResult login(String nombre, String clave, String perfilSeleccionado) throws SQLException {
        if (nombre == null || nombre.trim().isEmpty() || clave == null || clave.isEmpty()) {
            return LoginResult.de(Status.DATOS_INCOMPLETOS, "Ingresa usuario y contraseña.");
        }
        Usuario u = repo.buscarPorNombreUsuario(nombre.trim());
        if (u == null) {
            return LoginResult.de(Status.CREDENCIALES_INVALIDAS, "Usuario o contraseña incorrectos.");
        }
        if (!"Activo".equals(u.getEstado())) {
            return LoginResult.de(Status.INACTIVO, "Usuario inactivo. Contacta al administrador.");
        }
        if (u.isBloqueado()) {
            return LoginResult.de(Status.BLOQUEADO, "Cuenta bloqueada por 3 intentos fallidos. Contacta al administrador.");
        }
        if (!PasswordHasher.verificar(clave, u.getContrasena())) {
            int intentos = u.getIntentosFallidos() + 1;
            boolean bloquear = intentos >= MAX_INTENTOS;
            repo.registrarFallo(u.getIdUsuario(), intentos, bloquear);
            if (bloquear) {
                return LoginResult.de(Status.BLOQUEADO, "Cuenta bloqueada tras 3 intentos fallidos.");
            }
            LoginResult r = LoginResult.de(Status.CREDENCIALES_INVALIDAS, "Usuario o contraseña incorrectos.");
            r.intentosRestantes = MAX_INTENTOS - intentos;
            return r;
        }
        if (perfilSeleccionado != null && !perfilSeleccionado.isEmpty()
                && !perfilSeleccionado.equalsIgnoreCase(u.getRolNombre())) {
            return LoginResult.de(Status.PERFIL_INCORRECTO,
                    "Esta cuenta no corresponde al perfil seleccionado. Elige \"" + nombreLegible(u.getRolNombre()) + "\".");
        }
        repo.registrarAccesoExitoso(u.getIdUsuario());
        String token = tokens.crear(u.getNombreUsuario(), u.getRolNombre());
        return LoginResult.ok(token, u.getNombreUsuario(), u.getRolNombre());
    }

    private String nombreLegible(String rol) {
        switch (rol) {
            case "CAFICULTOR": return "Caficultor";
            case "COMPRADOR": return "Comprador";
            case "ADMINISTRADOR": return "Administrador";
            default: return rol;
        }
    }
}
