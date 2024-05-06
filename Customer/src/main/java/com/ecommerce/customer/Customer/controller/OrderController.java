package com.ecommerce.customer.Customer.controller;

import com.ecommerce.library.model.Order;
import com.ecommerce.library.model.OrderDetails;
import com.ecommerce.library.model.ShoppingCart;
import com.ecommerce.library.repository.ShopingCartRepository;
import com.ecommerce.library.service.AddressService;
import com.ecommerce.library.service.OrderService;
import com.ecommerce.library.service.ShoppingCartService;
import com.lowagie.text.DocumentException;
import jakarta.servlet.http.HttpServletResponse;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
public class OrderController {

    @Autowired
    OrderService orderService;
    AddressService addressService;
    ShoppingCartService shopCartService;

    @Autowired
    public OrderController(OrderService orderService, AddressService addressService,
                           ShoppingCartService shopCartService) {
        this.orderService = orderService;
        this.addressService = addressService;
        this.shopCartService = shopCartService;
    }


    @GetMapping("/orderConfirm")
    public String showOrderConfirm(){
        return "order-confirm";
    }

//    @GetMapping("/order")
//    public String showOrder(@RequestParam("pageNo")int pageNo, Model model, Principal principal) {
//        if (principal == null) {
//            return "redirect:/login";
//        }
//        String username = principal.getName();
//        Page<Order> orders = orderService.findOrderByCustomerPagable(pageNo,username);
//        model.addAttribute("orders", orders);
//       model.addAttribute("currentPage", pageNo);
//       model.addAttribute("totalPage", orders.getTotalPages());
//
//        return "orders";
//    }
//
//    @GetMapping("/orderDetails")
//    public String showOrderDetails(@RequestParam("orderId") Long id, Model model) {
//        List<OrderDetails> orderDetails = orderService.findByOrderId(id);
//        model.addAttribute("orderDetails", orderDetails);
//        return "order-details";
//    }

    @GetMapping("/order")
    public String showOrder(@RequestParam("orderId") Long id, Model model,Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        String username = principal.getName();
        Order orders = orderService.findOrderById(id);
        model.addAttribute("orders", orders);
        return "orders";
    }

    @GetMapping("/orderDetails")
    public String showOrderDetails(@RequestParam("pageNo") int pageNo, Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        int pageSize = 10;
        String username = principal.getName();
        Page<OrderDetails> orderDetailsPage = orderService.findOrderDetailsByCustomerPageable(pageNo, username,pageSize);
        model.addAttribute("orderDetailsPage", orderDetailsPage);
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", orderDetailsPage.getTotalPages());
        return "order-details";
    }



    @PostMapping("/createPayment")
    @ResponseBody
    public String showOnlinePayment(Principal principal, Authentication authentication,
                                    @RequestBody Map<String, Object> data){

//        CustomUser customUser= (CustomUser) authentication.getPrincipal();
//        Long id=customUser.getCustomer_id();
        String username=principal.getName();
        String paymentMethod = data.get("paymentMethod").toString();
        Long address_id=Long.parseLong(data.get("addressId").toString());
        Double amount= Double.valueOf(data.get("amount").toString());

        System.out.println(amount);


        ShoppingCart shoppingCart = new ShoppingCart();
        orderService.saveOrder(shoppingCart, username, address_id,paymentMethod,amount);
        JSONObject option=new JSONObject();
        option.put("status","cash");
        return option.toString();

    }

    @PostMapping("/verify-payment")
    @ResponseBody
    public String showVerifyPayment(@RequestBody Map<String,Object> data){

        return "done";
    }
    @GetMapping("/orderListPdf1")
    public void generatePdf(HttpServletResponse response, Principal principal) throws DocumentException, IOException {

        String email=principal.getName();
        response.setContentType("application/pdf");
        DateFormat dateFormat = new SimpleDateFormat("YYYY-MM-DD:HH:MM:SS");
        String currentDateTime = dateFormat.format(new Date());
        String headerkey = "Content-Disposition";
        String headervalue = "attachment; filename=pdf_" + currentDateTime + ".pdf";
        response.setHeader(headerkey, headervalue);

       // List<Order> list=orderService.findOrderByCustomer(email);

//        PdfGenerator pdfGenerator=new PdfGenerator();
//       // pdfGenerator.setOrders(list);
//        pdfGenerator.generate(response);
    }


}
