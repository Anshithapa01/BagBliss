package com.ecommerce.customer.Customer.controller;

import com.ecommerce.library.model.Order;
import com.ecommerce.library.service.OrderService;
import com.ecommerce.library.service.WalletService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class StatusController {


    OrderService orderService;
    WalletService walletService;

    public StatusController(OrderService orderService,
                            WalletService walletService){
        this.orderService=orderService;
        this.walletService=walletService;
    }


    @PostMapping("/returnOrder")
    public String showReturnOrder(@RequestParam("orderId") Long id, @RequestParam("reason") String reason){
        Order order=orderService.findById(id);
        orderService.returnOrder(id,reason);
        if(order.getPaymentMethod().equals("online_payment") || order.getPaymentMethod().equals("wallet"))
        {
            walletService.addToRefundAmount(id);
        }
        return "redirect:/orderDetails?pageNo=0";
    }

    @PostMapping("/cancelOrder")
    public String showCancelOrder(@RequestParam("orderId") Long id, @RequestParam("reason") String reason){
        Order order=orderService.findById(id);
        orderService.cancelOrder(id,reason);
        if(order.getPaymentMethod().equals("online_payment") || order.getPaymentMethod().equals("wallet"))
        {
            walletService.addToRefundAmount(id);
        }
        return "redirect:/orderDetails?pageNo=0";
    }

}
