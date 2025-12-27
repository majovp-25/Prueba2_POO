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

    public Usuario crearUsuario(String username, String password, String rol) {
        Usuario u = new Usuario(0, username, password, rol);
        return dao.create(u);
    }

    public boolean actualizarUsuario(int id, String username, String password, String rol) {
        Usuario u = new Usuario(id, username, password, rol);
        return dao.update(u);
    }

    public boolean eliminarUsuario(int id) {
        return dao.delete(id);
    }
}

