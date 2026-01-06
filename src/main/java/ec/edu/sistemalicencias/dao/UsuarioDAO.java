package ec.edu.sistemalicencias.dao;

import ec.edu.sistemalicencias.config.DatabaseConfig;
import ec.edu.sistemalicencias.model.Usuario;
import ec.edu.sistemalicencias.model.exceptions.BaseDatosException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class UsuarioDAO {

    public Usuario login(String username, String password) {
        Usuario usuarioEncontrado = null;
        String sql = "SELECT * FROM usuarios WHERE username = ? AND password = ?";

        try (Connection conn = DatabaseConfig.getInstance().obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, password);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    usuarioEncontrado = new Usuario();
                    usuarioEncontrado.setId(rs.getInt("id"));
                    usuarioEncontrado.setNombre(rs.getString("nombres"));
                    usuarioEncontrado.setApellido(rs.getString("apellidos"));
                    usuarioEncontrado.setTelefono(rs.getString("telefono"));
                    usuarioEncontrado.setEmail(rs.getString("email"));
                    usuarioEncontrado.setUsername(rs.getString("username"));
                    usuarioEncontrado.setPassword(rs.getString("password"));
                    usuarioEncontrado.setRol(rs.getString("rol"));
                }
            }
        } catch (Exception e) {
            System.err.println("Error en el login: " + e.getMessage());
        }
        return usuarioEncontrado;
    }
    public List<Usuario> findAll() {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT id, username, password, rol, nombres, apellidos, telefono, email FROM usuarios ORDER BY id";


        try (Connection cn = DatabaseConfig.getInstance().obtenerConexion();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Usuario u = new Usuario(
                        rs.getInt("id"),
                        rs.getString("nombres"),
                        rs.getString("apellidos"),
                        rs.getString("telefono"),
                        rs.getString("email"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("rol")
                );
                lista.add(u);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error listando usuarios: " + e.getMessage(), e);
        } catch (BaseDatosException e) {
            throw new RuntimeException(e);
        }
        return lista;
    }

    public Usuario create(Usuario u) {
        String sql = "INSERT INTO usuarios (username, password, rol, nombres, apellidos, telefono, email) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection cn = DatabaseConfig.getInstance().obtenerConexion();
             PreparedStatement ps = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            // Orden correcto según el SQL:
            ps.setString(1, u.getUsername());
            ps.setString(2, u.getPassword());
            ps.setString(3, u.getRol());
            ps.setString(4, u.getNombre());
            ps.setString(5, u.getApellido());
            ps.setString(6, u.getTelefono());
            ps.setString(7, u.getEmail());

            int rows = ps.executeUpdate();
            if (rows == 0) return null;

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    u.setId(keys.getInt(1));
                    return u;
                }
            }
            return null;

        } catch (SQLException e) {
            throw new RuntimeException("Error creando usuario: " + e.getMessage(), e);
        } catch (BaseDatosException e) {
            throw new RuntimeException(e);
        }
    }



    public boolean update(Usuario u) {
        String sql = "UPDATE usuarios SET username = ?, password = ?, rol = ?, nombres = ?, apellidos = ?, telefono = ?, email = ? " +
                "WHERE id = ?";

        try (Connection cn = DatabaseConfig.getInstance().obtenerConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            // Orden correcto según el SQL:
            ps.setString(1, u.getUsername());
            ps.setString(2, u.getPassword());
            ps.setString(3, u.getRol());
            ps.setString(4, u.getNombre());
            ps.setString(5, u.getApellido());
            ps.setString(6, u.getTelefono());
            ps.setString(7, u.getEmail());
            ps.setInt(8, u.getId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Error actualizando usuario: " + e.getMessage(), e);
        } catch (BaseDatosException e) {
            throw new RuntimeException(e);
        }
    }


    public boolean delete(int id) {
        String sql = "DELETE FROM usuarios WHERE id = ?";
        try (Connection cn = DatabaseConfig.getInstance().obtenerConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Error eliminando usuario: " + e.getMessage(), e);
        } catch (BaseDatosException e) {
            throw new RuntimeException(e);
        }
    }

}
