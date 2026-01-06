package ec.edu.sistemalicencias.controller;

import ec.edu.sistemalicencias.dao.UsuarioDAO;
import ec.edu.sistemalicencias.model.Usuario;

import java.util.List;

public class UsuarioController {

    private final UsuarioDAO dao;

    public UsuarioController() {
        this.dao = new UsuarioDAO();
    }

    public List<Usuario> listarUsuarios() {
        return dao.findAll();
    }

    // ===== MÉTODO ORIGINAL
    public Usuario crearUsuario(String username, String password, String rol) {
        Usuario u = new Usuario(0, username, password, rol);
        return dao.create(u);
    }

    // ===== MÉTODO NUEVO CRUD ampliado =====
    public Usuario crearUsuario(String username, String password, String rol,
                                String nombre, String apellido, String telefono, String correo) {

        validarDatos(username, password, rol, nombre, apellido, telefono, correo);

        Usuario u = new Usuario(0, username, password, rol, nombre, apellido, telefono, correo);
        return dao.create(u);
    }

    // ===== MÉTODO ORIGINAL
    public boolean actualizarUsuario(int id, String username, String password, String rol) {
        Usuario u = new Usuario(id, username, password, rol);
        return dao.update(u);
    }

    // ===== MÉTODO NUEVO CRUD ampliado =====
    public boolean actualizarUsuario(int id, String username, String password, String rol,
                                     String nombre, String apellido, String telefono, String correo) {

        validarDatos(username, password, rol, nombre, apellido, telefono, correo);

        Usuario u = new Usuario(id, username, password, rol, nombre, apellido, telefono, correo);
        return dao.update(u);
    }

    public boolean eliminarUsuario(int id) {
        return dao.delete(id);
    }

    // ===== VALIDACIONES + EXCEPCIONES DE NEGOCIO =====
    // Aquí es donde va la lógica de validación (NO en el DAO).
    private void validarDatos(String username, String password, String rol,
                              String nombre, String apellido, String telefono, String correo) {

        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username es obligatorio.");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password es obligatorio.");
        }
        if (rol == null || rol.trim().isEmpty()) {
            throw new IllegalArgumentException("Rol es obligatorio.");
        }

        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("Nombre es obligatorio.");
        }
        if (apellido == null || apellido.trim().isEmpty()) {
            throw new IllegalArgumentException("Apellido es obligatorio.");
        }

        // Teléfono: solo números (7 a 15). Ajusta si tu profe pide otro formato.
        if (telefono == null || !telefono.trim().matches("\\d{7,15}")) {
            throw new IllegalArgumentException("Teléfono inválido (solo números, 7 a 15 dígitos).");
        }

        // Correo: validación simple
        if (correo == null || !correo.trim().matches("^[\\w._%+-]+@[\\w.-]+\\.[A-Za-z]{2,}$")) {
            throw new IllegalArgumentException("Correo inválido.");
        }
    }
}




