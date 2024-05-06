package com.ecommerce.library.service.impl;

import com.ecommerce.library.dto.DailyEarning;
import com.ecommerce.library.dto.Monthlyearning;
import com.ecommerce.library.dto.WeeklyEarnings;
import com.ecommerce.library.model.*;
import com.ecommerce.library.repository.*;
import com.ecommerce.library.service.AddressService;
import com.ecommerce.library.service.OrderService;
import com.ecommerce.library.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
    private OrderRepository orderRepository;
    private OrderDetailsRepository orderDetailsRepository;
    private CustomerRepository customerRepository;
    private ShopingCartRepository shopingCartRepository;

    private AddressService addressService;

    private ShoppingCartService shopCartService;
    private ProductRepository productRepository;
    private AddressRepository addressRepository;



    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, OrderDetailsRepository orderDetailsRepository,
                            CustomerRepository customerRepository, ShopingCartRepository shopingCartRepository,
                            AddressService addressService, ShoppingCartService shopCartService,
                            ProductRepository productRepository, AddressRepository addressRepository) {
        this.orderRepository = orderRepository;
        this.orderDetailsRepository = orderDetailsRepository;
        this.customerRepository = customerRepository;
        this.shopingCartRepository = shopingCartRepository;
        this.addressService = addressService;
        this.shopCartService = shopCartService;
        this.productRepository = productRepository;
        this.addressRepository = addressRepository;
    }

    @Override
    public Order saveOrder(ShoppingCart shopingCart, String email, Long addressId, String paymentMethod,
                           Double grandTotel) {

        Order order = new Order();
        order.setOrderDate(new Date());
        order.setOrderStatus("Pending");

        order.setCustomer(customerRepository.findByEmail(email));
        order.setGrandTotalPrize(grandTotel);
        order.setPaymentMethod(paymentMethod);

        order.setShippingAddress(addressRepository.getReferenceById(addressId));
        orderRepository.save(order);
        List<ShoppingCart> shoppingCarts = shopingCartRepository.findShoppingCartByCustomer(email);
        for (ShoppingCart cart : shoppingCarts) {
            OrderDetails orderDetails = new OrderDetails();
            orderDetails.setProduct(cart.getProduct());
            orderDetails.setOrder(order);
            orderDetails.setQuantity(cart.getQuantity());
            orderDetails.setUnitPrice(cart.getUnitPrice());
            orderDetails.setTotalPrice(cart.getTotalPrice());
            orderDetailsRepository.save(orderDetails);
            Product product = cart.getProduct();
            int quantity = product.getCurrentQuantity() - cart.getQuantity();
            product.setCurrentQuantity(quantity);
            productRepository.save(product);
            cart.setDeleted(true);
            shopingCartRepository.save(cart);
        }

        return order;
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
    public void cancelOrder(Long id) {

        Order order = orderRepository.getReferenceById(id);
        order.setOrderStatus("Cancel");

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
    public void returnOrder(Long id) {
        Order order = orderRepository.getReferenceById(id);
        order.setOrderStatus("Return Pending");
        orderRepository.save(order);
    }

//    @Override
//    public List<Order> findOrderByCustomer(String email) {
//       // Pageable pageable=PageRequest.of(pageNo,6);
//       // Page<Order> orders=this.orderRepository.findOrderByCustomer(email,pageable);
//        return orderRepository.findOrderByCustomer(email);
//    }

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
            Pageable pageable = PageRequest.of(pageNo, pageSize);
            return orderDetailsRepository.findByOrder_Customer_Email(username, pageable);
        }

    @Override
    public boolean hasOrdersForAddress(Long addressId) {
        return orderRepository.existsByShippingAddressId(addressId);
    }


    @Override
    public List<Order> getDailyReport(Date date) {
        // Call the repository method to get daily orders
        return orderRepository.findDailyOrders(date);
    }

    @Override
    public List<Monthlyearning> getMonthlyReport(int year) {
        List<Object[]> result=orderRepository.monthlyReport(year);
        List<Monthlyearning> report=new ArrayList<>();
        for(Object[] row:result){
            Date month= (Date) row[0];
            Double grandTotel= (Double) row[1];
            Long totelOrder= (Long) row[2];
            Long delivered_orders= (Long) row[3];
            Long cancelled_orders= (Long) row[4];
            report.add(new Monthlyearning(month,grandTotel,totelOrder,delivered_orders,cancelled_orders));
        }
        return report;

    }

    @Override
    public List<DailyEarning> dailyReport(int year, int month) {
        List<Object[]> result=orderRepository.dailyReport(year,month);
        List<DailyEarning> report=new ArrayList<>();
        for(Object[] row:result){
            Date date= (Date) row[0];
            Double grandTotel= (Double) row[1];
            Long totelOrder= (Long) row[2];
            report.add(new DailyEarning(date,grandTotel,totelOrder));
        }

        return report;
    }

    @Override
    public List<WeeklyEarnings> findWeeklyEarnings(int year) {
        List<Object[]> result=orderRepository.weeklyEarnings(year);
        List<WeeklyEarnings> report=new ArrayList<>();
        for (Object[] row:result){
            Date date= (Date) row[0];
            Double earnings=(Double)  row[1];
            report.add(new WeeklyEarnings(date,earnings));

        }
        return report;
    }
}












