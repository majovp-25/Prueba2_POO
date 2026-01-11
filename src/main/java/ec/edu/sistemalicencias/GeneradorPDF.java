package ec.edu.sistemalicencias; 

// IMPORTACIONES:
// Document: Es como la hoja de papel en blanco.
// Paragraph: Para escribir párrafos de texto.
// PdfPTable: Para crear tablas (la P es de porcentaje, se ajusta sola).
// PdfWriter: Es el "bolígrafo" que escribe en el disco duro.
import com.itextpdf.text.Document;
import ec.edu.sistemalicencias.model.Usuario;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileOutputStream;
import java.util.List;

public class GeneradorPDF {

    // Método principal: Recibe la lista de usuarios y el nombre del archivo a crear
    public void generarReporte(List<Usuario> listaUsuarios, String nombreArchivo) {
        
        // Creamos la "hoja de papel" (Documento)
        Document documento = new Document();

        try {
            // Preparamos el "escritor". Le decimos:
            // "Usa este documento y guárdalo en esta ruta (nombreArchivo)"
            PdfWriter.getInstance(documento, new FileOutputStream(nombreArchivo));

            // Abrimos el documento para empezar a editar
            // (Si no haces esto, te dará error al intentar escribir)
            documento.open();

            // Agregamos el Título
            Paragraph titulo = new Paragraph("Reporte de Usuarios del Sistema");
            titulo.setAlignment(Paragraph.ALIGN_CENTER); // Lo centramos
            documento.add(titulo);

            // Agregamos un espacio en blanco para que no se pegue la tabla al título
            documento.add(new Paragraph(" ")); 

            // Creamos la Tabla
            // El número '3' indica que tendrá 3 columnas (ID, Usuario, Rol)
            PdfPTable tabla = new PdfPTable(3);
            
            // Ponemos los Encabezados (La primera fila)
            tabla.addCell("ID");
            tabla.addCell("Usuario");
            tabla.addCell("Rol");

            // Llenamos la tabla con los datos reales
            // Recorremos la lista que nos enviaron
            for (Usuario u : listaUsuarios) {
                tabla.addCell(String.valueOf(u.getId())); // Convertimos el número a texto
                tabla.addCell(u.getUsername());
                tabla.addCell(u.getRol());
            }

            // Agregamos la tabla llena al documento
            documento.add(tabla);

            // Cerramos el documento.
            // ¡IMPORTANTE! Si no cierras, el PDF queda corrupto
            documento.close();
            
            System.out.println("--- PDF Generado correctamente: " + nombreArchivo + " ---");

        } catch (Exception e) {
            // Si algo falla (ej. disco lleno, o archivo abierto), cae aquí
            System.err.println("Error al crear el PDF: " + e.getMessage());
        }
    }
}