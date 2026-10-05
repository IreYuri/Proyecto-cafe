package pe.usil.cafe.controllers;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import pe.usil.cafe.facades.AuthFacade;
import pe.usil.cafe.models.services.TokenStore.Sesion;
import pe.usil.cafe.util.JsonUtil;

/** GET /api/menu — protegido por AuthFilter, que ya deja la sesión en el request. */
@WebServlet("/api/menu")
public class MenuController extends HttpServlet {

    private final AuthFacade facade = new AuthFacade();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Sesion s = (Sesion) req.getAttribute("sesion");
        Map<String, Object> cuerpo = new LinkedHashMap<>();
        cuerpo.put("usuario", s.usuario);
        cuerpo.put("rol", s.rol);
        cuerpo.put("modulos", facade.menuPara(s.rol));
        JsonUtil.escribir(resp, 200, cuerpo);
    }
}
