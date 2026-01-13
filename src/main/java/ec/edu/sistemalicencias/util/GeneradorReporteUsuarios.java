package ec.edu.sistemalicencias.util;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import ec.edu.sistemalicencias.model.Usuario;

import javax.swing.JOptionPane;
import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class GeneradorReporteUsuarios {

    public static void generar(List<Usuario> usuarios, String rutaDestino) {
        // Documento horizontal (Rotate) para que quepan todas las columnas
        Document documento = new Document(PageSize.A4.rotate());
        documento.setMargins(20, 20, 20, 20);

        try {
            PdfWriter.getInstance(documento, new FileOutputStream(rutaDestino));
            documento.open();

            // ==========================================
            // 1. ENCABEZADO (Estilo Antiguo)
            // ==========================================
            PdfPTable headerTable = new PdfPTable(2);
            headerTable.setWidthPercentage(100);
            headerTable.setWidths(new float[]{1.5f, 5.5f}); // 20% Logo, 80% Texto

            // A. LOGO (Izquierda)
            PdfPCell cellLogo = new PdfPCell();
            cellLogo.setBorder(Rectangle.NO_BORDER);
            cellLogo.setVerticalAlignment(Element.ALIGN_MIDDLE);
            try {
                // Intenta cargar "/images/ImagenANT.jpg" o usa texto si falla
                java.net.URL urlImagen = GeneradorReporteUsuarios.class.getResource("/images/ImagenANT.jpg");
                if (urlImagen != null) {
                    Image logo = Image.getInstance(urlImagen);
                    logo.scaleToFit(90, 90);
                    cellLogo.addElement(logo);
                } else {
                    cellLogo.addElement(new Phrase("ANT (Logo)"));
                }
            } catch (Exception e) {
                cellLogo.addElement(new Phrase("ANT"));
            }
            headerTable.addCell(cellLogo);

            // B. TEXTO (Derecha - Alineado a la derecha como la imagen antigua)
            PdfPCell cellText = new PdfPCell();
            cellText.setBorder(Rectangle.NO_BORDER);
            cellText.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cellText.setHorizontalAlignment(Element.ALIGN_RIGHT);

            Font fontTitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20, new BaseColor(40, 40, 40));
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
            
            // Línea separadora gris
            LineSeparator ls = new LineSeparator();
            ls.setLineColor(BaseColor.LIGHT_GRAY);
            documento.add(ls);
            documento.add(Chunk.NEWLINE);

            // ==========================================
            // 2. TABLA DE DATOS (Columnas Solicitadas)
            // ==========================================
            // ID, Cédula, Nombres, Apellidos, Telf, Correo, Estado, Creado Por, Fecha Creación
            PdfPTable tabla = new PdfPTable(9);
            tabla.setWidthPercentage(100);
            // Ajuste de anchos para que se vea bien
            tabla.setWidths(new float[]{0.8f, 2.2f, 3f, 3f, 2.2f, 3.5f, 1.5f, 2f, 2.5f});

            // ENCABEZADOS (Fondo Oscuro como la imagen antigua)
            String[] headers = {"ID", "Cédula", "Nombres", "Apellidos", "Telf.", "Correo", "Estado", "Creado Por", "Fecha/ Hora de Creación"};
            Font fontHeader = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9, BaseColor.WHITE);
            BaseColor colorHeader = new BaseColor(30, 30, 30); // Gris muy oscuro casi negro

            for (String h : headers) {
                PdfPCell celda = new PdfPCell(new Phrase(h, fontHeader));
                celda.setBackgroundColor(colorHeader);
                celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
                celda.setPadding(6);
                tabla.addCell(celda);
            }

            // DATOS
            Font fontData = FontFactory.getFont(FontFactory.HELVETICA, 8, BaseColor.BLACK);
            SimpleDateFormat formatFechaRegistro = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            boolean filaGris = false;

            for (Usuario u : usuarios) {
                // Alternar color de fondo (Zebra)
                BaseColor bg = filaGris ? new BaseColor(235, 235, 235) : BaseColor.WHITE;

                // 1. ID
                agregarCelda(tabla, String.valueOf(u.getId()), fontData, bg);
                
                // 2. Cédula
                agregarCelda(tabla, u.getCedula(), fontData, bg);
                
                // 3. Nombres (Combinamos 1er y 2do)
                String nom = u.getPrimerNombre() + " " + (u.getSegundoNombre() != null ? u.getSegundoNombre() : "");
                agregarCelda(tabla, nom, fontData, bg);
                
                // 4. Apellidos (Combinamos 1er y 2do)
                String ape = u.getPrimerApellido() + " " + (u.getSegundoApellido() != null ? u.getSegundoApellido() : "");
                agregarCelda(tabla, ape, fontData, bg);
                
                // 5. Teléfono
                agregarCelda(tabla, u.getTelefono(), fontData, bg);
                
                // 6. Correo
                agregarCelda(tabla, u.getEmail(), fontData, bg);
                
                // 7. Estado
                String estado = u.isActivo() ? "ACTIVO" : "INACTIVO";
                agregarCelda(tabla, estado, fontData, bg);
                
                // 8. Creado Por
                agregarCelda(tabla, u.getCreadoPor(), fontData, bg);
                
                // 9. Fecha Creación
                String fechaReg = u.getFechaRegistro() != null ? formatFechaRegistro.format(u.getFechaRegistro()) : "-";
                agregarCelda(tabla, fechaReg, fontData, bg);

                filaGris = !filaGris;
            }

            documento.add(tabla);

            // Pie de total
            documento.add(Chunk.NEWLINE);
            Paragraph total = new Paragraph("Total de registros: " + usuarios.size(), fontSub);
            total.setAlignment(Element.ALIGN_RIGHT);
            documento.add(total);

            documento.close();

            // Abrir archivo automáticamente
            int resp = JOptionPane.showConfirmDialog(null, 
                "Reporte generado exitosamente.\nUbicación: " + rutaDestino + "\n\n¿Desea abrirlo?", 
                "Éxito", JOptionPane.YES_NO_OPTION);
            
            if (resp == JOptionPane.YES_OPTION) {
                try {
                    Desktop.getDesktop().open(new File(rutaDestino));
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "No se pudo abrir el archivo.");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error PDF: " + e.getMessage());
        }
    }

    private static void agregarCelda(PdfPTable tabla, String texto, Font fuente, BaseColor fondo) {
        PdfPCell celda = new PdfPCell(new Phrase(texto != null ? texto : "", fuente));
        celda.setBackgroundColor(fondo);
        celda.setPadding(5);
        celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
        celda.setHorizontalAlignment(Element.ALIGN_CENTER); // Centrado para que se vea ordenado
        tabla.addCell(celda);
    }
}