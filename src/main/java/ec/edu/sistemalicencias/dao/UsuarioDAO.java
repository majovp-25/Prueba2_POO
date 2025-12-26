package ec.edu.sistemalicencias.dao;

import ec.edu.sistemalicencias.config.DatabaseConfig;
import ec.edu.sistemalicencias.model.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

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
}