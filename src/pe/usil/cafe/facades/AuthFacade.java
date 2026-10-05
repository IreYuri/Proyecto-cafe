package pe.usil.cafe.facades;

import java.sql.SQLException;
import java.util.List;
import pe.usil.cafe.models.repositories.UsuarioRepositoryJdbc;
import pe.usil.cafe.models.services.AuthService;
import pe.usil.cafe.models.services.LoginResult;
import pe.usil.cafe.models.services.MenuService;
import pe.usil.cafe.models.services.TokenStore;

/** Patrón Facade: los controladores (servlets) hablan solo con esta clase. */
public class AuthFacade {

    private final TokenStore tokens = TokenStore.getInstance();
    private final AuthService auth = new AuthService(new UsuarioRepositoryJdbc(), tokens);
    private final MenuService menu = new MenuService();

    public LoginResult login(String usuario, String clave, String perfilSeleccionado) throws SQLException {
        return auth.login(usuario, clave, perfilSeleccionado);
    }

    public TokenStore.Sesion validarToken(String token) {
        return tokens.obtener(token);
    }

    public void cerrarSesion(String token) {
        tokens.eliminar(token);
    }

    public List<MenuService.Modulo> menuPara(String rol) {
        return menu.modulosPara(rol);
    }
}
