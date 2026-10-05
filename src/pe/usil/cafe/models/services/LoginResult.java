package pe.usil.cafe.models.services;

public class LoginResult {

    public enum Status {
        OK, DATOS_INCOMPLETOS, CREDENCIALES_INVALIDAS, PERFIL_INCORRECTO, INACTIVO, BLOQUEADO
    }

    public final Status status;
    public final String mensaje;
    public String token;
    public String usuario;
    public String rol;
    public Integer intentosRestantes;

    private LoginResult(Status status, String mensaje) {
        this.status = status;
        this.mensaje = mensaje;
    }

    public static LoginResult de(Status status, String mensaje) {
        return new LoginResult(status, mensaje);
    }

    public static LoginResult ok(String token, String usuario, String rol) {
        LoginResult r = new LoginResult(Status.OK, "Bienvenido.");
        r.token = token;
        r.usuario = usuario;
        r.rol = rol;
        return r;
    }
}
