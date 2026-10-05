package pe.usil.cafe.controllers.middlewares;

import java.io.IOException;
import java.util.Collections;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import pe.usil.cafe.facades.AuthFacade;
import pe.usil.cafe.models.services.TokenStore.Sesion;
import pe.usil.cafe.util.JsonUtil;

/** Patrón Proxy: intercepta cada petición a /api/menu y /api/auth/logout y exige un token válido. */
public class AuthFilter implements Filter {

    private final AuthFacade facade = new AuthFacade();

    @Override
    public void init(FilterConfig cfg) { }

    @Override
    public void doFilter(ServletRequest rq, ServletResponse rs, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) rq;
        HttpServletResponse resp = (HttpServletResponse) rs;

        String token = extraerToken(req);
        Sesion sesion = (token == null) ? null : facade.validarToken(token);
        if (sesion == null) {
            JsonUtil.escribir(resp, 401, Collections.singletonMap("mensaje", "Sesión no válida o expirada."));
            return;
        }
        req.setAttribute("token", token);
        req.setAttribute("sesion", sesion);
        chain.doFilter(rq, rs);
    }

    public static String extraerToken(HttpServletRequest req) {
        String header = req.getHeader("Authorization");
        return (header != null && header.startsWith("Bearer ")) ? header.substring(7).trim() : null;
    }

    @Override
    public void destroy() { }
}
