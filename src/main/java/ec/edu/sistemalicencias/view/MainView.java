package ec.edu.sistemalicencias.view;


import ec.edu.sistemalicencias.controller.LicenciaController;
import ec.edu.sistemalicencias.model.Usuario;
import ec.edu.sistemalicencias.model.entities.Conductor;
import ec.edu.sistemalicencias.model.entities.Licencia;
import ec.edu.sistemalicencias.model.entities.PruebaPsicometrica;
import ec.edu.sistemalicencias.model.exceptions.LicenciaException;
import ec.edu.sistemalicencias.util.PDFGenerator;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.io.File;
import java.util.List;

/**
 * Vista principal del Sistema de Licencias de Conducir.
 * Contiene el menú principal con acceso a todos los módulos del sistema.
 *
 * @author Sistema Licencias Ecuador
 * @version 1.0
 */
public class MainView extends JFrame {

    // Controlador del sistema
    private final LicenciaController controller;

    // Componentes de la interfaz
    private JPanel mainPanel;
    private JButton btnGestionConductores;
    private JButton btnValidarDocumentos;
    private JButton btnPruebasPsicometricas;
    private JButton btnEmitirLicencia;
    private JButton btnConsultarLicencias;
    private JButton btnGenerarDocumento;
    private JButton btnSalir;
    private JButton btnGestionUsuarios;


    /**
     * Constructor de la vista principal
     */
    public MainView(Usuario usuarioLogueado) {
        this.controller = new LicenciaController();

        // Inicializar componentes programáticamente
        inicializarComponentes();

        setTitle("Sistema de Licencias de Conducir - Ecuador");
        setContentPane(mainPanel);
        setSize(800, 600);
        setMinimumSize(new Dimension(800, 600));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        configurarEventos();
        configurarEstilos();
        if (usuarioLogueado != null) {
            aplicarPermisosPorRol(usuarioLogueado.getRol());
        }
    }

    /**
     * Inicializa todos los componentes de la interfaz gráfica
     */
    private void inicializarComponentes() {
        // Panel principal
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        mainPanel.setPreferredSize(new Dimension(800, 600));

        // === Panel de Encabezado ===
        JPanel panelEncabezado = new JPanel();
        panelEncabezado.setLayout(new BoxLayout(panelEncabezado, BoxLayout.Y_AXIS));
        panelEncabezado.setBorder(new EmptyBorder(0, 0, 20, 0));

        JLabel lblTitulo = new JLabel("SISTEMA DE LICENCIAS DE CONDUCIR - ECUADOR");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitulo.setForeground(new Color(40, 60, 100));
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblSubtitulo = new JLabel("Agencia Nacional de Tránsito");
        lblSubtitulo.setFont(new Font("Arial", Font.PLAIN, 14));
        lblSubtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        panelEncabezado.add(lblTitulo);
        panelEncabezado.add(Box.createVerticalStrut(5));
        panelEncabezado.add(lblSubtitulo);

        // === Panel de Módulos (Botones) ===
        JPanel panelModulos = new JPanel();
        panelModulos.setLayout(new GridLayout(4, 2, 15, 15));
        panelModulos.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                "Módulos del Sistema",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 12)
        ));
        ((EmptyBorder) BorderFactory.createEmptyBorder(10, 10, 10, 10)).getBorderInsets(panelModulos);
        panelModulos.setBorder(BorderFactory.createCompoundBorder(
                panelModulos.getBorder(),
                new EmptyBorder(10, 10, 10, 10)
        ));

        // Crear botones
        btnGestionConductores = crearBoton("Gestión de Conductores");
        btnValidarDocumentos = crearBoton("Validar Documentos");
        btnPruebasPsicometricas = crearBoton("Pruebas Psicométricas");
        btnEmitirLicencia = crearBoton("Emitir Licencia");
        btnConsultarLicencias = crearBoton("Consultar Licencias");
        btnGenerarDocumento = crearBoton("Generar Documento PDF");
        btnGestionConductores = crearBoton("Gestión de Conductores");
        btnGestionUsuarios = crearBoton("Gestión de Usuarios");
        btnSalir = crearBoton("Cerrar Sesión");

        // Agregar botones al panel
        panelModulos.add(btnGestionConductores);
        panelModulos.add(btnValidarDocumentos);
        panelModulos.add(btnPruebasPsicometricas);
        panelModulos.add(btnEmitirLicencia);
        panelModulos.add(btnConsultarLicencias);
        panelModulos.add(btnGenerarDocumento);
        panelModulos.add(btnGestionUsuarios);
        panelModulos.add(btnSalir);
        //panelModulos.add(new JLabel()); // Celda vacía para balancear

        // === Panel de Pie de Página ===
        JPanel panelPie = new JPanel();
        panelPie.setBorder(new EmptyBorder(10, 0, 0, 0));

        JLabel lblVersion = new JLabel("Sistema de Licencias v1.0 - Desarrollado con Java y MySQL");
        lblVersion.setFont(new Font("Arial", Font.PLAIN, 10));
        lblVersion.setForeground(Color.GRAY);
        panelPie.add(lblVersion);

        // Agregar paneles al panel principal
        mainPanel.add(panelEncabezado, BorderLayout.NORTH);
        mainPanel.add(panelModulos, BorderLayout.CENTER);
        mainPanel.add(panelPie, BorderLayout.SOUTH);
    }

    /**
     * Crea un botón con estilo uniforme
     *
     * @param texto Texto del botón
     * @return JButton configurado
     */
    private JButton crearBoton(String texto) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Arial", Font.PLAIN, 14));
        boton.setPreferredSize(new Dimension(150, 80));
        boton.setFocusPainted(false);
        return boton;
    }

    /**
     * Configura los eventos de los botones del menú principal
     */
    private void configurarEventos() {
        // Botón Gestión de Conductores
        btnGestionConductores.addActionListener(e -> abrirGestionConductores());

        // Botón Validar Documentos
        btnValidarDocumentos.addActionListener(e -> abrirValidarDocumentos());

        // Botón Pruebas Psicométricas
        btnPruebasPsicometricas.addActionListener(e -> abrirPruebasPsicometricas());

        // Botón Emitir Licencia
        btnEmitirLicencia.addActionListener(e -> abrirEmitirLicencia());

        // Botón Consultar Licencias
        btnConsultarLicencias.addActionListener(e -> abrirConsultarLicencias());

        // Botón Generar Documento PDF
        btnGenerarDocumento.addActionListener(e -> generarDocumentoPDF());

        // Botón Gestión de Usuarios
        btnGestionUsuarios.addActionListener(e -> abrirGestionUsuarios());

        // Botón Salir
        btnSalir.addActionListener(e -> cerrarSesion());
    }

    // Cierra la sesión del usuario y vuelve al login
    private void cerrarSesion() {
        int opcion = JOptionPane.showConfirmDialog(
                this,
                "¿Desea cerrar sesión?",
                "Cerrar sesión",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (opcion == JOptionPane.YES_OPTION) {
            dispose(); // cerrar la ventana principal
            // volver a mostrar el login
            ec.edu.sistemalicencias.Main.mostrarLogin();
        }
    }


    /**
     * Configura los estilos adicionales de los componentes
     */
    private void configurarEstilos() {
        // Estilo del botón de salir (color rojo)
        btnSalir.setBackground(new Color(220, 53, 69));
        btnSalir.setForeground(Color.WHITE);
        btnSalir.setOpaque(true);
        btnSalir.setBorderPainted(false);

        // Cursor de mano para todos los botones
        btnGestionConductores.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnValidarDocumentos.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnPruebasPsicometricas.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnEmitirLicencia.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnConsultarLicencias.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnGenerarDocumento.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSalir.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }


    /**
     * Abre la ventana de Gestión de Conductores
     */
    private void abrirGestionConductores() {
        try {
            GestionConductoresView gestionView = new GestionConductoresView(controller);
            gestionView.setVisible(true);
        } catch (Exception ex) {
            mostrarError("Error al abrir Gestión de Conductores: " + ex.getMessage());
        }
    }

    private void abrirGestionUsuarios() {
        try {
            GestionUsuariosView usuariosView = new GestionUsuariosView();
            usuariosView.setVisible(true);
        } catch (Exception ex) {
            mostrarError("Error al abrir Gestión de Usuarios: " + ex.getMessage());
        }
    }

    public void aplicarPermisosPorRol(String rol) {
        System.out.println("DEBUG: El rol que llegó de la BD es: '" + rol + "'");
        if (rol == null) return;
        boolean esAnalista = rol.trim().equalsIgnoreCase("Analista");
        if (btnGestionUsuarios != null) {
            btnGestionUsuarios.setVisible(!esAnalista);
            btnGestionUsuarios.setEnabled(!esAnalista);
        } // bloquea acceso al CRUD
    }


    /**
     * Abre la ventana de Validar Documentos
     */
    private void abrirValidarDocumentos() {
        try {
            ValidarDocumentosView validarView = new ValidarDocumentosView(controller);
            validarView.setVisible(true);
        } catch (Exception ex) {
            mostrarError("Error al abrir Validar Documentos: " + ex.getMessage());
        }
    }

    /**
     * Abre la ventana de Pruebas Psicométricas
     */
    private void abrirPruebasPsicometricas() {
        try {
            PruebasPsicometricasView pruebasView = new PruebasPsicometricasView(controller);
            pruebasView.setVisible(true);
        } catch (Exception ex) {
            mostrarError("Error al abrir Pruebas Psicométricas: " + ex.getMessage());
        }
    }

    /**
     * Abre la ventana de Emitir Licencia
     */
    private void abrirEmitirLicencia() {
        try {
            EmitirLicenciaView emitirView = new EmitirLicenciaView(controller);
            emitirView.setVisible(true);
        } catch (Exception ex) {
            mostrarError("Error al abrir Emitir Licencia: " + ex.getMessage());
        }
    }

    /**
     * Abre la ventana de Consultar Licencias
     */
    private void abrirConsultarLicencias() {
        try {
            ConsultarLicenciasView consultarView = new ConsultarLicenciasView(controller);
            consultarView.setVisible(true);
        } catch (Exception ex) {
            mostrarError("Error al abrir Consultar Licencias: " + ex.getMessage());
        }
    }

    /**
     * Genera un documento PDF de una licencia seleccionada
     */
    private void generarDocumentoPDF() {
        try {
            // Solicitar cédula del conductor
            String cedula = JOptionPane.showInputDialog(
                    this,
                    "Ingrese la cédula del conductor para generar el PDF:",
                    "Generar Documento PDF",
                    JOptionPane.QUESTION_MESSAGE
            );

            if (cedula == null || cedula.trim().isEmpty()) {
                return; // Usuario canceló
            }

            // Buscar conductor
            Conductor conductor = controller.buscarConductorPorCedula(cedula.trim());
            if (conductor == null) {
                mostrarError("No se encontró un conductor con la cédula: " + cedula);
                return;
            }

            // Buscar licencias del conductor
            List<Licencia> licencias = controller.obtenerLicenciasConductor(conductor.getId());
            if (licencias == null || licencias.isEmpty()) {
                mostrarError("El conductor no tiene licencias emitidas.");
                return;
            }

            // Si hay múltiples licencias, usar la más reciente
            Licencia licencia = licencias.get(0);

            // Obtener última prueba aprobada (puede ser null)
            PruebaPsicometrica prueba = controller.obtenerUltimaPruebaAprobada(conductor.getId());

            // Seleccionar ubicación para guardar
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Guardar Licencia PDF");
            fileChooser.setSelectedFile(new File("Licencia_" + conductor.getCedula() + ".pdf"));

            int resultado = fileChooser.showSaveDialog(this);
            if (resultado == JFileChooser.APPROVE_OPTION) {
                String rutaArchivo = fileChooser.getSelectedFile().getAbsolutePath();

                // Asegurar extensión .pdf
                if (!rutaArchivo.toLowerCase().endsWith(".pdf")) {
                    rutaArchivo += ".pdf";
                }

                // Generar PDF
                PDFGenerator.generarLicenciaPDF(licencia, conductor, prueba, rutaArchivo);

                mostrarExito("Documento PDF generado exitosamente:\n" + rutaArchivo);

                // Preguntar si desea abrir el archivo
                int abrir = JOptionPane.showConfirmDialog(
                        this,
                        "¿Desea abrir el documento generado?",
                        "Abrir PDF",
                        JOptionPane.YES_NO_OPTION
                );

                if (abrir == JOptionPane.YES_OPTION) {
                    Desktop.getDesktop().open(new File(rutaArchivo));
                }
            }

        } catch (LicenciaException ex) {
            mostrarError("Error al generar PDF: " + ex.getMessage());
        } catch (Exception ex) {
            mostrarError("Error inesperado: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    /**
     * Cierra la aplicación con confirmación
     */
    private void salirAplicacion() {
        int opcion = JOptionPane.showConfirmDialog(
                this,
                "¿Está seguro que desea salir del sistema?",
                "Confirmar Salida",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (opcion == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    //public void aplicarPermisosPorRol(String rol) {
    //boolean esAnalista = rol != null && rol.equalsIgnoreCase("Analista");

    // IMPORTANTE:
    // En tu MainView actual NO hay módulo/botón de "Usuarios".
    // Si luego agregas un botón como btnGestionUsuarios, entonces aquí lo ocultas:
    // btnGestionUsuarios.setVisible(!esAnalista);

    // Si el profesor acepta que Analista SOLO consulta:
    // btnEmitirLicencia.setVisible(!esAnalista);
    // btnGestionConductores.setVisible(!esAnalista);


    /**
     * Muestra un mensaje de error
     *
     * @param mensaje Mensaje a mostrar
     */
    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(
                this,
                mensaje,
                "Error",
                JOptionPane.ERROR_MESSAGE
        );
    }

    /**
     * Muestra un mensaje de éxito
     *
     * @param mensaje Mensaje a mostrar
     */
    private void mostrarExito(String mensaje) {
        JOptionPane.showMessageDialog(
                this,
                mensaje,
                "Éxito",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

}