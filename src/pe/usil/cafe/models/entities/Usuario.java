package pe.usil.cafe.models.entities;

public class Usuario {
    private final int idUsuario;
    private final String nombreUsuario;
    private final String contrasena;
    private final String rolNombre;
    private final int intentosFallidos;
    private final boolean bloqueado;
    private final String estado;

    public Usuario(int idUsuario, String nombreUsuario, String contrasena, String rolNombre,
                    int intentosFallidos, boolean bloqueado, String estado) {
        this.idUsuario = idUsuario;
        this.nombreUsuario = nombreUsuario;
        this.contrasena = contrasena;
        this.rolNombre = rolNombre;
        this.intentosFallidos = intentosFallidos;
        this.bloqueado = bloqueado;
        this.estado = estado;
    }

    public int getIdUsuario() { return idUsuario; }
    public String getNombreUsuario() { return nombreUsuario; }
    public String getContrasena() { return contrasena; }
    public String getRolNombre() { return rolNombre; }
    public int getIntentosFallidos() { return intentosFallidos; }
    public boolean isBloqueado() { return bloqueado; }
    public String getEstado() { return estado; }
}
