package com.ecommerce.library.service.impl;

import com.ecommerce.library.dto.CustomEarning;
import com.ecommerce.library.dto.DailyEarning;
import com.ecommerce.library.dto.WeeklyEarnings;
import com.ecommerce.library.dto.YearlyEarning;
import com.ecommerce.library.exception.OrderNotFoundException;
import com.ecommerce.library.model.*;
import com.ecommerce.library.repository.*;
import com.ecommerce.library.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
    private OrderRepository orderRepository;
    private OrderDetailsRepository orderDetailsRepository;
    private CustomerRepository customerRepository;
    private ShopingCartRepository shopingCartRepository;

    private ProductRepository productRepository;
    private AddressRepository addressRepository;



    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, OrderDetailsRepository orderDetailsRepository,
                            CustomerRepository customerRepository, ShopingCartRepository shopingCartRepository,
                            ProductRepository productRepository, AddressRepository addressRepository) {
        this.orderRepository = orderRepository;
        this.orderDetailsRepository = orderDetailsRepository;
        this.customerRepository = customerRepository;
        this.shopingCartRepository = shopingCartRepository;
        this.productRepository = productRepository;
        this.addressRepository = addressRepository;
    }

    @Override
    public Order saveOrder(ShoppingCart shopingCart, String email, Long addressId, String paymentMethod,
                           Double grandTotel,Double deduction,double total) {
        Order order = new Order();
        double discount=order.getDeduction()+deduction;
        int orderQuantity=0;
        order.setOrderDate(new Date());
        order.setOrderStatus("Pending");

        order.setCustomer(customerRepository.findByEmail(email));
        order.setGrandTotalPrize(grandTotel);
        order.setPaymentMethod(paymentMethod);
        if(total<1000&&grandTotel!=0){
            order.setShippingFee(50);
        }
        order.setShippingAddress(addressRepository.getReferenceById(addressId));
        List<ShoppingCart> shoppingCarts = shopingCartRepository.findShoppingCartByCustomer(email);
        for (ShoppingCart cart : shoppingCarts) {
            OrderDetails orderDetails = new OrderDetails();
            orderDetails.setProduct(cart.getProduct());
            orderDetails.setOrder(order);
            orderDetails.setQuantity(cart.getQuantity());
            orderQuantity+=cart.getQuantity();
            orderDetails.setUnitPrice(cart.getProduct().getSalePrice());
            orderDetails.setTotalPrice(cart.getTotalPrice());
            orderDetailsRepository.save(orderDetails);
            Product product = cart.getProduct();
            int quantity = product.getCurrentQuantity() - cart.getQuantity();
            product.setCurrentQuantity(quantity);
            double amt=product.getCostPrice()- product.getSalePrice();
            discount+=amt;
            productRepository.save(product);
            cart.setDeleted(true);
            shopingCartRepository.save(cart);
        }
        double roundedBalance = Math.round(discount * 100.0) / 100.0;
        order.setDeduction(roundedBalance);
        order.setQuantity(orderQuantity);
        orderRepository.save(order);
        return order;
    }

    @Override
    public void updateOrder(String paymentMethod, Long id){
        Order order = findOrderById(id);
        try {
            if (order == null) {
                System.out.println("Order not found");
            }
            assert order != null;
            order.setPaymentMethod(paymentMethod);
            orderRepository.save(order);
        }catch (NullPointerException e){
            e.printStackTrace();
        }
    }

    @Override
    public List<OrderDetails> findAllOrder() {
        return orderDetailsRepository.findAllOrder();
    }

    @Override
    public List<OrderDetails> findOrderDetailsByCustomer(String email) {
        return orderDetailsRepository.findOrderDetailsByCustomer(email);
    }

    @Override
    public List<OrderDetails> findByOrderId(Long orderId) {
        return orderDetailsRepository.findByOrderId(orderId);
    }

    @Override
    public Order findById(long id) {
        return orderRepository.getReferenceById(id);
    }

    @Override
    public void updateOrderStatus(Order order) {

        Order order1 = orderRepository.getReferenceById(order.getId());

        order1.setOrderStatus(order.getOrderStatus());
        orderRepository.save(order1);
        if(order.getOrderStatus().equals("Return Accept")){
            List<OrderDetails> orderDetails=orderDetailsRepository.findByOrderId(order.getId());
            for(OrderDetails orders:orderDetails){
                Long productId=orders.getProduct().getId();
                Product product=productRepository.getReferenceById(productId);
                product.setCurrentQuantity(product.getCurrentQuantity()+orders.getQuantity());
                productRepository.save(product);
            }

        }


    }

    @Override
    public List<Order> findAll() {
        return orderRepository.findAllByDate();
    }

    @Override
    public void cancelOrder(Long id,String reason) {

        Order order = orderRepository.getReferenceById(id);
        order.setOrderStatus("Cancel");
        order.setReason(reason);

        orderRepository.save(order);
        List<OrderDetails> orderDetails=orderDetailsRepository.findByOrderId(id);
        for(OrderDetails orders:orderDetails){
            Long productId=orders.getProduct().getId();
            Product product=productRepository.getReferenceById(productId);
            product.setCurrentQuantity(product.getCurrentQuantity()+orders.getQuantity());
            productRepository.save(product);
        }
    }

    @Override
    public void returnOrder(Long id,String reason) {
        Order order = orderRepository.getReferenceById(id);
        order.setOrderStatus("Return Pending");
        order.setReason(reason);
        orderRepository.save(order);
    }

    @Override
    public List<Order> findOrderByCustomer(String email) {
       // Pageable pageable=PageRequest.of(pageNo,6);
       // Page<Order> orders=this.orderRepository.findOrderByCustomer(email,pageable);
        return orderRepository.findOrderByCustomer(email);
    }

    @Override
    public Order findOrderById(Long id) {
        return orderRepository.findById(id).orElse(null);
    }

    @Override
    public Page<Order> findOrderByCustomerPagable(int pageNo, String email) {
        Pageable pageable=PageRequest.of(pageNo,6);
        Page<Order> orders=this.orderRepository.findOrderByCustomerPagable(pageable,email);
        return orders;
    }

    @Override
    public Page<Order> findOrderByOrderStatusPagable(int pageNo, String status) {
        Pageable pageable=PageRequest.of(pageNo,6);
        Page<Order> orders=this.orderRepository.findOrderByOrderStatusPagable(pageable,status);
        return orders;
    }

    @Override
    public List<Order> getDailyOrders(LocalDate date) {
        LocalDate startOfDay = date.atStartOfDay().toLocalDate();
        LocalDate endOfDay = startOfDay.plusDays(1);
        return orderRepository.findByOrderDateBetween(startOfDay, endOfDay);
    }

    @Override
    public Page<Order> findOrderByPageble(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Order> orders = this.orderRepository.findOrderByPagable(pageable);
        return orders;
    }


    @Override
    public void deleteOrderDetailsById(Long id) {
        orderDetailsRepository.deleteOrderDetailsById(id);
    }

    @Override
    public List<Order> findOrdersByAddressId(Long addressId) {
        return orderRepository.findByShippingAddressId(addressId);
    }

    @Override
    public List<Long> findOrderIdsByAddressId(Long addressId) {
        List<Order> orders = orderRepository.findByShippingAddressId(addressId);
        return orders.stream().map(Order::getId).collect(Collectors.toList());
    }

    @Override
    public List<Long> findOrderIdsByShippingAddressId(Long addressId) {
        List<Order> orders = orderRepository.findByShippingAddressId(addressId);
        return orders.stream().map(Order::getId).collect(Collectors.toList());
    }

    @Override
        public Page<OrderDetails> findOrderDetailsByCustomerPageable(int pageNo, String username, int pageSize) {
            Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by("order.id").descending());
            return orderDetailsRepository.findByOrder_Customer_Email(username, pageable);
        }

    @Override
    public boolean hasOrdersForAddress(Long addressId) {
        return orderRepository.existsByShippingAddressId(addressId);
    }


//    @Override
//    public List<Order> getDailyReport(Date date) {
//        return orderRepository.findDailyOrders(date);
//    }


//    @Override
//    public List<DailyEarning> dailyReport(int year, int month) {
//        List<Object[]> result=orderRepository.dailyReport(year,month);
//        List<DailyEarning> report=new ArrayList<>();
//        for(Object[] row:result){
//            Date date= (Date) row[0];
//            double newBalance1 =(Double) row[1];
//            Double grandTotal = Math.round(newBalance1 * 100.0) / 100.0;
//            Long totalOrder= (Long) row[2];
//            double newBalance2 =(Double) row[3];
//            Double deduction = Math.round(newBalance2 * 100.0) / 100.0;
//            Long deliveredOrders = (Long) row[4];
//            Long cancelledOrders = (Long) row[5];
//            report.add(new DailyEarning(date,grandTotal,totalOrder,deduction,deliveredOrders,cancelledOrders));
//        }
//
//        return report;
//    }

    @Override
    public List<DailyEarning> getCurrentDayOrders() {
        List<Object[]> result = orderRepository.currentDayOrders();
        List<DailyEarning> orders = new ArrayList<>();

        for (Object[] row : result) {
            Long orderId = (Long) row[0];
            Long productId = ((Long) row[1]);
            String productName = (String) row[2];
            String description = (String) row[3];
            Double unitPrice = (Double) row[4];
            Integer quantity = (Integer) row[5];
            Double total = (Double) row[6];
            Double deduction = (Double) row[7];
            Double shippingFee = (Double) row[8];
            Double totalAmount = (Double) row[9];

            orders.add(new DailyEarning(orderId, productId, productName, description, unitPrice, quantity, total, deduction, shippingFee, totalAmount));
        }
        return orders;
    }


//    @Override
//    public List<WeeklyEarnings> findWeeklyEarnings(int year) {
//        List<Object[]> result=orderRepository.weeklyEarnings(year);
//        List<WeeklyEarnings> report=new ArrayList<>();
//        for (Object[] row:result){
//            Date date= (Date) row[0];
//            double newBalance1 =(Double) row[1];
//            Double earnings = Math.round(newBalance1 * 100.0) / 100.0;
//            Long totalOrders = (Long) row[2];
//            double newBalance2 =(Double) row[3];
//            Double deduction = Math.round(newBalance2 * 100.0) / 100.0;
//            Long deliveredOrders = (Long) row[4];
//            Long cancelledOrders = (Long) row[5];
//            report.add(new WeeklyEarnings(date, earnings, totalOrders, deduction, deliveredOrders, cancelledOrders));
//        }
//        return report;
//    }

    @Override
    public List<WeeklyEarnings> getLastWeekOrders() {
        List<Object[]> results = orderRepository.lastWeekOrders();
        List<WeeklyEarnings> orders = new ArrayList<>();

        for (Object[] result : results) {
            WeeklyEarnings order = new WeeklyEarnings();
            order.setOrderDate((Date) result[0]);
            order.setOrderId(((Long) result[1]));
            order.setProductId(((Long) result[2]));
            order.setProductName((String) result[3]);
            order.setDescription((String) result[4]);
            order.setUnitPrice((Double) result[5]);
            order.setQuantity((Integer) result[6]);
            order.setTotalPrice((Double) result[7]);
            order.setDeduction((Double) result[8]);
            order.setShippingFee((Double) result[9]);
            order.setTotalAmount((Double) result[10]);
            orders.add(order);
        }
        return orders;
    }


    @Override
    public List<YearlyEarning> getYearlyOrders() {
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        List<Object[]> results = orderRepository.findYearlyOrdersWithCategory(currentYear);
        List<YearlyEarning> orders = new ArrayList<>();

        for (Object[] result : results) {
            YearlyEarning order = new YearlyEarning();
            order.setCategoryName((String) result[0]);
            order.setProductName((String) result[1]);
            order.setOrderedQuantity((Long) result[2]);
            order.setOrderedTotalPrice((Double) result[3]);
            orders.add(order);
        }

        return orders;
    }


//    @Override
//    public List<YearlyEarning> getYearlyReport(int year) {
//        List<Object[]> result = orderRepository.yearlyReport(year);
//        List<YearlyEarning> report = new ArrayList<>();
//        for (Object[] row : result) {
//            Date yearDate = (Date) row[0];
//            double newBalance1 =(Double) row[1];
//            Double totalEarnings = Math.round(newBalance1 * 100.0) / 100.0;
//            Long totalOrders = (Long) row[2];
//            double newBalance2 =(Double) row[3];
//            Double deduction  = Math.round(newBalance2 * 100.0) / 100.0;
//            Long deliveredOrders = (Long) row[4];
//            Long cancelledOrders = (Long) row[5];
//            report.add(new YearlyEarning(yearDate, totalEarnings, totalOrders, deduction, deliveredOrders, cancelledOrders));
//        }
//        return report;
//    }

//    @Override
//    public List<CustomEarning> getCustomReport(Date startDate, Date endDate,Date parsedEndDate) {
//        List<Object[]> result = orderRepository.findByOrderDatesBetween(startDate, endDate);
//        List<CustomEarning> report = new ArrayList<>();
//        for (Object[] row : result) {
//            double newBalance1 =(Double) row[0];
//            Double totalEarnings = Math.round(newBalance1 * 100.0) / 100.0;
//            Long totalOrders = (Long) row[1];
//            double newBalance2 =(Double) row[2];
//            Double deduction  = Math.round(newBalance2 * 100.0) / 100.0;
//            Long deliveredOrders = (Long) row[3];
//            Long cancelledOrders = (Long) row[4];
//            report.add(new CustomEarning(startDate,parsedEndDate, totalEarnings, totalOrders, deduction, deliveredOrders, cancelledOrders));
//        }
//        return report;
//    }

    @Override
    public List<CustomEarning> getOrdersWithinDateRange(Date startDate, Date endDate) {
        List<Object[]> results = orderRepository.findOrdersWithinDateRange(startDate, endDate);
        return results.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Override
    public CustomEarning mapToDTO(Object[] result) {
        CustomEarning dto = new CustomEarning();
        dto.setOrderDate((Date) result[0]);
        dto.setOrderId((Long) result[1]);
        dto.setProductId((Long) result[2]);
        dto.setProductName((String) result[3]);
        dto.setProductDescription((String) result[4]);
        dto.setUnitPrice((Double) result[5]);
        dto.setQuantity((Integer) result[6]);
        dto.setTotalPrice((Double) result[7]);
        dto.setDeduction((Double) result[8]);
        dto.setShippingFee((Double) result[9]);
        dto.setTotalAmount((Double) result[10]);
        return dto;
    }

    @Override
    public void setReason(Long orderId, String reason) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));
        order.setReason(reason);
        orderRepository.save(order);
    }

}












