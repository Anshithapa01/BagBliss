package com.ecommerce.library.repository;

import com.ecommerce.library.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.util.Date;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {


   // @Query(value="SELECT o.*,od.* FROM orders o JOIN order_details od ON o.order_id=od.order_id",nativeQuery=true)
  //  List<Order> findAllOrder();
    @Query("select o from Order o order by o.id desc")
    List<Order> findAllByDate();
    @Query("select o from Order o where o.customer.email=?1 order by o.orderDate desc ")
    List<Order> findOrderByCustomer(String email);

    @Query("select o from Order o where o.customer.email=?1 order by o.orderDate desc ")
    Page<Order> findOrderByCustomerPagable(Pageable pageable,String email);

    @Query("select o from Order o where o.orderStatus=?1 order by o.orderDate desc ")
    Page<Order> findOrderByOrderStatusPagable(Pageable pageable,String orderStatus);

    List<Order> findByOrderDateBetween(LocalDate startDate, LocalDate endDate);
@Query("select o from Order o order by o.orderDate desc")
    Page<Order> findOrderByPagable(Pageable pageable);

    @Query(value = "SELECT SUM(o.grand_totel_prize) FROM Order o WHERE MONTH(o.order_date)=:month AND YEAR(o.order_date)=:year",nativeQuery = true)
    int totalPrice(YearMonth year, Month month);

    List<Order> findByOrderDateBetween(Date startDate, Date endDate);

    int countByIsAcceptIsFalse();


    @Query("SELECT o.paymentMethod, SUM(o.grandTotalPrize) FROM Order o WHERE o.orderStatus='Delivered' AND o.paymentMethod IN ('online_payment', 'cash_on_Delivery', 'wallet') GROUP BY o.paymentMethod")
    List<Object[]> findTotalPricesByPaymentMethod();




    @Query("SELECT o FROM Order o WHERE DATE(o.orderDate) = :date")
    List<Order> findDailyOrders(@Param("date") Date date);



    @Query("SELECT o FROM Order o WHERE o.shippingAddress.id = :addressId")
    List<Order> findByShippingAddressId(@Param("addressId") Long addressId);


    boolean existsByShippingAddressId(Long addressId);



}
