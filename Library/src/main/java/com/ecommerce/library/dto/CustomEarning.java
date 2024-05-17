package com.ecommerce.library.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomEarning {
    private Date startDate;
    private Date endDate;
    private double totalEarnings;
    private Long totalOrders;
    private Double couponDeduction;
    private Long delivered_orders;
    private Long cancelled_orders;

    public CustomEarning(Date startDate,Date endDate, Double totalEarnings,
                         Long totalOrders, Double couponDeduction,
                         Long delivered_orders, Long cancelled_orders) {
        this.startDate = startDate;
        this.endDate=endDate;
        this.totalEarnings = totalEarnings;
        this.totalOrders=totalOrders;
        this.couponDeduction=couponDeduction;
        this.delivered_orders=delivered_orders;
        this.cancelled_orders=cancelled_orders;
    }
}
