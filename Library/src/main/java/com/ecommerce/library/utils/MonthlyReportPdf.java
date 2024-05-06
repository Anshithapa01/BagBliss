package com.ecommerce.library.utils;

import com.ecommerce.library.dto.Monthlyearning;
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MonthlyReportPdf {
    private List<Monthlyearning> orders;

    public void generate(HttpServletResponse response) throws DocumentException, IOException {

        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();
        Font fontTiltle = FontFactory.getFont(FontFactory.TIMES_ROMAN);
        fontTiltle.setSize(20);
        Paragraph paragraph = new Paragraph("Monthly Report", fontTiltle);
        paragraph.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(paragraph);
        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100f);
        table.setWidths(new int[]{3, 3,3,3,3});
        table.setSpacingBefore(5);

        PdfPCell cell = new PdfPCell();

        cell.setBackgroundColor(CMYKColor.MAGENTA);
        cell.setPadding(5);

        Font font = FontFactory.getFont(FontFactory.TIMES_ROMAN);
        font.setColor(CMYKColor.WHITE);

        cell.setPhrase(new Phrase("Month", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Earning", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Totel Orders", font));
         table.addCell(cell);
        cell.setPhrase(new Phrase("Delivered Orders", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Cancel Orders", font));
        table.addCell(cell);

        for (Monthlyearning order : orders) {
            Date date = order.getMonth();
            String formattedMonth = formatMonthToString(date);

            table.addCell(formattedMonth);


            table.addCell(String.valueOf(order.getEarning()));
            table.addCell(String.valueOf(order.getTotalOrder()));
            table.addCell(String.valueOf(order.getDelivered_orders()));
            table.addCell(String.valueOf(order.getCancelled_orders()));
        }
        document.add(table);

        document.close();

    }

    private String formatMonthToString(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM yyyy");
        return sdf.format(date);
    }





}
