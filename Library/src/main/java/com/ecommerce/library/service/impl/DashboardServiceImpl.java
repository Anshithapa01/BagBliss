package com.ecommerce.library.service.impl;

import com.ecommerce.library.model.Order;
import com.ecommerce.library.repository.OrderRepository;
import com.ecommerce.library.service.DashBoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class DashboardServiceImpl implements DashBoardService {

    private OrderRepository orderRepository;

    @Autowired
    public DashboardServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public double findCurrentMonthOrder(Date startDate, Date endDate) {
        List<Order> orderList=orderRepository.findByOrderDateBetween(startDate,endDate);
        double ordersTotalPrice=0;
        if(!orderList.isEmpty()){
            for(Order orders:orderList) {
                ordersTotalPrice = Double.parseDouble(String.format("%.2f",(ordersTotalPrice + orders.getGrandTotalPrize())));
            }
        }
        return ordersTotalPrice;
    }

    @Override
    public long findOrdersTotal() {
        return orderRepository.count();
    }

    @Override
    public int findOrdersPending() {
        return orderRepository.countByIsAcceptIsFalse();
    }



    @Override
    public List<Object[]> retrieveDailyEarnings(int currentYr, int currentMnt) {
        return orderRepository.dailyEarnings(currentYr,currentMnt);
    }
    @Override
    public List<Object[]> retriveMontlyEarning(int currentYr){
        return orderRepository.monthlyEarnings(currentYr);
    }

    @Override
    public List<Object[]> findTotalPricesByPayment() {
        return orderRepository.findTotalPricesByPaymentMethod();
    }
}
