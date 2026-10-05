package pe.usil.cafe.util;

import org.mindrot.jbcrypt.BCrypt;

public final class PasswordHasher {
    private PasswordHasher() { }

    public static String hash(String clave) {
        return BCrypt.hashpw(clave, BCrypt.gensalt(10));
    }

    public static boolean verificar(String clave, String hash) {
        try {
            return BCrypt.checkpw(clave, hash);
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
