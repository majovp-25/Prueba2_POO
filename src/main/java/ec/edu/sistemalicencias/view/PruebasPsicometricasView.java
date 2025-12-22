package ec.edu.sistemalicencias.view;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import ec.edu.sistemalicencias.controller.LicenciaController;
import ec.edu.sistemalicencias.model.entities.Conductor;
import ec.edu.sistemalicencias.model.entities.PruebaPsicometrica;
import ec.edu.sistemalicencias.model.exceptions.LicenciaException;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.Locale;

public class PruebasPsicometricasView extends JFrame {
    private final LicenciaController controller;
    private Conductor conductorActual;

    private JPanel panelPrincipal;
    private JPanel panelBusqueda;
    private JTextField txtCedula;
    private JButton btnBuscar;

    private JScrollPane scrollInfo;
    private JTextArea txtInfoConductor;

    private JPanel panelNotas;
    private JTextField txtNotaReaccion;
    private JTextField txtNotaAtencion;
    private JTextField txtNotaCoordinacion;
    private JTextField txtNotaPercepcion;
    private JTextField txtNotaPsicologica;
    private JButton btnCalcular;

    private JPanel panelResultados;
    private JLabel lblPromedio;
    private JLabel lblResultado;
    private JTextArea txtObservaciones;

    private JPanel panelBotones;
    private JButton btnGuardar;
    private JButton btnLimpiar;
    private JButton btnCerrar;

    public PruebasPsicometricasView(LicenciaController controller) {
        this.controller = controller;

        setTitle("Registro de Pruebas Psicométricas");
        setContentPane(panelPrincipal);
        setSize(750, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        configurarEventos();
    }

    private void configurarEventos() {
        btnBuscar.addActionListener(e -> buscarConductor());
        btnCalcular.addActionListener(e -> calcularPromedioVisual());
        btnGuardar.addActionListener(e -> guardarPrueba());
        btnLimpiar.addActionListener(e -> limpiarFormulario());
        btnCerrar.addActionListener(e -> dispose());
    }

    private void buscarConductor() {
        try {
            String cedula = txtCedula.getText().trim();
            if (cedula.isEmpty()) {
                mostrarMensaje("Ingrese una cédula");
                return;
            }

            conductorActual = controller.buscarConductorPorCedula(cedula);

            if (conductorActual != null) {
                txtInfoConductor.setText(
                        "CONDUCTOR ENCONTRADO:\n" +
                                "Nombre: " + conductorActual.getNombreCompleto() + "\n" +
                                "Cédula: " + conductorActual.getCedula() + "\n" +
                                "Edad: " + conductorActual.calcularEdad() + " años"
                );
                txtInfoConductor.setForeground(new Color(0, 100, 0)); // Verde
                habilitarCampos(true);
            } else {
                txtInfoConductor.setText("Conductor no encontrado con cédula: " + cedula);
                txtInfoConductor.setForeground(Color.RED);
                habilitarCampos(false);
            }
        } catch (LicenciaException e) {
            mostrarMensaje("Error: " + e.getMessage());
        }
    }

    private void calcularPromedioVisual() {
        try {
            double promedio = obtenerPromedio();
            lblPromedio.setText(String.format("Promedio: %.2f", promedio));

            if (promedio >= 70) {
                lblResultado.setText("Estado: APROBADO");
                lblResultado.setForeground(new Color(0, 150, 0));
            } else {
                lblResultado.setText("Estado: REPROBADO");
                lblResultado.setForeground(Color.RED);
            }
        } catch (NumberFormatException e) {
            mostrarMensaje("Ingrese valores numéricos válidos (0-100) en todas las notas.");
        }
    }

    private double obtenerPromedio() {
        double n1 = Double.parseDouble(txtNotaReaccion.getText());
        double n2 = Double.parseDouble(txtNotaAtencion.getText());
        double n3 = Double.parseDouble(txtNotaCoordinacion.getText());
        double n4 = Double.parseDouble(txtNotaPercepcion.getText());
        double n5 = Double.parseDouble(txtNotaPsicologica.getText());
        return (n1 + n2 + n3 + n4 + n5) / 5.0;
    }

    private void guardarPrueba() {
        if (conductorActual == null) {
            mostrarMensaje("Error: Debe buscar un conductor válido antes de guardar los resultados.");
            return;
        }

        try {
            PruebaPsicometrica prueba = new PruebaPsicometrica();
            prueba.setConductorId(conductorActual.getId());
            prueba.setFechaRealizacion(LocalDateTime.now());

            prueba.setNotaReaccion(Double.parseDouble(txtNotaReaccion.getText()));
            prueba.setNotaAtencion(Double.parseDouble(txtNotaAtencion.getText()));
            prueba.setNotaCoordinacion(Double.parseDouble(txtNotaCoordinacion.getText()));
            prueba.setNotaPercepcion(Double.parseDouble(txtNotaPercepcion.getText()));
            prueba.setNotaPsicologica(Double.parseDouble(txtNotaPsicologica.getText()));

            prueba.setObservaciones(txtObservaciones.getText());

            controller.registrarPruebaPsicometrica(prueba);

            mostrarMensaje("¡Prueba registrada exitosamente!");
            limpiarFormulario();
            dispose();

        } catch (NumberFormatException e) {
            mostrarMensaje("Verifique que todas las notas sean números válidos.");
        } catch (LicenciaException e) {
            mostrarMensaje("Error al guardar: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            mostrarMensaje("Ocurrió un error inesperado: " + e.getMessage());
        }
    }

    private void limpiarFormulario() {
        conductorActual = null;
        txtCedula.setText("");
        txtInfoConductor.setText("");
        txtNotaReaccion.setText("");
        txtNotaAtencion.setText("");
        txtNotaCoordinacion.setText("");
        txtNotaPercepcion.setText("");
        txtNotaPsicologica.setText("");
        txtObservaciones.setText("");
        lblPromedio.setText("Promedio: --");
        lblResultado.setText("Estado: --");
        habilitarCampos(false);
    }

    private void habilitarCampos(boolean activo) {
        txtNotaReaccion.setEnabled(activo);
        txtNotaAtencion.setEnabled(activo);
        txtNotaCoordinacion.setEnabled(activo);
        txtNotaPercepcion.setEnabled(activo);
        txtNotaPsicologica.setEnabled(activo);
        txtObservaciones.setEnabled(activo);
        btnCalcular.setEnabled(activo);
        btnGuardar.setEnabled(activo);
        btnLimpiar.setEnabled(activo);
    }

    private void mostrarMensaje(String msg) {
        JOptionPane.showMessageDialog(this, msg);
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new GridLayoutManager(5, 1, new Insets(15, 15, 15, 15), -1, -1));
        panelPrincipal.setMinimumSize(new Dimension(700, 600));
        panelPrincipal.setPreferredSize(new Dimension(700, 600));
        panelBusqueda = new JPanel();
        panelBusqueda.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        panelPrincipal.add(panelBusqueda, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("Cédula:");
        panelBusqueda.add(label1);
        txtCedula = new JTextField();
        txtCedula.setColumns(15);
        panelBusqueda.add(txtCedula);
        btnBuscar = new JButton();
        btnBuscar.setText("Buscar");
        panelBusqueda.add(btnBuscar);
        scrollInfo = new JScrollPane();
        panelPrincipal.add(scrollInfo, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        scrollInfo.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "Información del Conductor", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        txtInfoConductor = new JTextArea();
        txtInfoConductor.setEditable(false);
        txtInfoConductor.setRows(5);
        scrollInfo.setViewportView(txtInfoConductor);
        panelNotas = new JPanel();
        panelNotas.setLayout(new GridLayoutManager(3, 4, new Insets(10, 10, 10, 10), -1, -1));
        panelPrincipal.add(panelNotas, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panelNotas.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "Calificaciones Prueba Psicométrica", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        final JLabel label2 = new JLabel();
        label2.setText("Reacción (0-100):");
        panelNotas.add(label2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txtNotaReaccion = new JTextField();
        txtNotaReaccion.setText("");
        panelNotas.add(txtNotaReaccion, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(80, -1), null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("Atención (0-100):");
        panelNotas.add(label3, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txtNotaAtencion = new JTextField();
        panelNotas.add(txtNotaAtencion, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(80, -1), null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setText("Coordinación (0-100):");
        panelNotas.add(label4, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txtNotaCoordinacion = new JTextField();
        panelNotas.add(txtNotaCoordinacion, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(80, -1), null, 0, false));
        final JLabel label5 = new JLabel();
        label5.setText("Percepción (0-100):");
        panelNotas.add(label5, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txtNotaPercepcion = new JTextField();
        panelNotas.add(txtNotaPercepcion, new GridConstraints(1, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(80, -1), null, 0, false));
        final JLabel label6 = new JLabel();
        label6.setText("Psicológica (0-100):");
        panelNotas.add(label6, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txtNotaPsicologica = new JTextField();
        panelNotas.add(txtNotaPsicologica, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(80, -1), null, 0, false));
        btnCalcular = new JButton();
        btnCalcular.setText("Calcular Promedio");
        panelNotas.add(btnCalcular, new GridConstraints(2, 2, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        panelResultados = new JPanel();
        panelResultados.setLayout(new GridLayoutManager(3, 1, new Insets(10, 10, 10, 10), -1, -1));
        panelPrincipal.add(panelResultados, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panelResultados.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "Resultados", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        lblPromedio = new JLabel();
        Font lblPromedioFont = this.$$$getFont$$$(null, Font.BOLD, 14, lblPromedio.getFont());
        if (lblPromedioFont != null) lblPromedio.setFont(lblPromedioFont);
        lblPromedio.setText("Promedio: --");
        panelResultados.add(lblPromedio, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        lblResultado = new JLabel();
        Font lblResultadoFont = this.$$$getFont$$$(null, Font.BOLD, 14, lblResultado.getFont());
        if (lblResultadoFont != null) lblResultado.setFont(lblResultadoFont);
        lblResultado.setText("Estado: --");
        panelResultados.add(lblResultado, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        panelResultados.add(scrollPane1, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        scrollPane1.setBorder(BorderFactory.createTitledBorder(null, "Observaciones", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        txtObservaciones = new JTextArea();
        txtObservaciones.setRows(3);
        scrollPane1.setViewportView(txtObservaciones);
        panelBotones = new JPanel();
        panelBotones.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        panelPrincipal.add(panelBotones, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnGuardar = new JButton();
        btnGuardar.setText("Guardar Resultados");
        panelBotones.add(btnGuardar);
        btnLimpiar = new JButton();
        btnLimpiar.setText("Limpiar");
        panelBotones.add(btnLimpiar);
        btnCerrar = new JButton();
        btnCerrar.setText("Cerrar");
        panelBotones.add(btnCerrar);
    }

    /**
     * @noinspection ALL
     */
    private Font $$$getFont$$$(String fontName, int style, int size, Font currentFont) {
        if (currentFont == null) return null;
        String resultName;
        if (fontName == null) {
            resultName = currentFont.getName();
        } else {
            Font testFont = new Font(fontName, Font.PLAIN, 10);
            if (testFont.canDisplay('a') && testFont.canDisplay('1')) {
                resultName = fontName;
            } else {
                resultName = currentFont.getName();
            }
        }
        Font font = new Font(resultName, style >= 0 ? style : currentFont.getStyle(), size >= 0 ? size : currentFont.getSize());
        boolean isMac = System.getProperty("os.name", "").toLowerCase(Locale.ENGLISH).startsWith("mac");
        Font fontWithFallback = isMac ? new Font(font.getFamily(), font.getStyle(), font.getSize()) : new StyleContext().getFont(font.getFamily(), font.getStyle(), font.getSize());
        return fontWithFallback instanceof FontUIResource ? fontWithFallback : new FontUIResource(fontWithFallback);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panelPrincipal;
    }

}