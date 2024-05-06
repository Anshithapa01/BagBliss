package com.ecommerce.admin.controller;

import com.ecommerce.library.dto.OrderDto;
import com.ecommerce.library.model.Order;
import com.ecommerce.library.model.OrderDetails;
import com.ecommerce.library.repository.OrderRepository;
import com.ecommerce.library.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class OrderDetailsControll {
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;


    @Autowired
    public OrderDetailsControll(OrderService orderService, OrderRepository orderRepository) {
        this.orderService = orderService;
        this.orderRepository = orderRepository;
    }

    @GetMapping("/orderDetails/{pageNo}")
    public String showOrderDetails(@PathVariable("pageNo") int pageNo, Model model) {


        List<Long> orderDetailIds = orderService.findAllOrder().stream()
                .map(OrderDetails::getOrder)
                .map(Order::getId)
                .distinct()
                .collect(Collectors.toList());

        // Retrieve orders whose IDs are present in the order details
        List<Order> filteredOrders = orderService.findOrderByPageble(pageNo, 10)
                .stream()
                .filter(order -> orderDetailIds.contains(order.getId()))
                .collect(Collectors.toList());

        // Create a PageImpl from the filtered orders
        Page<Order> orders = new PageImpl<>(filteredOrders);

        OrderDto orderDto = new OrderDto();
        model.addAttribute("report", orderDto);
        model.addAttribute("orders", orders);
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPage", orders.getTotalPages());
        return "orderDetails";
//        Page<Order> orders = orderService.findOrderByPageble(pageNo, 10);
//        List<OrderDetails> orderDetails=orderService.findAllOrder();
//        //List<Order>orders=orderService.findAll();
//        OrderDto orderDto=new OrderDto();
//        model.addAttribute("report",orderDto);
//        model.addAttribute("orderDetails",orderDetails);
//        model.addAttribute("orders", orders);
//        model.addAttribute("currentPage", pageNo);
//        model.addAttribute("totalPage", orders.getTotalPages());;
//        return "orderDetails";
    }

    @GetMapping("/orderDetailsInfo")
    public String showOrderDetaliInfo(@RequestParam("orderId") Long orderId, Model model) {
        List<OrderDetails> orderDetails = orderService.findByOrderId(orderId);
        Order order = orderService.findById(orderId);
        model.addAttribute("orderDetails", orderDetails);
        model.addAttribute("order", order);
        model.addAttribute("order1", order);
        return "orderDetail-info";
    }

    @PostMapping("/updateStatus")
    public String showUpdateOrderStaus(@ModelAttribute("order1") Order order) {
        orderService.updateOrderStatus(order);
        return "redirect:/orderDetails/0";
    }


    @GetMapping("/orderStatus/{pageNo}")
    public String showOrderStatus(@PathVariable("pageNo")int pageNo,
                                  @ModelAttribute("report")OrderDto orderDto1,Model model){
        String orderStatus=orderDto1.getOrderStatus();
        Page<Order> orders = orderService.findOrderByOrderStatusPagable(pageNo,orderStatus);
        //List<Order>orders=orderService.findAll();
        OrderDto orderDto=new OrderDto();
        model.addAttribute("report",orderDto);
        model.addAttribute("orders", orders);
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPage", orders.getTotalPages());
        return "orderDetails";
    }

    @GetMapping("/removeOrder")
    public String removeOrder(@RequestParam("orderId") Long orderId, Model model) {
        try {
            orderService.deleteOrderDetailsById(orderId);
        } catch (Exception e) {
           e.printStackTrace();
        }
        return "redirect:/orderDetails/0";
    }

}