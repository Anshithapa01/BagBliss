package com.ecommerce.library.utils;

import com.ecommerce.library.dto.WeeklyEarnings;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WeeklyPdfGenerator {
    private List<WeeklyEarnings> orders;

    public void generate(List<WeeklyEarnings> orders, HttpServletResponse response)
            throws DocumentException, IOException {
        Document document = new Document();
        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();

        // Title
        Font fontTitle = FontFactory.getFont(FontFactory.TIMES_ROMAN, 20, Font.BOLD);
        Paragraph titleParagraph = new Paragraph("Weekly Sales Report", fontTitle);
        titleParagraph.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(titleParagraph);

        // Add space
        document.add(new Paragraph(" "));

        // Create table with 8 columns (including Date)
        PdfPTable table = new PdfPTable(8);
        table.setWidthPercentage(100); // Set table width to 100% of the page
        table.setSpacingBefore(10f); // Add space before the table
        table.setSpacingAfter(10f); // Add space after the table

        // Add table headers
        table.addCell("Date");
        table.addCell("Order ID");
        table.addCell("Product Name");
        table.addCell("Quantity");
        table.addCell("Total");
        table.addCell("Deduction");
        table.addCell("Shipping Fee");
        table.addCell("Paid");

        List<Double> dailyTotals = new ArrayList<>();
        Double dailyTotal = 0.0;
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd (EEEE)"); // Date format with day name
        DecimalFormat df = new DecimalFormat("#.00"); // Decimal format for rounding

        String prevDate = "";
        Long prevOrderId = null;

        for (int i = 0; i < orders.size(); i++) {
            WeeklyEarnings order = orders.get(i);
            String dateFormatted = dateFormat.format(order.getOrderDate());

            // Check if date cell needs to be added
            if (!dateFormatted.equals(prevDate)) {
                // Add daily total row before adding a new date row
                if (!prevDate.isEmpty() && dailyTotal != 0.0) {
                    PdfPCell dailyTotalCell = new PdfPCell(new Phrase("Daily Total"));
                    dailyTotalCell.setColspan(7);
                    table.addCell(dailyTotalCell);
                    table.addCell(df.format(dailyTotal));
                    dailyTotals.add(dailyTotal); // Store the daily total
                    dailyTotal = 0.0; // Reset daily total for next date
                }

                PdfPCell dateCell = new PdfPCell(new Phrase(dateFormatted));
                dateCell.setRowspan(getRowSpanForDate(orders, order.getOrderDate()));
                table.addCell(dateCell);
                prevDate = dateFormatted;
            }

            // Check if order ID cell needs to be added
            if (!order.getOrderId().equals(prevOrderId)) {
                PdfPCell orderIdCell = new PdfPCell(new Phrase(order.getOrderId().toString()));
                orderIdCell.setRowspan(getRowSpanForOrderId(orders, order.getOrderId()));
                table.addCell(orderIdCell);
                prevOrderId = order.getOrderId();
            }

            // Add product details
            table.addCell(order.getProductName());
            table.addCell(order.getQuantity().toString());
            table.addCell(order.getTotalPrice().toString());

            // Add deduction, shipping fee, and paid for the first occurrence of each order ID
            if (i == 0 || !order.getOrderId().equals(orders.get(i - 1).getOrderId())) {
                table.addCell(order.getDeduction().toString());
                table.addCell(order.getShippingFee().toString());
                table.addCell(order.getTotalAmount().toString());
                dailyTotal += order.getTotalAmount(); // Add to daily total only for the first occurrence
            } else {
                // Skip adding deduction, shipping fee, and paid for subsequent occurrences of the same order ID
                table.addCell("");
                table.addCell("");
                table.addCell("");
            }

            // Check if it's the last order or the date changes in the next iteration
            if (i == orders.size() - 1 || !dateFormat.format(orders.get(i + 1).getOrderDate()).equals(dateFormatted)) {
                // Add daily total row
                PdfPCell dailyTotalCell = new PdfPCell(new Phrase("Daily Total"));
                dailyTotalCell.setColspan(7);
                table.addCell(dailyTotalCell);
                table.addCell(df.format(dailyTotal));
                dailyTotals.add(dailyTotal); // Store the daily total
                dailyTotal = 0.0; // Reset daily total for next date
            }
        }

        // Calculate the weekly grand total as the sum of all daily totals
        Double weeklyGrandTotal = dailyTotals.stream().mapToDouble(Double::doubleValue).sum();

        // Add a row for the weekly grand total
        PdfPCell cell = new PdfPCell(new Phrase("Weekly Grand Total"));
        cell.setColspan(7); // Span the cell across 7 columns
        table.addCell(cell);
        table.addCell(df.format(weeklyGrandTotal));

        // Add table to the document
        document.add(table);

        document.close();
    }

    // Helper method to calculate rowspan for a specific date
    private int getRowSpanForDate(List<WeeklyEarnings> orders, Date date) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String targetDate = dateFormat.format(date);
        int rowSpan = 0;

        for (WeeklyEarnings order : orders) {
            if (dateFormat.format(order.getOrderDate()).equals(targetDate)) {
                rowSpan++;
            }
        }

        return rowSpan;
    }

    // Helper method to calculate rowspan for a specific order ID
    private int getRowSpanForOrderId(List<WeeklyEarnings> orders, Long orderId) {
        int rowSpan = 0;

        for (WeeklyEarnings order : orders) {
            if (order.getOrderId().equals(orderId)) {
                rowSpan++;
            }
        }

        return rowSpan;
    }

}