package com.ecommerce.library.service;

import java.util.Date;
import java.util.List;

public interface DashBoardService {
    double findCurrentMonthOrder(Date startDate, Date endDate );

    long findOrdersTotal();

    int findOrdersPending();

    List<Object[]> retrieveDailyEarnings(int currentYr, int currentMnt);

    List<Object[]> findTotalPricesByPayment();
    List<Object[]> retriveMontlyEarning(int currentYr);
}
