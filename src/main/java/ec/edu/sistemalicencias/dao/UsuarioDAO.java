package ec.edu.sistemalicencias.dao;

import ec.edu.sistemalicencias.config.DatabaseConfig;
import ec.edu.sistemalicencias.model.Usuario;
import ec.edu.sistemalicencias.model.exceptions.BaseDatosException;
import org.mindrot.jbcrypt.BCrypt; // Necesario para encriptar al actualizar

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    // 1. LISTAR TODOS (Modificado: Quitamos 'WHERE activo = TRUE')
    public List<Usuario> listarTodos() { // Antes se llamaba listarActivos
        List<Usuario> lista = new ArrayList<>();
        // Traemos todos para que se vean en la tabla (activos e inactivos)
        String sql = "SELECT * FROM usuarios ORDER BY id ASC"; 
        
        try (Connection cn = DatabaseConfig.getInstance().obtenerConexion();
            PreparedStatement ps = cn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapearUsuario(rs));
            }
        } catch (SQLException | BaseDatosException e) {
            throw new RuntimeException("Error listando usuarios: " + e.getMessage());
        }
        return lista;
    }

    // 2. CREAR (4 nombres + cédula)
    public boolean crear(Usuario u) {
        String sql = "INSERT INTO usuarios (cedula, primer_nombre, segundo_nombre, primer_apellido, segundo_apellido, " +
                    "telefono, email, username, password, rol, creado_por, activo) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
        try (Connection cn = DatabaseConfig.getInstance().obtenerConexion();
            PreparedStatement ps = cn.prepareStatement(sql)) {
            
            ps.setString(1, u.getCedula());
            ps.setString(2, u.getPrimerNombre());
            ps.setString(3, u.getSegundoNombre());
            ps.setString(4, u.getPrimerApellido());
            ps.setString(5, u.getSegundoApellido());
            ps.setString(6, u.getTelefono());
            ps.setString(7, u.getEmail());
            ps.setString(8, u.getUsername());
            ps.setString(9, u.getPassword()); // YA VIENE HASHEADA DEL CONTROLLER
            ps.setString(10, u.getRol());
            ps.setString(11, u.getCreadoPor());
            ps.setBoolean(12, true);

            return ps.executeUpdate() > 0;
        } catch (SQLException | BaseDatosException e) {
            throw new RuntimeException("Error creando usuario: " + e.getMessage());
        }
    }

    // 3. ACTUALIZAR
    public boolean actualizar(Usuario u) {
        String sql = "UPDATE usuarios SET cedula=?, primer_nombre=?, segundo_nombre=?, primer_apellido=?, segundo_apellido=?, " +
                    "telefono=?, email=?, rol=? WHERE id=?";
        try (Connection cn = DatabaseConfig.getInstance().obtenerConexion();
            PreparedStatement ps = cn.prepareStatement(sql)) {
            
            ps.setString(1, u.getCedula());
            ps.setString(2, u.getPrimerNombre());
            ps.setString(3, u.getSegundoNombre());
            ps.setString(4, u.getPrimerApellido());
            ps.setString(5, u.getSegundoApellido());
            ps.setString(6, u.getTelefono());
            ps.setString(7, u.getEmail());
            ps.setString(8, u.getRol());
            ps.setInt(9, u.getId());

            return ps.executeUpdate() > 0;
        } catch (SQLException | BaseDatosException e) {
            throw new RuntimeException("Error actualizando: " + e.getMessage());
        }
    }

    // 4. SOFT DELETE (Cambiar estado)
    public boolean cambiarEstado(int id, boolean activo) {
        String sql = "UPDATE usuarios SET activo = ? WHERE id = ?";
        try (Connection cn = DatabaseConfig.getInstance().obtenerConexion();
            PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setBoolean(1, activo);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException | BaseDatosException e) {
            throw new RuntimeException("Error cambiando estado: " + e.getMessage());
        }
    }

    // 5. LOGIN
    public Usuario buscarPorUsername(String username) {
        String sql = "SELECT * FROM usuarios WHERE username = ?"; // <-- CAMBIO AQUÍ
        
        try (Connection cn = DatabaseConfig.getInstance().obtenerConexion();
            PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapearUsuario(rs);
            }
        } catch (SQLException | BaseDatosException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 6. VALIDAR DUPLICADOS
    public String verificarDuplicados(String cedula, String username, String email) {
        String sql = "SELECT cedula, username, email FROM usuarios WHERE cedula=? OR username=? OR email=?";
        try (Connection cn = DatabaseConfig.getInstance().obtenerConexion();
            PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, cedula);
            ps.setString(2, username);
            ps.setString(3, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    if (cedula.equals(rs.getString("cedula"))) return "Cédula ya registrada.";
                    if (username.equals(rs.getString("username"))) return "Username ocupado.";
                    if (email.equals(rs.getString("email"))) return "Email ya registrado.";
                }
            }
        } catch (SQLException | BaseDatosException e) { e.printStackTrace(); }
        return null;
    }

    // 7. ACTUALIZAR PASSWORD (ESTE ES EL QUE FALTABA)
    public boolean actualizarPassword(int id, String nuevaPassword) {
        // ¡IMPORTANTE! Como es la versión PRO, debemos encriptar aquí también.
        // La vista manda texto plano, nosotros lo convertimos a Hash BCrypt.
        String hash = BCrypt.hashpw(nuevaPassword, BCrypt.gensalt());

        String sql = "UPDATE usuarios SET password = ? WHERE id = ?";
        try (Connection cn = DatabaseConfig.getInstance().obtenerConexion();
            PreparedStatement ps = cn.prepareStatement(sql)) {
            
            ps.setString(1, hash); // Guardamos el hash, no el texto plano
            ps.setInt(2, id);
            
            return ps.executeUpdate() > 0;
        } catch (SQLException | BaseDatosException e) {
            throw new RuntimeException("Error actualizando password: " + e.getMessage());
        }
    }

    private Usuario mapearUsuario(ResultSet rs) throws SQLException {
        Usuario u = new Usuario();
        u.setId(rs.getInt("id"));
        u.setCedula(rs.getString("cedula"));
        u.setPrimerNombre(rs.getString("primer_nombre"));
        u.setSegundoNombre(rs.getString("segundo_nombre"));
        u.setPrimerApellido(rs.getString("primer_apellido"));
        u.setSegundoApellido(rs.getString("segundo_apellido"));
        u.setTelefono(rs.getString("telefono"));
        u.setEmail(rs.getString("email"));
        u.setUsername(rs.getString("username"));
        u.setPassword(rs.getString("password"));
        u.setRol(rs.getString("rol"));
        u.setCreadoPor(rs.getString("creado_por"));
        u.setActivo(rs.getBoolean("activo"));
        u.setFechaRegistro(rs.getTimestamp("fecha_registro"));
        return u;
    }

    public boolean eliminarFisico(int id) {
        String sql = "DELETE FROM usuarios WHERE id = ?";
        try (Connection cn = DatabaseConfig.getInstance().obtenerConexion();
            PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException | BaseDatosException e) {
            // Error común: Llave foránea (si el usuario ya creó licencias, la BD no dejará borrarlo)
            throw new RuntimeException("No se puede eliminar: El usuario tiene registros asociados.");
        }
    }
}