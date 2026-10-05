package pe.usil.cafe.controllers;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collections;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import pe.usil.cafe.controllers.middlewares.AuthFilter;
import pe.usil.cafe.facades.AuthFacade;
import pe.usil.cafe.models.services.LoginResult;
import pe.usil.cafe.util.JsonUtil;

/** POST /api/auth/login (público) y POST /api/auth/logout (protegido por AuthFilter). */
@WebServlet(urlPatterns = {"/api/auth/login", "/api/auth/logout"})
public class AuthController extends HttpServlet {

    private final AuthFacade facade = new AuthFacade();

    static class LoginRequest {
        String usuario;
        String clave;
        String perfil;   // CAFICULTOR, COMPRADOR o ADMINISTRADOR, según la pestaña elegida
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (req.getServletPath().endsWith("/logout")) {
            facade.cerrarSesion(AuthFilter.extraerToken(req));
            JsonUtil.escribir(resp, 200, Collections.singletonMap("mensaje", "Sesión cerrada."));
            return;
        }
        LoginRequest body = JsonUtil.leer(req, LoginRequest.class);
        if (body == null) {
            body = new LoginRequest();
        }
        try {
            LoginResult r = facade.login(body.usuario, body.clave, body.perfil);
            JsonUtil.escribir(resp, codigoHttp(r.status), r);
        } catch (SQLException e) {
            log("Error de base de datos en login", e);
            JsonUtil.escribir(resp, 500, Collections.singletonMap("mensaje", "Error de base de datos."));
        }
    }

    private int codigoHttp(LoginResult.Status s) {
        switch (s) {
            case OK: return 200;
            case DATOS_INCOMPLETOS: return 400;
            case CREDENCIALES_INVALIDAS: return 401;
            case PERFIL_INCORRECTO: return 409;
            case INACTIVO: return 403;
            default: return 423;   // BLOQUEADO
        }
    }
}
