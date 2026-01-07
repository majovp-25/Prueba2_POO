package ec.edu.sistemalicencias.util;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import ec.edu.sistemalicencias.model.Usuario;

import javax.swing.JOptionPane;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.List;
import java.awt.Desktop;
import java.io.File;

public class GeneradorReporteUsuarios {

    public static void generarPDF(List<Usuario> listaUsuarios, String rutaDestino) {

        // 1. Configuramos la hoja horizontal
        Document documento = new Document(PageSize.A4.rotate());

        try {
            // 2. Usamos la ruta que nos mandaron desde la vista
            PdfWriter.getInstance(documento, new FileOutputStream(rutaDestino));
            documento.open();

            // --- TÍTULO ---
            Font fontTitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.DARK_GRAY);
            Paragraph titulo = new Paragraph("Reporte General de Usuarios", fontTitulo);
            titulo.setAlignment(Element.ALIGN_CENTER);
            documento.add(titulo);

            documento.add(new Paragraph("Fecha: " + new Date().toString()));
            documento.add(Chunk.NEWLINE);

            // --- TABLA ---
            PdfPTable tabla = new PdfPTable(7);
            tabla.setWidthPercentage(100);
            float[] anchos = {1f, 2f, 2f, 3f, 3f, 2.5f, 4f};
            tabla.setWidths(anchos);

            // Encabezados
            String[] headers = {"ID", "Usuario", "Rol", "Nombres", "Apellidos", "Telf.", "Correo"};
            Font fontHeader = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, BaseColor.WHITE);

            for (String header : headers) {
                PdfPCell celda = new PdfPCell(new Phrase(header, fontHeader));
                celda.setBackgroundColor(new BaseColor(50, 50, 50));
                celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                celda.setPadding(6);
                tabla.addCell(celda);
            }

            // Datos
            Font fontData = FontFactory.getFont(FontFactory.HELVETICA, 9, BaseColor.BLACK);
            boolean filaGris = false;

            for (Usuario u : listaUsuarios) {
                BaseColor colorFondo = filaGris ? new BaseColor(240, 240, 240) : BaseColor.WHITE;

                // Usamos getters correctos (singular)
                agregarCelda(tabla, String.valueOf(u.getId()), fontData, colorFondo);
                agregarCelda(tabla, u.getUsername(), fontData, colorFondo);
                agregarCelda(tabla, u.getRol(), fontData, colorFondo);
                agregarCelda(tabla, u.getNombre(), fontData, colorFondo);
                agregarCelda(tabla, u.getApellido(), fontData, colorFondo);
                agregarCelda(tabla, u.getTelefono(), fontData, colorFondo);
                agregarCelda(tabla, u.getEmail(), fontData, colorFondo);

                filaGris = !filaGris;
            }

            documento.add(tabla);

            // Total
            documento.add(Chunk.NEWLINE);
            documento.add(new Paragraph("Total: " + listaUsuarios.size()));

            documento.close();

            // 3. Confirmación y abrir archivo
            int resp = JOptionPane.showConfirmDialog(null,
                    "✅ Reporte guardado en:\n" + rutaDestino + "\n\n¿Deseas abrirlo ahora?",
                    "Éxito", JOptionPane.YES_NO_OPTION);

            if (resp == JOptionPane.YES_OPTION) {
                try {
                    Desktop.getDesktop().open(new File(rutaDestino));
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "No se pudo abrir el archivo automáticamente.");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al guardar PDF: " + e.getMessage());
        }
    }

    // Método auxiliar (Ahora sí está DENTRO de la clase)
    private static void agregarCelda(PdfPTable tabla, String texto, Font fuente, BaseColor fondo) {
        PdfPCell celda = new PdfPCell(new Phrase(texto != null ? texto : "", fuente));
        celda.setPadding(5);
        celda.setBackgroundColor(fondo);
        celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
        tabla.addCell(celda);
    }
}