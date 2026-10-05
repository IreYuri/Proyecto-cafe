package pe.usil.cafe.models.services;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/** Patrón Singleton: guarda las sesiones activas en memoria (se pierden al reiniciar Tomcat). */
public class TokenStore {

    public static class Sesion {
        public final String usuario;
        public final String rol;
        private final long expira;

        Sesion(String usuario, String rol, long expira) {
            this.usuario = usuario;
            this.rol = rol;
            this.expira = expira;
        }
    }

    private static final TokenStore INSTANCE = new TokenStore();
    private static final long DURACION_MS = 60L * 60 * 1000;   // 1 hora

    private final Map<String, Sesion> sesiones = new ConcurrentHashMap<>();

    private TokenStore() { }

    public static TokenStore getInstance() { return INSTANCE; }

    public String crear(String usuario, String rol) {
        String token = UUID.randomUUID().toString();
        sesiones.put(token, new Sesion(usuario, rol, System.currentTimeMillis() + DURACION_MS));
        return token;
    }

    public Sesion obtener(String token) {
        Sesion s = sesiones.get(token);
        if (s != null && s.expira < System.currentTimeMillis()) {
            sesiones.remove(token);
            return null;
        }
        return s;
    }

    public void eliminar(String token) {
        if (token != null) {
            sesiones.remove(token);
        }
    }
}
