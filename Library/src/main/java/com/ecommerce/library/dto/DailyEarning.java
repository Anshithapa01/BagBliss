package com.ecommerce.library.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor

public class DailyEarning {
    private Date date;
    private Double earnings;
    private Long totalOrders;
    private Double deduction;
    private Long deliveredOrders;
    private Long cancelledOrders;

    public DailyEarning(Date date, double earnings, Long totalOrders,
                        Double deduction,Long deliveredOrders,
                        long cancelledOrders) {
        this.date = date;
        this.earnings = earnings;
        this.totalOrders=totalOrders;
        this.deduction=deduction;
        this.deliveredOrders=deliveredOrders;
        this.cancelledOrders=cancelledOrders;
    }
}
