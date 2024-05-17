package com.ecommerce.library.utils;

import com.ecommerce.library.dto.DailyEarning;
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
public class PdfGenerator {

    private List<DailyEarning> orders;

    public void generate(HttpServletResponse response) throws DocumentException, IOException {

        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();
        Font fontTiltle = FontFactory.getFont(FontFactory.TIMES_ROMAN);
        fontTiltle.setSize(20);
        Paragraph paragraph = new Paragraph("Daily report", fontTiltle);
        paragraph.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(paragraph);
        PdfPTable table = new PdfPTable(6);
        table.setWidthPercentage(100f);
        table.setWidths(new int[]{2, 2, 2, 2, 2, 2});
        table.setSpacingBefore(10);

        PdfPCell cell = new PdfPCell();

        cell.setBackgroundColor(CMYKColor.MAGENTA);
        cell.setPadding(5);

        Font font = FontFactory.getFont(FontFactory.TIMES_ROMAN);
        font.setColor(CMYKColor.WHITE);

        cell.setPhrase(new Phrase("Date", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Earning", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Total Orders", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Total Deduction", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Delivered Orders", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Cancelled Orders", font));
        table.addCell(cell);

        for (DailyEarning order : orders) {
            Date date = order.getDate();
            LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            String formattedDate = formatDateToDateString(localDate);

            table.addCell(formattedDate);
            table.addCell(String.valueOf(order.getEarnings()));
            table.addCell(String.valueOf(order.getTotalOrders()));
            table.addCell(String.valueOf(order.getDeduction()));
            table.addCell(String.valueOf(order.getDeliveredOrders()));
            table.addCell(String.valueOf(order.getCancelledOrders()));
        }
        document.add(table);

        document.close();

    }

    private String formatDateToDateString(LocalDate localDate) {
        return localDate.toString();
    }
}
