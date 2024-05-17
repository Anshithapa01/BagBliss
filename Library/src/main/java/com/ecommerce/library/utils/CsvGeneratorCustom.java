package com.ecommerce.library.utils;

import com.ecommerce.library.dto.CustomEarning;
import com.ecommerce.library.dto.DailyEarning;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.List;

@Component
public class CsvGeneratorCustom {
    private static final String CSV_HEADER = "Start Date,End Date,Total Earning,Total Orders,Total Deduction,Delivered Orders,Cancelled Orders\n";
    private static final String DATE_FORMAT = "yyyy-MM-dd";

    public String generateCsv(List<CustomEarning> customEarnings) {
        StringBuilder csvContent = new StringBuilder();
        csvContent.append(CSV_HEADER);


        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);

        for (CustomEarning custom : customEarnings) {
            csvContent.append(dateFormat.format(custom.getStartDate())).append(",")
                    .append(dateFormat.format(custom.getEndDate())).append(",")
                    .append(custom.getTotalEarnings()).append(",")
                    .append(custom.getTotalOrders()).append(",")
                    .append(custom.getCouponDeduction()).append(",")
                    .append(custom.getDelivered_orders()).append(",")
                    .append(custom.getCancelled_orders()).append("\n");
        }

        return csvContent.toString();
    }
}
