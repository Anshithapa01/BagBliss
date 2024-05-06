package com.ecommerce.library.utils;

import com.ecommerce.library.dto.DailyEarning;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.List;
@Component
public class CsvGeneratorDaily {
    private static final String CSV_HEADER = "Date,TotelOrder,Earning\n";
    private static final String DATE_FORMAT = "yyyy-MM-dd";

    public String generateCsv(List<DailyEarning> dailyEarning) {
        StringBuilder csvContent = new StringBuilder();
        csvContent.append(CSV_HEADER);


        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);

        for (DailyEarning daily : dailyEarning) {
            csvContent.append(dateFormat.format(daily.getDate())).append(",")
                    .append(daily.getTotalOrder()).append(",")
                    .append(daily.getEarnings()).append("\n");

        }

        return csvContent.toString();
    }
}
