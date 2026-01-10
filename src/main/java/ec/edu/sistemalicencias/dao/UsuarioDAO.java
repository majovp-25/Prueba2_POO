package ec.edu.sistemalicencias.dao;

import ec.edu.sistemalicencias.config.DatabaseConfig;
import ec.edu.sistemalicencias.model.Usuario;
import ec.edu.sistemalicencias.model.exceptions.BaseDatosException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    public Usuario login(String username, String password) {
        Usuario usuarioEncontrado = null;
        String sql = "SELECT id, nombres, apellidos, telefono, email, username, password, rol " +
                "FROM usuarios WHERE username = ? AND password = ?";

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

        } catch (BaseDatosException e) {
            throw new RuntimeException("Error en el login: " + e.getMessage(), e);
        } catch (SQLException e) {
            throw new RuntimeException("Error en el login: " + e.getMessage(), e);
        }

        return usuarioEncontrado;
    }

    public List<Usuario> findAll() {
        List<Usuario> lista = new ArrayList<>();

        // Selecciona columnas explícitas y mapea por nombre (evita orden incorrecto)
        String sql = "SELECT id, nombres, apellidos, telefono, email, username, password, rol, creado_por " +
                "FROM usuarios ORDER BY id";

        try (Connection cn = DatabaseConfig.getInstance().obtenerConexion();
            PreparedStatement ps = cn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Usuario u = new Usuario();
                u.setId(rs.getInt("id"));
                u.setNombre(rs.getString("nombres"));
                u.setApellido(rs.getString("apellidos"));
                u.setTelefono(rs.getString("telefono"));
                u.setEmail(rs.getString("email"));
                u.setUsername(rs.getString("username"));
                u.setPassword(rs.getString("password"));
                u.setRol(rs.getString("rol"));
                String creador = rs.getString("creado_por");
                u.setCreadoPor(creador != null ? creador : "-");
                lista.add(u);
            }

        } catch (BaseDatosException e) {
            throw new RuntimeException("Error listando usuarios: " + e.getMessage(), e);
        } catch (SQLException e) {
            throw new RuntimeException("Error listando usuarios: " + e.getMessage(), e);
        }

        return lista;
    }

    public Usuario create(Usuario u) {
        String sql = "INSERT INTO usuarios (username, password, rol, nombres, apellidos, telefono, email, creado_por) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?) RETURNING id";

        try (Connection cn = DatabaseConfig.getInstance().obtenerConexion();
            PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, u.getUsername());
            ps.setString(2, u.getPassword());
            ps.setString(3, u.getRol());
            ps.setString(4, u.getNombre());
            ps.setString(5, u.getApellido());
            ps.setString(6, u.getTelefono());
            ps.setString(7, u.getEmail());
            ps.setString(8, u.getCreadoPor());

            try (ResultSet keys = ps.executeQuery()) {
                if (keys.next()) {
                    u.setId(keys.getInt(1));
                    return u;
                }
            }

            return null;

        } catch (BaseDatosException e) {
            throw new RuntimeException("Error creando usuario: " + e.getMessage(), e);
        } catch (SQLException e) {
            throw new RuntimeException("Error creando usuario: " + e.getMessage(), e);
        }
    }

    public boolean update(Usuario u) {
        String sql = "UPDATE usuarios SET username = ?, password = ?, rol = ?, nombres = ?, apellidos = ?, telefono = ?, email = ? " +
                "WHERE id = ?";

        try (Connection cn = DatabaseConfig.getInstance().obtenerConexion();
            PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, u.getUsername());
            ps.setString(2, u.getPassword());
            ps.setString(3, u.getRol());
            ps.setString(4, u.getNombre());
            ps.setString(5, u.getApellido());
            ps.setString(6, u.getTelefono());
            ps.setString(7, u.getEmail());
            ps.setInt(8, u.getId());

            return ps.executeUpdate() > 0;

        } catch (BaseDatosException e) {
            throw new RuntimeException("Error actualizando usuario: " + e.getMessage(), e);
        } catch (SQLException e) {
            throw new RuntimeException("Error actualizando usuario: " + e.getMessage(), e);
        }
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM usuarios WHERE id = ?";

        try (Connection cn = DatabaseConfig.getInstance().obtenerConexion();
            PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (BaseDatosException e) {
            throw new RuntimeException("Error eliminando usuario: " + e.getMessage(), e);
        } catch (SQLException e) {
            throw new RuntimeException("Error eliminando usuario: " + e.getMessage(), e);
        }
    }

    public boolean actualizarPassword(int id, String nuevaPassword) {
        String sql = "UPDATE usuarios SET password = ? WHERE id = ?";

        try (Connection cn = DatabaseConfig.getInstance().obtenerConexion();
            PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, nuevaPassword);
            ps.setInt(2, id);

            return ps.executeUpdate() > 0;

        } catch (BaseDatosException e) {
            throw new RuntimeException("Error cambiando password: " + e.getMessage(), e);
        } catch (SQLException e) {
            throw new RuntimeException("Error cambiando password: " + e.getMessage(), e);
        }
    }

    // Método para verificar duplicados
    public String verificarDuplicados(String username, String email, String telefono) {
        String sql = "SELECT username, email, telefono FROM usuarios WHERE username = ? OR email = ? OR telefono = ?";
        
        try (Connection cn = DatabaseConfig.getInstance().obtenerConexion();
            PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, email);
            ps.setString(3, telefono);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String dbUser = rs.getString("username");
                    String dbEmail = rs.getString("email");
                    String dbTelf = rs.getString("telefono");

                    if (dbUser.equalsIgnoreCase(username)) return "El Usuario '" + username + "' ya existe.";
                    if (dbEmail.equalsIgnoreCase(email)) return "El Correo '" + email + "' ya está registrado.";
                    if (dbTelf.equals(telefono)) return "El Teléfono '" + telefono + "' ya está registrado.";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null; 
    }
}


