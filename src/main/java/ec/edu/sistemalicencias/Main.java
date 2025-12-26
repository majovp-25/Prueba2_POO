package ec.edu.sistemalicencias;

import ec.edu.sistemalicencias.config.DatabaseConfig;
import ec.edu.sistemalicencias.view.MainView;
import ec.edu.sistemalicencias.model.Usuario; // Importamos Usuario para no escribir la ruta larga
import ec.edu.sistemalicencias.dao.UsuarioDAO; // Importamos el DAO

import javax.swing.*;
import java.util.ArrayList; // Necesario para crear la lista
import java.util.List;      // Necesario para el tipo de dato List

/**
 * Clase principal del Sistema de Licencias de Conducir del Ecuador.
 * Punto de entrada de la aplicación.
 */
public class Main {

    public static void main(String[] args) {
        // 1. Configurar Look and Feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("No se pudo establecer el Look and Feel: " + e.getMessage());
        }

        // 2. Instanciamos la configuración
        DatabaseConfig dbConfig = DatabaseConfig.getInstance();

        // ==========================================
        // 🧪 ZONA DE PRUEBAS DEL LOGIN
        // ==========================================
        System.out.println("\n🛠️ --- INICIANDO PRUEBA DE BACKEND (RAILWAY) ---");

        UsuarioDAO dao = new UsuarioDAO();

        // TEST 1: Probamos con el ADMIN
        System.out.println("👉 Intentando login con 'admin'...");
        Usuario u1 = dao.login("admin", "1234"); 

        if (u1 != null) {
            System.out.println("✅ ¡ÉXITO! Usuario encontrado: " + u1.getUsername());
            System.out.println("🔹 Rol detectado: " + u1.getRol());
        } else {
            System.out.println("❌ ERROR: No se pudo conectar o usuario incorrecto.");
        }

        // TEST 2: Probamos con datos FALSOS
        System.out.println("\n👉 Intentando login con 'hacker'...");
        Usuario u2 = dao.login("hacker", "nadie");

        if (u2 == null) {
            System.out.println("✅ ¡CORRECTO! El sistema rechazó al intruso.");
        } else {
            System.out.println("⚠️ ALERTA: El sistema dejó pasar a un usuario falso.");
        }
        
        System.out.println("----------------------------------------------");

        // ==========================================
        // 📄 ZONA DE PRUEBAS DEL PDF (NUEVO)
        // ==========================================
        System.out.println("\n📄 --- INICIANDO PRUEBA DE GENERACIÓN PDF ---");
        
        // 1. Creamos una lista simulada de usuarios (porque aún no tenemos un método que traiga todos)
        List<Usuario> listaParaReporte = new ArrayList<>();
        
        // Agregamos usuarios a mano a la lista
        listaParaReporte.add(new Usuario(1, "admin", "1234", "Administrador"));
        listaParaReporte.add(new Usuario(2, "supervisor_quito", "1234", "Supervisor"));
        listaParaReporte.add(new Usuario(3, "cajero_guayaquil", "1234", "Cajero"));

        // 2. Instanciamos la clase GeneradorPDF
        // (Asegúrate de haber creado el archivo GeneradorPDF.java que te pasé antes)
        GeneradorPDF generador = new GeneradorPDF();

        // 3. Generamos el reporte
        String nombreArchivo = "ReporteUsuarios.pdf";
        generador.generarReporte(listaParaReporte, nombreArchivo);

        System.out.println("----------------------------------------------\n");
    }
}