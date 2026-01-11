package ec.edu.sistemalicencias.util;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import ec.edu.sistemalicencias.model.Usuario;

import javax.swing.JOptionPane;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.awt.Desktop;
import java.io.File;

public class GeneradorReporteUsuarios {

    public static void generarPDF(List<Usuario> listaUsuarios, String rutaDestino) {

        Document documento = new Document(PageSize.A4.rotate());

        try {
            PdfWriter.getInstance(documento, new FileOutputStream(rutaDestino));
            documento.open();

            PdfPTable headerTable = new PdfPTable(2);
            headerTable.setWidthPercentage(100);
            headerTable.setWidths(new float[]{1f, 4f}); 

            PdfPCell cellLogo = new PdfPCell();
            cellLogo.setBorder(Rectangle.NO_BORDER);
            try {
                java.net.URL urlImagen = GeneradorReporteUsuarios.class.getResource("/images/ImagenANT.jpg");
                
                if (urlImagen != null) {
                    Image logo = Image.getInstance(urlImagen);
                    logo.scaleToFit(80, 80);
                    cellLogo.addElement(logo);
                } else {
                    cellLogo.addElement(new Phrase("ANT (Sin Logo)"));
                }
                
            } catch (Exception e) {
                cellLogo.addElement(new Phrase("ANT"));
                System.err.println("Error cargando logo: " + e.getMessage());
            }
            headerTable.addCell(cellLogo);

            PdfPCell cellText = new PdfPCell();
            cellText.setBorder(Rectangle.NO_BORDER);
            cellText.setVerticalAlignment(Element.ALIGN_MIDDLE);
            
            Font fontTitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.DARK_GRAY);
            Font fontSub = FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.GRAY);
            
            Paragraph pTitulo = new Paragraph("Reporte General de Usuarios - Agencia Nacional de Tránsito", fontTitulo);
            pTitulo.setAlignment(Element.ALIGN_RIGHT);
            
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            Paragraph pFecha = new Paragraph("Generado el: " + sdf.format(new Date()), fontSub);
            pFecha.setAlignment(Element.ALIGN_RIGHT);

            Paragraph pUser = new Paragraph("Generado por: Sistema Administrativo", fontSub);
            pUser.setAlignment(Element.ALIGN_RIGHT);

            cellText.addElement(pTitulo);
            cellText.addElement(pFecha);
            cellText.addElement(pUser);
            
            headerTable.addCell(cellText);

            documento.add(headerTable);
            documento.add(Chunk.NEWLINE);
            documento.add(new LineSeparator()); 
            documento.add(Chunk.NEWLINE);

            //Tabla con 8 columnas
            PdfPTable tabla = new PdfPTable(8);
            tabla.setWidthPercentage(100);

            // Ajustamos anchos para que quepa la nueva columna
            float[] anchos = {1f, 2f, 2f, 3f, 3f, 2.5f, 4f, 2.5f}; 
            tabla.setWidths(anchos);

            // Encabezados
            String[] headers = {"ID", "Usuario", "Rol", "Nombres", "Apellidos", "Telf.", "Correo", "Creado Por"};
            Font fontHeader = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9, BaseColor.WHITE);

            for (String header : headers) {
                PdfPCell celda = new PdfPCell(new Phrase(header, fontHeader));
                celda.setBackgroundColor(new BaseColor(50, 50, 50));
                celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                celda.setPadding(6);
                tabla.addCell(celda);
            }

            // Datos
            Font fontData = FontFactory.getFont(FontFactory.HELVETICA, 8, BaseColor.BLACK);
            boolean filaGris = false;

            for (Usuario u : listaUsuarios) {
                BaseColor colorFondo = filaGris ? new BaseColor(240, 240, 240) : BaseColor.WHITE;

                agregarCelda(tabla, String.valueOf(u.getId()), fontData, colorFondo);
                agregarCelda(tabla, u.getUsername(), fontData, colorFondo);
                agregarCelda(tabla, u.getRol(), fontData, colorFondo);
                agregarCelda(tabla, u.getNombre(), fontData, colorFondo);
                agregarCelda(tabla, u.getApellido(), fontData, colorFondo);
                agregarCelda(tabla, u.getTelefono(), fontData, colorFondo);
                agregarCelda(tabla, u.getEmail(), fontData, colorFondo);
                
                agregarCelda(tabla, u.getCreadoPor(), fontData, colorFondo);

                filaGris = !filaGris;
            }

            documento.add(tabla);

            documento.add(Chunk.NEWLINE);
            Paragraph total = new Paragraph("Total de registros: " + listaUsuarios.size());
            total.setAlignment(Element.ALIGN_RIGHT);
            documento.add(total);

            documento.close();


            int resp = JOptionPane.showConfirmDialog(null,
                    "Reporte guardado en:\n" + rutaDestino + "\n\n¿Deseas abrirlo ahora?",
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

    private static void agregarCelda(PdfPTable tabla, String texto, Font fuente, BaseColor fondo) {
        PdfPCell celda = new PdfPCell(new Phrase(texto != null ? texto : "-", fuente));
        celda.setPadding(5);
        celda.setBackgroundColor(fondo);
        celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
        tabla.addCell(celda);
    }
}