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

    public WeeklyEarnings(Date week, Double earnings) {
        this.week = week;
        this.earnings = earnings;
    }
}
