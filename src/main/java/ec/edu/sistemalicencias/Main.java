package ec.edu.sistemalicencias;

import ec.edu.sistemalicencias.config.DatabaseConfig;
import ec.edu.sistemalicencias.dao.UsuarioDAO;
import ec.edu.sistemalicencias.model.Usuario;
import ec.edu.sistemalicencias.view.LoginView;
import ec.edu.sistemalicencias.view.MainView;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {

        // 1) Look & Feel (antes de crear cualquier JFrame)
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("No se pudo establecer el Look and Feel: " + e.getMessage());
        }

        // 2) Inicializar config BD (si tu singleton hace conexión/validación)
        DatabaseConfig.getInstance();

        // 3) Swing siempre en EDT
        SwingUtilities.invokeLater(() -> {

            UsuarioDAO dao = new UsuarioDAO();

            // Mostrar Login
            LoginView loginView = new LoginView();
            loginView.setVisible(true);

            // Evento del botón Ingresar
            loginView.addIngresarListener(e -> {

                String username = loginView.getUsername();
                String password = loginView.getPassword();

                Usuario usuario = dao.login(username, password);

                if (usuario == null) {
                    loginView.showError("Credenciales incorrectas o error de conexión.");
                    loginView.clearFields();
                    return;
                }

                String rol = usuario.getRol(); // <-- AQUÍ sale tu rol real desde BD

                // Abrir MainView y aplicar permisos
                MainView mainView = new MainView();
                mainView.aplicarPermisosPorRol(rol);
                mainView.setVisible(true);

                // Cerrar Login
                loginView.dispose();
            });
        });
    }
}
