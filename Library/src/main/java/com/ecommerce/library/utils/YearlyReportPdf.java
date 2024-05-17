package com.ecommerce.library.utils;
import com.ecommerce.library.dto.YearlyEarning;
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
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class YearlyReportPdf {
    private List<YearlyEarning> yearlyEarnings;

    public void generate(HttpServletResponse response) throws DocumentException, IOException {

        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();
        Font fontTitle = FontFactory.getFont(FontFactory.TIMES_ROMAN);
        fontTitle.setSize(20);
        Paragraph paragraph = new Paragraph("Yearly Earnings Report", fontTitle);
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

        cell.setPhrase(new Phrase("Year", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Total Earnings", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Total Orders", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Discount", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Delivered Orders", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Cancelled Orders", font));
        table.addCell(cell);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");

        for (YearlyEarning yearlyEarning : yearlyEarnings) {
            table.addCell(dateFormat.format(yearlyEarning.getYear()));
            table.addCell(String.valueOf(yearlyEarning.getTotalEarnings()));
            table.addCell(String.valueOf(yearlyEarning.getTotalOrders()));
            table.addCell(String.valueOf(yearlyEarning.getCouponDeduction()));
            table.addCell(String.valueOf(yearlyEarning.getDelivered_orders()));
            table.addCell(String.valueOf(yearlyEarning.getCancelled_orders()));
        }

        document.add(table);
        document.close();
    }
}
