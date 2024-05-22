package com.ecommerce.customer.Customer.controller;

import com.ecommerce.customer.Customer.config.CustomerDetails;
import com.ecommerce.library.model.Order;
import com.ecommerce.library.model.ShoppingCart;
import com.ecommerce.library.model.Wallet;
import com.ecommerce.library.service.OrderService;
import com.ecommerce.library.service.WalletService;
import com.ecommerce.library.utils.PdfGenerator;
import com.lowagie.text.DocumentException;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.security.Principal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@Controller
public class PaymentController {

    OrderService orderService;

    WalletService walletService;

    @Autowired
    public PaymentController(OrderService orderService,
                             WalletService walletService){
    this.orderService=orderService;
    this.walletService=walletService;

    }


//    @PostMapping("/createPayment")
//    @ResponseBody
//    public String showOnlinePayment(Principal principal, Authentication authentication,
//                                    @RequestBody Map<String, Object> data) throws RazorpayException {
//        CustomerDetails customUser= (CustomerDetails) authentication.getPrincipal();
////        Long id=customUser.getCustomer_id();
//        String username=principal.getName();
//        String paymentMethod = data.get("paymentMethod").toString();
//        Long address_id=Long.parseLong(data.get("addressId").toString());
//        Double amount= Double.valueOf(data.get("amount").toString());
//        Double deduction=Double.valueOf(data.get("deduction").toString());
//
//
//        if(paymentMethod.equals("online_payment")) {
//            ShoppingCart shopingCart = new ShoppingCart();
//            orderService.saveOrder(shopingCart, username, address_id,paymentMethod,amount,deduction);
//            var client = new RazorpayClient("rzp_test_y8yagsN9W3YyKJ", "cX3rnwciccdWXuT1JUvQ8ywd");
//            JSONObject object = new JSONObject();
//            object.put("amount", amount * 100);
//            object.put("currency", "INR");
//            object.put("receipt", "receipt#1");
//            com.razorpay.Order order = client.orders.create(object);
//            System.out.println(order);
//            System.out.println(paymentMethod);
//            System.out.println(address_id);
//            return order.toString();
//
//        }
//        if(paymentMethod.equals("wallet")){
//            Wallet wallet=walletService.findByCustomerByUsername(username);
//            if(wallet.getBalance()<amount){
//                JSONObject option=new JSONObject();
//                option.put("status","noWallet");
//                return option.toString();
//            }
//            else{
//                ShoppingCart shopingCart = new ShoppingCart();
//                orderService.saveOrder(shopingCart, username, address_id,paymentMethod,amount,deduction);
//                walletService.setWalletAmount(username,amount);
//                JSONObject option=new JSONObject();
//                option.put("status","wallet");
//                return option.toString();
//            }
//
//        }
//        else{
//            ShoppingCart shopingCart = new ShoppingCart();
//            orderService.saveOrder(shopingCart, username, address_id,paymentMethod,amount,deduction);
//
//            JSONObject option=new JSONObject();
//            option.put("status","cash");
//            return option.toString();
//        }
//    }


    @PostMapping("/createPayment")
    @ResponseBody
    public String showOnlinePayment(Principal principal, Authentication authentication,
                                    @RequestBody Map<String, Object> data) throws RazorpayException {
        CustomerDetails customUser = (CustomerDetails) authentication.getPrincipal();
        String username = principal.getName();
        String paymentMethod = data.get("paymentMethod").toString();
        Long address_id = Long.parseLong(data.get("addressId").toString());
        Double amount = Double.valueOf(data.get("amount").toString());
        Double deduction = Double.valueOf(data.get("deduction").toString());
        Double total=Double.parseDouble(data.get("total").toString());

        if (paymentMethod.equals("online_payment")) {
            var client = new RazorpayClient("rzp_test_y8yagsN9W3YyKJ", "cX3rnwciccdWXuT1JUvQ8ywd");
            JSONObject object = new JSONObject();
            object.put("amount", (Object) (amount * 100)); // Explicitly cast to Object
            object.put("currency", "INR");
            object.put("receipt", "receipt#1");
            com.razorpay.Order order = client.orders.create(object);
            JSONObject response = new JSONObject();
            response.put("status", "created");
            response.put("id", (Object) order.get("id"));
            response.put("amount", (Object) order.get("amount"));
            return response.toString();
        } else if (paymentMethod.equals("wallet")) {
            Wallet wallet = walletService.findByCustomerByUsername(username);
            if (wallet.getBalance() < amount) {
                JSONObject option = new JSONObject();
                option.put("status", "noWallet");
                return option.toString();
            } else {
                ShoppingCart shoppingCart = new ShoppingCart();
                System.out.println(paymentMethod);
                orderService.saveOrder(shoppingCart, username, address_id, paymentMethod, amount, deduction,total);
                walletService.setWalletAmount(username, amount);
                JSONObject option = new JSONObject();
                option.put("status", "wallet");
                return option.toString();
            }
        } else {
            if(amount>1000)
            {
                JSONObject option = new JSONObject();
                option.put("status", "noCash");
                return option.toString();
            }else {
                ShoppingCart shoppingCart = new ShoppingCart();
                orderService.saveOrder(shoppingCart, username, address_id, paymentMethod, amount, deduction, total);
                JSONObject option = new JSONObject();
                option.put("status", "cash");
                return option.toString();
            }
        }
    }

    @PostMapping("/saveOrder")
    @ResponseBody
    public String saveOrder(@RequestBody Map<String, Object> data, Principal principal, Authentication authentication) {
        CustomerDetails customUser = (CustomerDetails) authentication.getPrincipal();
        String username = principal.getName();
        String paymentMethod = data.get("paymentMethod").toString();
        Long address_id = Long.parseLong(data.get("addressId").toString());
        Double amount = Double.valueOf(data.get("amount").toString());
        Double deduction = Double.valueOf(data.get("deduction").toString());
        Double total=Double.parseDouble(data.get("total").toString());

        ShoppingCart shoppingCart = new ShoppingCart();
        orderService.saveOrder(shoppingCart, username, address_id, paymentMethod, amount, deduction,total);
        JSONObject response = new JSONObject();
        response.put("status", "success");
        return response.toString();
    }

    @PostMapping("/pendingPayment")
    @ResponseBody
    public String pendingPayment(@RequestBody Map<String, Object> data, Principal principal, Authentication authentication) {
        CustomerDetails customUser = (CustomerDetails) authentication.getPrincipal();
        String username = principal.getName();
        String paymentMethod = "Payment Pending";
        Double total=Double.parseDouble(data.get("total").toString());
        Long address_id = Long.parseLong(data.get("addressId").toString());
        Double amount = Double.valueOf(data.get("amount").toString());
        Double deduction = Double.valueOf(data.get("deduction").toString());

        ShoppingCart shoppingCart = new ShoppingCart();
        System.out.println(shoppingCart+", "+username+", "+address_id+", "+paymentMethod+", "+amount+", "+deduction+", "+total);
        orderService.saveOrder(shoppingCart, username, address_id, paymentMethod, amount, deduction,total);
        JSONObject response = new JSONObject();
        response.put("status", "pending");
        return response.toString();
    }

    @PostMapping("/updateOrder")
    public ResponseEntity<String> updateOrder(@RequestBody Map<String, Object> data) {
        Long orderId = Long.parseLong(data.get("id").toString());
        String paymentMethod = data.get("paymentMethod").toString();
        orderService.updateOrder(paymentMethod,orderId);

        JSONObject response = new JSONObject();
        response.put("status", "success");
        response.put("message", "Order updated successfully");

        return ResponseEntity.ok(response.toString());
    }


}
