package com.ecommerce.customer.Customer.controller;

import com.ecommerce.library.model.Address;
import com.ecommerce.library.model.Customer;
import com.ecommerce.library.model.Order;
import com.ecommerce.library.model.OrderDetails;
import com.ecommerce.library.service.CustomerService;
import com.ecommerce.library.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
public class InvoiceController {

    OrderService orderService;

    CustomerService customerService;
    @Autowired
    public InvoiceController(OrderService orderService,CustomerService customerService){
        this.orderService=orderService;
        this.customerService=customerService;
    }

    @GetMapping("/invoice/{id}")
    public String invoiceDownload(@PathVariable("id")Long id, Model model,
                                  Principal principal){
        String username=principal.getName();
        Customer customer=customerService.findByEmail(username);
        Order orders = orderService.findOrderById(id);
        List<OrderDetails> orderDetails = orderService.findByOrderId(id);
        Address address=orders.getShippingAddress();

        Date orderDate1=orders.getOrderDate();
        SimpleDateFormat desiredFormat = new SimpleDateFormat("MMM dd, yyyy - hh:mm a");
        String orderDate = desiredFormat.format(orderDate1);

        Date date1 = new Date();
        SimpleDateFormat format2 = new SimpleDateFormat("EEE, MMM dd, yyyy");
        String date = format2.format(date1);

        double subtotal1=orders.getGrandTotalPrize()-orders.getShippingFee();
        double subtotal2=subtotal1+orders.getDeduction();
        Double subtotal=Math.round(subtotal2 * 100.0) / 100.0;
        model.addAttribute("orders", orders);
        model.addAttribute("orderDetails", orderDetails);
        model.addAttribute("customer", customer);
        model.addAttribute("order_date", orderDate);
        model.addAttribute("date", date);
        model.addAttribute("subtotal", subtotal);
        model.addAttribute("address", address);

        return "invoice";
    }
}
