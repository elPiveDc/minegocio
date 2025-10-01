package com.minegocio.backend.util;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.minegocio.backend.entity.ObjetoBdFranquicia;
import org.json.JSONObject;

import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;

/**
 * Utilidad para generar reportes PDF usando OpenPDF.
 */
public class ReporteUtil {

    public static ByteArrayInputStream generarReporteTablas(List<ObjetoBdFranquicia> tablas) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, baos);
        document.open();

        // Título principal
        Font tituloFont = new Font(Font.HELVETICA, 18, Font.BOLD, Color.BLUE);
        Paragraph titulo = new Paragraph("Reporte de Tablas - Sistema de Franquicia", tituloFont);
        titulo.setAlignment(Element.ALIGN_CENTER);
        titulo.setSpacingAfter(20);
        document.add(titulo);

        // Iterar tablas registradas
        for (ObjetoBdFranquicia tabla : tablas) {
            Font subtituloFont = new Font(Font.HELVETICA, 14, Font.BOLD, Color.BLACK);
            Paragraph subtitulo = new Paragraph("Tabla: " + tabla.getNombreTabla(), subtituloFont);
            subtitulo.setSpacingBefore(15);
            subtitulo.setSpacingAfter(10);
            document.add(subtitulo);

            // Parsear estructura de columnas
            JSONObject columnasJson = new JSONObject(tabla.getColumnas());

            PdfPTable pdfTable = new PdfPTable(3); // columnas: nombre, tipo, restricciones
            pdfTable.setWidthPercentage(100);

            // Encabezados
            addHeaderCell(pdfTable, "Columna");
            addHeaderCell(pdfTable, "Tipo");
            addHeaderCell(pdfTable, "Restricciones");

            var columnas = columnasJson.getJSONArray("columnas");
            for (int i = 0; i < columnas.length(); i++) {
                JSONObject col = columnas.getJSONObject(i);

                pdfTable.addCell(new PdfPCell(new Phrase(col.getString("nombre"))));
                pdfTable.addCell(new PdfPCell(new Phrase(col.getString("tipo"))));
                pdfTable.addCell(new PdfPCell(new Phrase(col.optString("restricciones", "-"))));
            }

            document.add(pdfTable);
        }

        document.close();
        return new ByteArrayInputStream(baos.toByteArray());
    }

    private static void addHeaderCell(PdfPTable table, String text) {
        Font font = new Font(Font.HELVETICA, 12, Font.BOLD, Color.WHITE);
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setBackgroundColor(Color.DARK_GRAY);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
    }
}
