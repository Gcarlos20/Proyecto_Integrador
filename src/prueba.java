import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import java.io.FileOutputStream;
import java.awt.Desktop;
import java.io.File;

public class prueba {

    public static void main(String[] args) {

        Document documento = new Document();

        try {

            // Crear PDF y establecer destino
            PdfWriter.getInstance(
                    documento,
                    new FileOutputStream("reporte_inventario.pdf")
            );

            documento.open();

            // =========================
            // TITULO
            // =========================

            Font tituloFont = FontFactory.getFont(
                    FontFactory.HELVETICA_BOLD,
                    22
            );

            Paragraph titulo = new Paragraph(
                    "REPORTE DE INVENTARIO",
                    tituloFont
            );

            titulo.setAlignment(Element.ALIGN_CENTER);

            documento.add(titulo);

            documento.add(new Paragraph(" "));
            documento.add(new Paragraph("Fecha: 11/05/2026"));
            documento.add(new Paragraph("Sistema: Inventario Arcadia"));
            documento.add(new Paragraph(" "));
            documento.add(new Paragraph(" "));

            // =========================
            // TABLA
            // =========================

            PdfPTable tabla = new PdfPTable(5);

            tabla.setWidthPercentage(100);

            tabla.setWidths(new float[]{1.5f, 4f, 2f, 2f, 2f});

            // =========================
            // ENCABEZADOS
            // =========================

            PdfPCell celda;

            celda = new PdfPCell(new Phrase("ID"));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            tabla.addCell(celda);

            celda = new PdfPCell(new Phrase("PRODUCTO"));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            tabla.addCell(celda);

            celda = new PdfPCell(new Phrase("STOCK"));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            tabla.addCell(celda);

            celda = new PdfPCell(new Phrase("PRECIO"));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            tabla.addCell(celda);

            celda = new PdfPCell(new Phrase("ESTADO"));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            tabla.addCell(celda);

            // =========================
            // DATOS FICTICIOS
            // =========================

            tabla.addCell("1");
            tabla.addCell("Mouse Gamer Redragon");
            tabla.addCell("15");
            tabla.addCell("$85.000");
            tabla.addCell("Disponible");

            tabla.addCell("2");
            tabla.addCell("Teclado Mecánico");
            tabla.addCell("8");
            tabla.addCell("$120.000");
            tabla.addCell("Disponible");

            tabla.addCell("3");
            tabla.addCell("Monitor Samsung 24");
            tabla.addCell("3");
            tabla.addCell("$780.000");
            tabla.addCell("Pocas unidades");

            tabla.addCell("4");
            tabla.addCell("Disco SSD 1TB");
            tabla.addCell("20");
            tabla.addCell("$350.000");
            tabla.addCell("Disponible");

            tabla.addCell("5");
            tabla.addCell("Portátil Lenovo i5");
            tabla.addCell("2");
            tabla.addCell("$2.800.000");
            tabla.addCell("Stock crítico");

            // Agregar tabla al documento
            documento.add(tabla);

            documento.add(new Paragraph(" "));
            documento.add(new Paragraph(" "));

            // =========================
            // PIE DE PAGINA
            // =========================

            Paragraph pie = new Paragraph(
                    "Reporte generado automáticamente por el sistema.",
                    FontFactory.getFont(
                            FontFactory.HELVETICA,
                            10
                    )
            );

            pie.setAlignment(Element.ALIGN_CENTER);

            documento.add(pie);

            // Cerrar PDF
            documento.close();

            // Abrir automáticamente
            Desktop.getDesktop().open(
                    new File("reporte_inventario.pdf")
            );

            System.out.println("Reporte generado correctamente");

        } catch (Exception e) {

            System.out.println("Error: " + e);

        }
    }
}