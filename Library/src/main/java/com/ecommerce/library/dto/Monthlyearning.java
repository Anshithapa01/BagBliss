package com.ecommerce.library.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class Monthlyearning {
  private Date month;
   private Double earning;
   private Long totalOrder;
   private Long delivered_orders;
   private Long cancelled_orders;

    public Monthlyearning(Date month, Double earning,Long totalOrder,Long delivered_orders,Long cancelled_orders) {
        this.month = month;
        this.earning = earning;
        this.totalOrder=totalOrder;

        this.delivered_orders=delivered_orders;
        this.cancelled_orders=cancelled_orders;
    }
}
