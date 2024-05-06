package com.ecommerce.library.utils;

import com.ecommerce.library.dto.Monthlyearning;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Component
public class CsvGeneratorMonthly {
    private static final String CSV_HEADER = "Month,Earning,Totel Order,Deliverd Order,Canell Order\n";


    public String generateCsv(List<Monthlyearning> monthlyearnings) {
        StringBuilder csvContent = new StringBuilder();
        csvContent.append(CSV_HEADER);




        for (Monthlyearning montly : monthlyearnings) {
                    Date date=montly.getMonth();
            String formattedMonth = formatMonthToString(date);
            csvContent.append(formattedMonth).append(",")
                    .append(montly.getEarning()).append(",")
                    .append(montly.getTotalOrder()).append(",")
                    .append(montly.getDelivered_orders()).append(",")
                    .append(montly.getCancelled_orders()).append("\n");

        }

        return csvContent.toString();
    }
    private String formatMonthToString(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM yyyy");
        return sdf.format(date);
    }

}
