package com.ecommerce.library.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class WeeklyEarnings {
    private Date week;
    private Double earnings;
    private Long totalOrders;
    private Double deduction;
    private Long deliveredOrders;
    private Long cancelledOrders;

    public WeeklyEarnings(Date week, Double earnings,
                          Long totalOrders,Double deduction,
                          Long deliveredOrders,Long cancelledOrders) {
        this.week = week;
        this.earnings = earnings;
        this.totalOrders=totalOrders;
        this.deduction=deduction;
        this.deliveredOrders=deliveredOrders;
        this.cancelledOrders=cancelledOrders;
    }
}
