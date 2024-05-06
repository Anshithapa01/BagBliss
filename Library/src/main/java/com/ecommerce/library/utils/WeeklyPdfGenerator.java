package com.ecommerce.library.utils;

import com.ecommerce.library.dto.WeeklyEarnings;
import com.lowagie.text.*;
import com.lowagie.text.pdf.CMYKColor;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WeeklyPdfGenerator {
    private List<WeeklyEarnings> orders;

    public void generate(HttpServletResponse response) throws DocumentException, IOException {

        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();
        Font fontTiltle = FontFactory.getFont(FontFactory.TIMES_ROMAN);
        fontTiltle.setSize(20);
        Paragraph paragraph = new Paragraph("Weekly report", fontTiltle);
        paragraph.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(paragraph);
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100f);
        table.setWidths(new int[]{3, 3});
        table.setSpacingBefore(5);

        PdfPCell cell = new PdfPCell();

        cell.setBackgroundColor(CMYKColor.MAGENTA);
        cell.setPadding(5);

        Font font = FontFactory.getFont(FontFactory.TIMES_ROMAN);
        font.setColor(CMYKColor.WHITE);

        cell.setPhrase(new Phrase("Week", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Earning", font));
        table.addCell(cell);
        // cell.setPhrase(new Phrase("Totel Orders", font));
        //   table.addCell(cell);

        for (WeeklyEarnings order : orders) {
            Date date = order.getWeek();
            LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            // Format LocalDate as String
            String formattedDate = formatDateToDateString(localDate);

            table.addCell(formattedDate);
            table.addCell(String.valueOf(order.getEarnings()));
            // table.addCell(String.valueOf(order.getTotelOrder()));
        }
        document.add(table);

        document.close();

    }

    private String formatDateToDateString(LocalDate localDate) {
        return localDate.toString();  // Assumes the default format of "yyyy-MM-dd"
    }
}
