package com.ecommerce.library.utils;

import com.ecommerce.library.dto.YearlyEarning;
import org.springframework.stereotype.Component;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


@Component
public class CsvGeneratorYearly {
    private static final String CSV_HEADER = "Year,Total Earning,Total Orders,Total Discount,Delivered Orders,Cancelled Orders\n";


    public String generateCsv(List<YearlyEarning> yearlyEarnings) {
        StringBuilder csvContent = new StringBuilder();
        csvContent.append(CSV_HEADER);




        for (YearlyEarning yearly : yearlyEarnings) {
                    Date date=yearly.getYear();
            String formattedMonth = formatYearToString(date);
            csvContent.append(formattedMonth).append(",")
                    .append(yearly.getTotalEarnings()).append(",")
                    .append(yearly.getTotalOrders()).append(",")
                    .append(yearly.getCouponDeduction()).append(",")
                    .append(yearly.getDelivered_orders()).append(",")
                    .append(yearly.getCancelled_orders()).append("\n");

        }

        return csvContent.toString();
    }
    private String formatYearToString(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        return sdf.format(date);
    }

}
