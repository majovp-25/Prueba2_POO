package ec.edu.sistemalicencias;

import ec.edu.sistemalicencias.config.DatabaseConfig;
import ec.edu.sistemalicencias.view.MainView;

import javax.swing.*;

/**
 * Clase principal del Sistema de Licencias de Conducir del Ecuador.
 * Punto de entrada de la aplicación.
 *
 * @author Sistema Licencias Ecuador
 * @version 1.0
 */
public class Main {

    /**
     * Método principal que inicia la aplicación
     */
    public static void main(String[] args) {
        // Configurar Look and Feel del sistema operativo (Para que se vea nativo como Windows)
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("No se pudo establecer el Look and Feel: " + e.getMessage());
        }

        // Instanciamos la configuración (Singleton)
        DatabaseConfig dbConfig = DatabaseConfig.getInstance();

        SwingUtilities.invokeLater(() -> {
            // Mostrar splash screen o mensaje de inicio actualizado
            mostrarPantallaInicio();

            // Verificar conexión a BD (Ahora verifica contra Railway/Postgres)
            if (!dbConfig.verificarConexion()) {
                mostrarErrorConexion();
                return; // Detenemos la ejecución si no hay red o base de datos
            }

            // Si todo sale bien, iniciamos la ventana principal
            MainView mainView = new MainView();
            mainView.setVisible(true);
        });
    }

    /**
     * Muestra una pantalla de inicio con información del sistema
     */
    private static void mostrarPantallaInicio() {
        JOptionPane.showMessageDialog(
                null,
                "SISTEMA DE LICENCIAS DE CONDUCIR - ECUADOR\n\n" +
                        "Agencia Nacional de Tránsito\n" +
                        "Versión 1.0 (Cloud Edition)\n\n" +
                        "Desarrollado con:\n" +
                        "- Java 21\n" +
                        "- PostgreSQL (Railway Cloud)\n" + // <--- CAMBIO AQUÍ
                        "- Arquitectura MVC\n" +
                        "- iText PDF\n\n" +
                        "Conectando con la nube e iniciando sistema...",
                "Bienvenido",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    /**
     * Muestra un mensaje de error si no se puede conectar a la base de datos
     */
    private static void mostrarErrorConexion() {
        String mensaje = "ERROR DE CONEXIÓN A BASE DE DATOS\n\n" +
                "No se pudo establecer conexión con el servidor en la nube (Railway).\n\n" +
                "Verifique que:\n" +
                "1. Tenga conexión a Internet activa\n" + // <--- CAMBIO IMPORTANTE
                "2. El servicio de Railway esté operativo\n" +
                "3. El firewall no esté bloqueando el puerto 48638\n\n" +
                "Si el problema persiste, contacte al administrador.\n\n" +
                "La aplicación se cerrará.";

        JOptionPane.showMessageDialog(
                null,
                mensaje,
                "Error de Conexión",
                JOptionPane.ERROR_MESSAGE
        );

        System.exit(1);
    }
}