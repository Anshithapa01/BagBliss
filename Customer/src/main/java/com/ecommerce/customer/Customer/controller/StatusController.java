package com.ecommerce.customer.Customer.controller;

import com.ecommerce.library.model.Order;
import com.ecommerce.library.service.OrderService;
import com.ecommerce.library.service.WalletService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class StatusController {


    OrderService orderService;
    WalletService walletService;

    public StatusController(OrderService orderService,
                            WalletService walletService){
        this.orderService=orderService;
        this.walletService=walletService;
    }


    @GetMapping("/returnOrder")
    public String showReturnOrder(@ModelAttribute("orderId") Long id){
        orderService.returnOrder(id);
        return "redirect:/orderDetails?pageNo=0";
    }

    @GetMapping("/cancelOrder")
    public String showCancelOrder(@ModelAttribute("orderId")Long id){
        Order order=orderService.findById(id);
        orderService.cancelOrder(id);
        if(order.getPaymentMethod().equals("online_payment") || order.getPaymentMethod().equals("wallet"))
        {
            walletService.addToRefundAmount(id);
        }
        return "redirect:/orderDetails?pageNo=0";
    }

}
