package ec.edu.sistemalicencias;

import ec.edu.sistemalicencias.config.DatabaseConfig;
import ec.edu.sistemalicencias.dao.UsuarioDAO;
import ec.edu.sistemalicencias.model.Usuario;
import ec.edu.sistemalicencias.view.LoginView;
import ec.edu.sistemalicencias.view.MainView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main {

    public static void main(String[] args) {

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("No se pudo establecer el Look and Feel: " + e.getMessage());
        }

        DatabaseConfig.getInstance();

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                mostrarLogin();
            }
        });
    }

    // IMPORTANTE: esto lo llamas al inicio y también al cerrar sesión
    public static void mostrarLogin() {

        final UsuarioDAO dao = new UsuarioDAO();
        final LoginView loginView = new LoginView();
        loginView.setVisible(true);

        loginView.addIngresarListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String username = loginView.getUsername();
                String password = loginView.getPassword();

                Usuario usuario = dao.login(username, password);

                if (usuario == null) {
                    loginView.showError("Credenciales incorrectas o error de conexión.");
                    loginView.clearFields();
                    return;
                }

                String rol = usuario.getRol();

                MainView mainView = new MainView(usuario);
                mainView.aplicarPermisosPorRol(rol);
                mainView.setVisible(true);

                loginView.dispose();
            }
        });
    }
}


