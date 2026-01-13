package ec.edu.sistemalicencias.model;
import java.sql.Timestamp;
public class Usuario {
    private int id;
    private String cedula;
    private String primerNombre;
    private String segundoNombre;
    private String primerApellido;
    private String segundoApellido;
    private String telefono;
    private String email;
    private String username;
    private String password;
    private String rol;
    private String creadoPor;
    private boolean activo;
    private Timestamp fechaRegistro;

    public Usuario() {}

    // Constructor completo
    public Usuario(int id, String cedula, String primerNombre, String segundoNombre, 
                String primerApellido, String segundoApellido, String telefono, 
                String email, String username, String password, String rol, boolean activo) {
        this.id = id;
        this.cedula = cedula;
        this.primerNombre = primerNombre;
        this.segundoNombre = segundoNombre;
        this.primerApellido = primerApellido;
        this.segundoApellido = segundoApellido;
        this.telefono = telefono;
        this.email = email;
        this.username = username;
        this.password = password;
        this.rol = rol;
        this.activo = activo;
    }
    public Timestamp getFechaRegistro() { return fechaRegistro; }
public void setFechaRegistro(Timestamp fechaRegistro) { this.fechaRegistro = fechaRegistro; }

    // Helper para reportes y tablas
    public String getNombreCompleto() {
        return primerNombre + " " + (segundoNombre != null ? segundoNombre : "") + " " +
            primerApellido + " " + (segundoApellido != null ? segundoApellido : "");
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getCedula() { return cedula; }
    public void setCedula(String cedula) { this.cedula = cedula; }
    public String getPrimerNombre() { return primerNombre; }
    public void setPrimerNombre(String primerNombre) { this.primerNombre = primerNombre; }
    public String getSegundoNombre() { return segundoNombre; }
    public void setSegundoNombre(String segundoNombre) { this.segundoNombre = segundoNombre; }
    public String getPrimerApellido() { return primerApellido; }
    public void setPrimerApellido(String primerApellido) { this.primerApellido = primerApellido; }
    public String getSegundoApellido() { return segundoApellido; }
    public void setSegundoApellido(String segundoApellido) { this.segundoApellido = segundoApellido; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }
    public String getCreadoPor() { return creadoPor; }
    public void setCreadoPor(String creadoPor) { this.creadoPor = creadoPor; }
    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
}