package com.ecommerce.library.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class YearlyEarning {
    private Date year;
    private double totalEarnings;
    private Long totalOrders;
    private Double couponDeduction;
    private Long delivered_orders;
    private Long cancelled_orders;

    public YearlyEarning(Date year, Double totalEarnings,
                         Long totalOrders,Double couponDeduction,
                         Long delivered_orders, Long cancelled_orders) {
        this.year = year;
        this.totalEarnings = totalEarnings;
        this.totalOrders=totalOrders;
        this.couponDeduction=couponDeduction;
        this.delivered_orders=delivered_orders;
        this.cancelled_orders=cancelled_orders;
    }
}
