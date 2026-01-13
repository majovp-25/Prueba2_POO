package ec.edu.sistemalicencias.view;

import com.formdev.flatlaf.FlatClientProperties;
import ec.edu.sistemalicencias.controller.UsuarioController;
import ec.edu.sistemalicencias.model.Usuario;
import ec.edu.sistemalicencias.util.GeneradorReporteUsuarios;
import org.mindrot.jbcrypt.BCrypt;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.util.List;

public class GestionUsuariosView extends JFrame {

    private final UsuarioController controller = new UsuarioController();
    private String usuarioLogueadoUsername;
    private JTable tabla;
    private DefaultTableModel modelo;

    // Campos
    private JTextField txtCedula, txtPrimerNombre, txtSegundoNombre, txtPrimerApellido, txtSegundoApellido;
    private JTextField txtTelefono, txtCorreo, txtId, txtUsername;
    private JPasswordField txtPassword;
    private JComboBox<String> cboRol;
    private JCheckBox chkVerPass;
    private JLabel lblEstadoActual;

    private JButton btnEstado;

    private Usuario usuarioSeleccionado = null;

    // PALETA DE COLORES "ESTILO ANT" (Basado en tu captura)
    private final Color ANT_RED = new Color(220, 53, 69);      // Rojo intenso (Botones de salida/borrar)
    private final Color ANT_WHITE_BG = Color.WHITE;            // Fondo de botones normales
    private final Color ANT_BORDER = new Color(200, 200, 200); // Borde sutil
    private final Color ANT_TEXT = new Color(50, 50, 50);      // Texto oscuro

    public GestionUsuariosView(String usernameLogueado) {
        this.usuarioLogueadoUsername = usernameLogueado;
        setTitle("Administración de Usuarios - Sistema Nacional");
        setSize(1250, 750);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initUI();
        cargarTabla();
    }

    private void initUI() {
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(new Color(245, 247, 250)); // Fondo gris muy suave

        // --- 1. HEADER ---
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        JLabel lblTitulo = new JLabel("Módulo de Gestión de Usuarios");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitulo.setForeground(new Color(40, 55, 70)); // Azul oscuro casi negro
        
        JLabel lblSubtitulo = new JLabel("Sesión activa: " + usuarioLogueadoUsername);
        lblSubtitulo.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        lblSubtitulo.setForeground(Color.GRAY);

        headerPanel.add(lblTitulo, BorderLayout.WEST);
        headerPanel.add(lblSubtitulo, BorderLayout.EAST);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // --- 2. TABLA (Izquierda) ---
        modelo = new DefaultTableModel(new Object[]{"ID", "Cédula", "Nombres", "Rol", "Estado"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        tabla = new JTable(modelo);
        tabla.setRowHeight(32);
        tabla.setShowVerticalLines(false);
        tabla.setIntercellSpacing(new Dimension(0, 0));
        tabla.setSelectionBackground(new Color(230, 240, 255));
        tabla.setSelectionForeground(Color.BLACK);
        tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tabla.getTableHeader().setBackground(Color.WHITE);
        tabla.getTableHeader().setPreferredSize(new Dimension(0, 40));

        // Renderizado de Estado (Texto Coloreado)
        tabla.getColumnModel().getColumn(4).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel c = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setHorizontalAlignment(SwingConstants.CENTER);
                String estado = (String) value;
                if ("ACTIVO".equals(estado)) {
                    c.setForeground(new Color(40, 167, 69)); // Verde
                    c.setFont(c.getFont().deriveFont(Font.BOLD));
                } else {
                    c.setForeground(ANT_RED); // Rojo
                }
                return c;
            }
        });

        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) cargarFormularioDesdeTabla();
        });

        JScrollPane scrollTabla = new JScrollPane(tabla);
        scrollTabla.setBorder(BorderFactory.createLineBorder(ANT_BORDER));
        scrollTabla.getViewport().setBackground(Color.WHITE);
        
        // --- 3. FORMULARIO (Derecha) ---
        JPanel rightPanel = new JPanel(new BorderLayout(0, 15));
        rightPanel.setOpaque(false);
        
        // Tarjeta del Formulario
        JPanel cardForm = new JPanel(new GridBagLayout());
        cardForm.setBackground(Color.WHITE);
        cardForm.setBorder(new EmptyBorder(20, 25, 20, 25));
        // Efecto tarjeta: borde gris muy fino y redondeado
        cardForm.putClientProperty(FlatClientProperties.STYLE, "arc: 12; border: 1,1,1,1, #dcdcdc");

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        // === CAMPOS DEL FORMULARIO ===
        addSectionTitle(cardForm, "Información Personal", 0, gbc);

        // Fila 1
        addField(cardForm, "Cédula *:", txtCedula = createStyledField(), 0, 1, gbc, 0.5);
        addField(cardForm, "Teléfono:", txtTelefono = createStyledField(), 2, 1, gbc, 0.5);

        // Fila 2 (Simetría)
        addField(cardForm, "Primer Nombre *:", txtPrimerNombre = createStyledField(), 0, 2, gbc, 0.5);
        addField(cardForm, "Segundo Nombre:", txtSegundoNombre = createStyledField(), 2, 2, gbc, 0.5);

        // Fila 3 (Simetría)
        addField(cardForm, "Primer Apellido *:", txtPrimerApellido = createStyledField(), 0, 3, gbc, 0.5);
        addField(cardForm, "Segundo Apellido:", txtSegundoApellido = createStyledField(), 2, 3, gbc, 0.5);

        // Fila 4
        gbc.gridy = 4; gbc.gridx = 0; gbc.weightx = 0; cardForm.add(new JLabel("Correo:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 3; txtCorreo = createStyledField(); cardForm.add(txtCorreo, gbc);
        gbc.gridwidth = 1; 

        addSectionTitle(cardForm, "Cuenta y Seguridad", 5, gbc);

        // Fila 6
        gbc.gridy = 6; gbc.gridx = 0; gbc.weightx = 0; cardForm.add(new JLabel("ID:"), gbc);
        gbc.gridx = 1; txtId = createStyledField(); txtId.setEditable(false); txtId.setBackground(new Color(245, 245, 245)); cardForm.add(txtId, gbc);
        
        gbc.gridx = 2; cardForm.add(new JLabel("Estado:"), gbc);
        gbc.gridx = 3; lblEstadoActual = new JLabel("---"); lblEstadoActual.setFont(new Font("Segoe UI", Font.BOLD, 13)); cardForm.add(lblEstadoActual, gbc);

        // Fila 7
        addField(cardForm, "Rol:", cboRol = new JComboBox<>(new String[]{"Administrador", "Analista"}), 0, 7, gbc, 0.5);
        
        // Fila 8
        gbc.gridy = 8; gbc.gridx = 0; cardForm.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 3; txtUsername = createStyledField(); txtUsername.setEditable(false); 
        txtUsername.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Generado automáticamente");
        cardForm.add(txtUsername, gbc);
        gbc.gridwidth = 1;

        // Fila 9
        gbc.gridy = 9; gbc.gridx = 0; cardForm.add(new JLabel("Contraseña:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2; txtPassword = new JPasswordField(); styleComponent(txtPassword);
        txtPassword.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Vacío para mantener actual");
        cardForm.add(txtPassword, gbc);
        
        gbc.gridx = 3; gbc.gridwidth = 1; chkVerPass = new JCheckBox("Ver");
        chkVerPass.addActionListener(e -> txtPassword.setEchoChar(chkVerPass.isSelected() ? (char)0 : '•'));
        cardForm.add(chkVerPass, gbc);

        // --- 4. BOTONERA (ESTILO LIMPIO - TIPO MENÚ) ---
        // Usamos GridLayout con espacio para que se vean como "Tarjetas" o botones grandes del menú
        JPanel actionsPanel = new JPanel(new GridLayout(2, 4, 10, 10)); // 2 filas, 4 columnas
        actionsPanel.setOpaque(false);
        
        // --- FILA 1: OPERACIONES ---
        JButton btnGuardar = createMenuButton("Guardar / Nuevo");
        JButton btnEditar = createMenuButton("Actualizar Datos");
        btnEstado = createMenuButton("Desactivar"); // Cambia dinámicamente
        JButton btnLimpiar = createMenuButton("Limpiar Campos");
        
        // --- FILA 2: EXTRAS Y SALIDA ---
        JButton btnPdf = createMenuButton("Generar PDF");
        JButton btnResetPass = createMenuButton("Resetear Clave");
        
        // BOTONES ROJOS (PELIGRO/SALIDA)
        JButton btnEliminarDef = createRedButton("Eliminar Definitivo");
        JButton btnLogout = createRedButton("Cerrar Sesión");

        // Añadir en orden
        actionsPanel.add(btnGuardar);
        actionsPanel.add(btnEditar);
        actionsPanel.add(btnEstado);
        actionsPanel.add(btnLimpiar);
        
        actionsPanel.add(btnPdf);
        actionsPanel.add(btnResetPass);
        actionsPanel.add(btnEliminarDef); // Rojo
        actionsPanel.add(btnLogout);      // Rojo

        // Ensamblaje derecho
        rightPanel.add(cardForm, BorderLayout.CENTER);
        rightPanel.add(actionsPanel, BorderLayout.SOUTH);

        // SPLIT
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scrollTabla, rightPanel);
        split.setResizeWeight(0.45);
        split.setDividerSize(5);
        mainPanel.add(split, BorderLayout.CENTER);

        setContentPane(mainPanel);

        // --- LISTENERS ---
        btnGuardar.addActionListener(e -> guardarUsuario());
        btnEditar.addActionListener(e -> editarUsuario());
        btnEstado.addActionListener(e -> cambiarEstadoLogico());
        btnEliminarDef.addActionListener(e -> eliminarFisico());
        btnResetPass.addActionListener(e -> resetearContrasenaDefault());
        btnLimpiar.addActionListener(e -> limpiarFormulario());
        btnPdf.addActionListener(e -> generarPDF());
        btnLogout.addActionListener(e -> {
            if(JOptionPane.showConfirmDialog(this, "¿Cerrar sesión?", "Salir", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                this.dispose();
                new LoginView().setVisible(true);
            }
        });
    }

    // ================= DISEÑO DE BOTONES (ESTILO MENÚ) =================
    
    // Botón BLANCO (Como los módulos del menú)
    private JButton createMenuButton(String text) {
        JButton b = new JButton(text);
        b.setBackground(ANT_WHITE_BG);
        b.setForeground(ANT_TEXT);
        b.setFont(new Font("Segoe UI", Font.BOLD, 12));
        b.setFocusPainted(false);
        // Borde gris suave y esquinas redondeadas
        b.setBorder(BorderFactory.createLineBorder(ANT_BORDER, 1));
        b.putClientProperty(FlatClientProperties.STYLE, "arc: 10; margin: 5,10,5,10");
        
        // Efecto hover simple (opcional, Swing lo maneja básico)
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return b;
    }

    // Botón ROJO (Como cerrar sesión)
    private JButton createRedButton(String text) {
        JButton b = new JButton(text);
        b.setBackground(ANT_RED);
        b.setForeground(Color.WHITE);
        b.setFont(new Font("Segoe UI", Font.BOLD, 12));
        b.setFocusPainted(false);
        b.setBorderPainted(false); // Sin borde, todo color
        b.putClientProperty(FlatClientProperties.STYLE, "arc: 10; margin: 5,10,5,10");
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return b;
    }

    private void styleComponent(JComponent c) {
        c.putClientProperty(FlatClientProperties.STYLE, "arc: 8; padding: 5,5,5,5");
        c.setPreferredSize(new Dimension(0, 30));
    }

    private JTextField createStyledField() {
        JTextField t = new JTextField();
        styleComponent(t);
        return t;
    }

    private void addField(JPanel p, String label, JComponent comp, int x, int y, GridBagConstraints gbc, double weight) {
        gbc.gridx = x; gbc.gridy = y; 
        gbc.weightx = 0; gbc.gridwidth = 1;
        p.add(new JLabel(label), gbc);
        
        gbc.gridx = x + 1; 
        gbc.weightx = weight;
        p.add(comp, gbc);
    }

    private void addSectionTitle(JPanel p, String title, int y, GridBagConstraints gbc) {
        GridBagConstraints c = (GridBagConstraints) gbc.clone();
        c.gridx = 0; c.gridy = y; c.gridwidth = 4;
        c.insets = new Insets(15, 5, 5, 5);
        JLabel l = new JLabel(title);
        l.setFont(new Font("Segoe UI", Font.BOLD, 14));
        l.setForeground(new Color(0, 80, 160)); // Azul corporativo suave
        l.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(230, 230, 230)));
        p.add(l, c);
    }

    // ================= LÓGICA (IGUAL) =================

    private void cargarTabla() {
        modelo.setRowCount(0);
        List<Usuario> lista = controller.listar(); 
        for (Usuario u : lista) {
            String nombreCompleto = u.getPrimerNombre() + " " + u.getPrimerApellido();
            modelo.addRow(new Object[]{u.getId(), u.getCedula(), nombreCompleto, u.getRol(), u.isActivo() ? "ACTIVO" : "INACTIVO"});
        }
    }

    private void cargarFormularioDesdeTabla() {
        int row = tabla.getSelectedRow();
        if (row == -1) return;
        int id = (int) modelo.getValueAt(row, 0);
        usuarioSeleccionado = controller.listar().stream().filter(x -> x.getId() == id).findFirst().orElse(null);
        if (usuarioSeleccionado == null) return;

        txtId.setText(String.valueOf(usuarioSeleccionado.getId()));
        txtCedula.setText(usuarioSeleccionado.getCedula());
        txtPrimerNombre.setText(usuarioSeleccionado.getPrimerNombre());
        txtSegundoNombre.setText(usuarioSeleccionado.getSegundoNombre());
        txtPrimerApellido.setText(usuarioSeleccionado.getPrimerApellido());
        txtSegundoApellido.setText(usuarioSeleccionado.getSegundoApellido());
        txtTelefono.setText(usuarioSeleccionado.getTelefono());
        txtCorreo.setText(usuarioSeleccionado.getEmail());
        cboRol.setSelectedItem(usuarioSeleccionado.getRol());
        txtUsername.setText(usuarioSeleccionado.getUsername());
        
        txtPassword.setText(usuarioSeleccionado.getPassword());
        txtPassword.setEditable(false);
        txtPassword.setBackground(new Color(245, 245, 245));

        if (usuarioSeleccionado.isActivo()) {
            lblEstadoActual.setText("ACTIVO");
            lblEstadoActual.setForeground(new Color(40, 167, 69));
            btnEstado.setText("Desactivar");
        } else {
            lblEstadoActual.setText("INACTIVO");
            lblEstadoActual.setForeground(ANT_RED);
            btnEstado.setText("Activar");
        }
    }

    private void guardarUsuario() {
        if (txtCedula.getText().isEmpty()) return;
        try {
            String pass = new String(txtPassword.getPassword());
            controller.crear(txtCedula.getText(), txtPrimerNombre.getText(), txtSegundoNombre.getText(),
                    txtPrimerApellido.getText(), txtSegundoApellido.getText(), txtTelefono.getText(),
                    txtCorreo.getText(), pass, 
                    (String)cboRol.getSelectedItem(), usuarioLogueadoUsername);
            
            JOptionPane.showMessageDialog(this, "Usuario creado exitosamente.");
            cargarTabla();
            limpiarFormulario();
        } catch(Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editarUsuario() {
        if (txtId.getText().isEmpty() || usuarioSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un usuario para editar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            Usuario u = new Usuario();
            u.setId(Integer.parseInt(txtId.getText()));
            u.setCedula(txtCedula.getText());
            u.setPrimerNombre(txtPrimerNombre.getText());
            u.setSegundoNombre(txtSegundoNombre.getText());
            u.setPrimerApellido(txtPrimerApellido.getText());
            u.setSegundoApellido(txtSegundoApellido.getText());
            u.setTelefono(txtTelefono.getText());
            u.setEmail(txtCorreo.getText());
            u.setRol((String)cboRol.getSelectedItem());
            u.setUsername(txtUsername.getText());
            
            u.setPassword(usuarioSeleccionado.getPassword()); 
            
            if(controller.actualizar(u)) {
                JOptionPane.showMessageDialog(this, "Datos actualizados correctamente.");
                cargarTabla();
                limpiarFormulario();
            }
        } catch(Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void resetearContrasenaDefault() {
        if (txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Seleccione un usuario primero.");
            return;
        }
        if (!validarAdmin("¿Desea resetear la contraseña a 'Sist.1234!'?")) return;

        try {
            int id = Integer.parseInt(txtId.getText());
            if (controller.cambiarPassword(id, "Sist.1234!")) {
                JOptionPane.showMessageDialog(this, "✅ Contraseña reseteada.");
                limpiarFormulario();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }
    
    private void cambiarEstadoLogico() {
        if (txtId.getText().isEmpty()) return;
        int id = Integer.parseInt(txtId.getText());
        boolean estaActivo = lblEstadoActual.getText().equals("ACTIVO");
        
        if (!validarAdmin("Confirme cambio de estado:")) return;

        if (controller.cambiarEstado(id, !estaActivo)) {
            JOptionPane.showMessageDialog(this, "Estado actualizado.");
            cargarTabla();
            limpiarFormulario();
        }
    }

    private void eliminarFisico() {
        if (txtId.getText().isEmpty()) return;
        int id = Integer.parseInt(txtId.getText());

        if (!validarAdmin("⚠ ADVERTENCIA: Borrado Definitivo.\nConfirme contraseña:")) return;

        try {
            if (controller.eliminarTotalmente(id)) {
                JOptionPane.showMessageDialog(this, "Usuario eliminado.");
                cargarTabla();
                limpiarFormulario();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private boolean validarAdmin(String mensaje) {
        JPanel p = new JPanel(new GridLayout(2, 1, 5, 5));
        p.add(new JLabel(mensaje));
        JPasswordField pf = new JPasswordField();
        p.add(pf);
        
        int ok = JOptionPane.showConfirmDialog(this, p, "Seguridad", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
        
        if (ok == JOptionPane.OK_OPTION) {
            String pass = new String(pf.getPassword());
            if (controller.login(usuarioLogueadoUsername, pass) != null) return true;
            else JOptionPane.showMessageDialog(this, "Contraseña incorrecta.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }

    private void limpiarFormulario() {
        txtId.setText(""); txtCedula.setText(""); txtPrimerNombre.setText(""); txtSegundoNombre.setText("");
        txtPrimerApellido.setText(""); txtSegundoApellido.setText(""); txtTelefono.setText(""); txtCorreo.setText("");
        txtUsername.setText(""); txtPassword.setText("");
        txtPassword.setEditable(true);
        txtPassword.setBackground(Color.WHITE);
        lblEstadoActual.setText("---");
        lblEstadoActual.setForeground(Color.BLACK);
        btnEstado.setText("Desactivar");
        usuarioSeleccionado = null; 
        tabla.clearSelection();
    }
    
    private void generarPDF() {
        JFileChooser fc = new JFileChooser();
        fc.setSelectedFile(new File("Reporte_Usuarios.pdf"));
        if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            GeneradorReporteUsuarios.generar(controller.listar(), fc.getSelectedFile().getAbsolutePath());
        }
    }
    
    private void $$$setupUI$$$() {}
}