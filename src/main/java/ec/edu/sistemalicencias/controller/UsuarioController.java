package ec.edu.sistemalicencias.controller;

import ec.edu.sistemalicencias.dao.UsuarioDAO;
import ec.edu.sistemalicencias.model.Usuario;
import org.mindrot.jbcrypt.BCrypt; // Requiere la dependencia en build.gradle

import java.util.List;

public class UsuarioController {

    private final UsuarioDAO dao = new UsuarioDAO();

    // LOGIN CON BCRYPT
    public Usuario login(String username, String passwordPlana) {
        Usuario u = dao.buscarPorUsername(username);
        
        if (u != null) {
            if (BCrypt.checkpw(passwordPlana, u.getPassword())) {
                if (!u.isActivo()) {
                    throw new RuntimeException("ERROR: Usuario Inactivo.\nConsulte al Administrador Master.");
                }
                
                return u; 
            }
        }
        return null; 
    }

    public List<Usuario> listar() {
        return dao.listarTodos();
    }

    public Usuario crear(String cedula, String nom1, String nom2, String ape1, String ape2,
                        String telf, String email, String pass, String rol, String creador) {
        
        // 1. Validaciones Fuertes
        if (nom1.length() < 3 || ape1.length() < 3) throw new RuntimeException("Nombre/Apellido muy cortos.");
        if (cedula.length() < 10) throw new RuntimeException("Cédula inválida.");
        
        // 2. Password Default y Encriptación
        String passwordFinal = (pass == null || pass.isEmpty()) ? "Sist.1234!" : pass;
        if (passwordFinal.length() < 5) throw new RuntimeException("Contraseña insegura.");
        
        // HASHEAR PASSWORD
        String hashedPassword = BCrypt.hashpw(passwordFinal, BCrypt.gensalt());

        // 3. Auto-Username (nom1[0] + ape1 + cedula[last4])
        String suffix = cedula.length() > 4 ? cedula.substring(cedula.length() - 4) : "0000";
        String autoUser = (nom1.substring(0,1) + ape1 + suffix).toLowerCase().replaceAll("\\s+", "");

        // 4. Verificar Duplicados
        String err = dao.verificarDuplicados(cedula, autoUser, email);
        if (err != null) throw new RuntimeException(err);

        Usuario u = new Usuario();
        u.setCedula(cedula);
        u.setPrimerNombre(nom1);
        u.setSegundoNombre(nom2);
        u.setPrimerApellido(ape1);
        u.setSegundoApellido(ape2);
        u.setTelefono(telf);
        u.setEmail(email);
        u.setUsername(autoUser);
        u.setPassword(hashedPassword); // Guardamos Hash
        u.setRol(rol);
        u.setCreadoPor(creador);
        u.setActivo(true);

        if (dao.crear(u)) return u;
        throw new RuntimeException("No se pudo guardar.");
    }

    public boolean actualizar(Usuario u) {
        return dao.actualizar(u);
    }

    // Lógica para CAMBIAR ESTADO (Soft)
    public boolean cambiarEstado(int id, boolean nuevoEstado) {
        return dao.cambiarEstado(id, nuevoEstado);
    }

    public boolean eliminarTotalmente(int id) {
        return dao.eliminarFisico(id);
    }

    public boolean cambiarPassword(int id, String nuevaPassPlana) {
        // El DAO se encarga de encriptarla con BCrypt
        return dao.actualizarPassword(id, nuevaPassPlana);
    }
}