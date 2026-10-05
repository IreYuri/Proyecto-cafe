package pe.usil.cafe.util;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import java.io.IOException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public final class JsonUtil {
    private static final Gson GSON = new Gson();

    private JsonUtil() { }

    public static <T> T leer(HttpServletRequest req, Class<T> tipo) throws IOException {
        req.setCharacterEncoding("UTF-8");
        try {
            return GSON.fromJson(req.getReader(), tipo);
        } catch (JsonParseException e) {
            return null;
        }
    }

    public static void escribir(HttpServletResponse resp, int status, Object cuerpo) throws IOException {
        resp.setStatus(status);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(GSON.toJson(cuerpo));
    }
}
