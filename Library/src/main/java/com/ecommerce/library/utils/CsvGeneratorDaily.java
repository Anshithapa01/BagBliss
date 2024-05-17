package com.ecommerce.library.utils;

import com.ecommerce.library.dto.DailyEarning;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.List;
@Component
public class CsvGeneratorDaily {
    private static final String CSV_HEADER = "Date,Total Earning,Total Orders,Total Deduction,Delivered Orders,Cancelled Orders\n";
    private static final String DATE_FORMAT = "yyyy-MM-dd";

    public String generateCsv(List<DailyEarning> dailyEarning) {
        StringBuilder csvContent = new StringBuilder();
        csvContent.append(CSV_HEADER);


        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);

        for (DailyEarning daily : dailyEarning) {
            csvContent.append(dateFormat.format(daily.getDate())).append(",")
                    .append(daily.getEarnings()).append(",")
                    .append(daily.getTotalOrders()).append(",")
                    .append(daily.getDeduction()).append(",")
                    .append(daily.getDeliveredOrders()).append(",")
                    .append(daily.getCancelledOrders()).append("\n");
        }

        return csvContent.toString();
    }
}
