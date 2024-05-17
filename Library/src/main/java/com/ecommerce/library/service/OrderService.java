package com.ecommerce.library.service;

import com.ecommerce.library.dto.CustomEarning;
import com.ecommerce.library.dto.DailyEarning;
import com.ecommerce.library.dto.WeeklyEarnings;
import com.ecommerce.library.dto.YearlyEarning;
import com.ecommerce.library.model.Order;
import com.ecommerce.library.model.OrderDetails;
import com.ecommerce.library.model.ShoppingCart;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface OrderService {
    Order saveOrder(ShoppingCart shopingCart, String email, Long addressId,
                    String paymentMethod, Double grandTotel,Double deduction,double total);

    void updateOrder(String paymentMethod, Long id);

    List<OrderDetails> findAllOrder();

    List<OrderDetails> findOrderDetailsByCustomer(String email);

    List<OrderDetails> findByOrderId(Long orderid);

    Order findById(long id);

    void updateOrderStatus(Order order);

    List<Order> findAll();

    void cancelOrder(Long id);

    void returnOrder(Long id);
    List<Order> findOrderByCustomer(String email);

    public List<Order> getDailyOrders(LocalDate date);

    Page<Order> findOrderByPageble(int page,int size);

    Order findOrderById(Long id);

    Page<Order> findOrderByCustomerPagable(int pageNo, String email);
    Page<Order> findOrderByOrderStatusPagable(int pageNo,String status);
//    List<Order> getDailyReport(Date date);

    void deleteOrderDetailsById(Long id);

    List<Order> findOrdersByAddressId(Long addressId);

    List<Long> findOrderIdsByAddressId(Long addressId);


    List<Long> findOrderIdsByShippingAddressId(Long addressId);

    Page<OrderDetails> findOrderDetailsByCustomerPageable(int pageNo, String username, int pageSize);

    boolean hasOrdersForAddress(Long addressId);

    List<DailyEarning> dailyReport(int year, int month);
    List<WeeklyEarnings> findWeeklyEarnings(int year);

    List<YearlyEarning> getYearlyReport(int year);

    List<CustomEarning> getCustomReport(Date startDate, Date endDate,Date parsedEndDate);
}
