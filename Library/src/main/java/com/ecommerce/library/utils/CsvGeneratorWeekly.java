package com.ecommerce.library.utils;

import com.ecommerce.library.dto.WeeklyEarnings;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.List;

@Component
public class CsvGeneratorWeekly {
    private static final String CSV_HEADER = "Week,Earning\n";
    private static final String DATE_FORMAT = "yyyy-MM-dd";

    public String generateCsv(List<WeeklyEarnings> weeklyEarnings) {
        StringBuilder csvContent = new StringBuilder();
        csvContent.append(CSV_HEADER);


        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);

        for (WeeklyEarnings weekly : weeklyEarnings) {
            csvContent.append(dateFormat.format(weekly.getWeek())).append(",")
                    .append(weekly.getEarnings()).append("\n");


        }

        return csvContent.toString();
    }
}
