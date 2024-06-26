package com.ecommerce.library.repository;

import com.ecommerce.library.dto.CustomEarning;
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

    List<Order> findByOrderDateBetween(Date startDate, Date endDate);
@Query("select o from Order o order by o.orderDate desc")
    Page<Order> findOrderByPagable(Pageable pageable);

    @Query(value = "SELECT SUM(o.grand_totel_prize) FROM Order o WHERE MONTH(o.order_date)=:month AND YEAR(o.order_date)=:year",nativeQuery = true)
    int totalPrice(YearMonth year, Month month);

    int countByIsAcceptIsFalse();


    @Query("SELECT o.paymentMethod, SUM(o.grandTotalPrize) FROM Order o WHERE o.orderStatus='Delivered' AND o.paymentMethod IN ('online_payment', 'cash_on_Delivery', 'wallet') GROUP BY o.paymentMethod")
    List<Object[]> findTotalPricesByPaymentMethod();




    @Query("SELECT o FROM Order o WHERE DATE(o.orderDate) = :date")
    List<Order> findDailyOrders(@Param("date") Date date);



    @Query("SELECT o FROM Order o WHERE o.shippingAddress.id = :addressId")
    List<Order> findByShippingAddressId(@Param("addressId") Long addressId);


    boolean existsByShippingAddressId(Long addressId);





    @Query(value="SELECT DATE_TRUNC('day', o.order_date) AS date, SUM(SUM(o.grand_total_prize)) OVER (PARTITION BY DATE_TRUNC('day', o.order_date)) AS earnings FROM orders o WHERE o.order_status='Delivered' AND EXTRACT(YEAR FROM o.order_date) = :year AND EXTRACT(MONTH FROM o.order_date) =:month GROUP BY DATE_TRUNC('day', o.order_date)",nativeQuery = true)
    List<Object[]> dailyEarnings(@Param("year") int year, @Param("month") int month);

    @Query(value="SELECT DATE_TRUNC('month', o.order_date) AS month, SUM(SUM(o.grand_total_prize)) OVER (PARTITION BY DATE_TRUNC('month', o.order_date)) AS earnings FROM orders o WHERE o.order_status='Delivered' AND EXTRACT(YEAR FROM o.order_date) = :year GROUP BY DATE_TRUNC('month', o.order_date)",nativeQuery = true)
    List<Object[]> monthlyEarnings(@Param("year") int year);

    @Query(value="SELECT EXTRACT(YEAR FROM o.order_date) AS year, SUM(o.grand_total_prize) AS earnings FROM orders o WHERE o.order_status='Delivered' GROUP BY EXTRACT(YEAR FROM o.order_date)", nativeQuery = true)
    List<Object[]> yearlyEarnings();


//    @Query(value="SELECT DATE_TRUNC('week', o.order_date) AS week, SUM(SUM(o.grand_total_prize)) OVER (PARTITION BY DATE_TRUNC('week', o.order_date)) AS earnings FROM orders o WHERE o.order_status='Delivered' AND EXTRACT(YEAR FROM o.order_date) = :year GROUP BY DATE_TRUNC('week', o.order_date)", nativeQuery = true)
//    List<Object[]> weeklyEarnings(@Param("year") int year);

//    @Query(value="SELECT DATE_TRUNC('day', o.order_date) AS date, SUM(SUM(o.grand_total_prize)) OVER (PARTITION BY DATE_TRUNC('day', o.order_date)) AS earnings, COUNT(o.order_id) AS totelOrder  FROM orders o WHERE o.order_status='Delivered' AND EXTRACT(YEAR FROM o.order_date) = :year AND EXTRACT(MONTH FROM o.order_date) =:month GROUP BY DATE_TRUNC('day', o.order_date)",nativeQuery = true)
//    List<Object[]> dailyReport(@Param("year") int year, @Param("month") int month);

    @Query(value = "SELECT DATE_TRUNC('week', o.order_date) AS week, " +
            "SUM(SUM(CASE WHEN o.order_status = 'Delivered' THEN o.grand_total_prize ELSE 0 END)) OVER (PARTITION BY DATE_TRUNC('week', o.order_date)) AS earnings, " +
            "COUNT(o.order_id) AS totalOrders, " +
            "SUM(o.deduction) AS couponDeduction, " +
            "COUNT(CASE WHEN o.order_status = 'Delivered' THEN o.order_id END) AS deliveredOrders, " +
            "COUNT(CASE WHEN o.order_status = 'Cancel' THEN o.order_id END) AS cancelledOrders " +
            "FROM orders o " +
            "WHERE EXTRACT(YEAR FROM o.order_date) = :year " +
            "GROUP BY DATE_TRUNC('week', o.order_date)",
            nativeQuery = true)
    List<Object[]> weeklyEarnings(@Param("year") int year);


    @Query(value = "SELECT DATE_TRUNC('day', o.order_date) AS date, " +
            "SUM(SUM(CASE WHEN o.order_status = 'Delivered' THEN o.grand_total_prize ELSE 0 END)) OVER (PARTITION BY DATE_TRUNC('day', o.order_date)) AS earnings, " +
            "COUNT(o.order_id) AS totalOrders, " +
            "SUM(o.deduction) AS deduction, " +
            "COUNT(CASE WHEN o.order_status = 'Delivered' THEN o.order_id END) AS deliveredOrders, " +
            "COUNT(CASE WHEN o.order_status = 'Cancel' THEN o.order_id END) AS cancelledOrders " +
            "FROM orders o " +
            "WHERE EXTRACT(YEAR FROM o.order_date) = :year " +
            "AND EXTRACT(MONTH FROM o.order_date) = :month " +
            "GROUP BY DATE_TRUNC('day', o.order_date)",
            nativeQuery = true)
    List<Object[]> dailyReport(@Param("year") int year, @Param("month") int month);


    @Query(value = "SELECT o.order_date, o.order_id, od.product_id, p.name, p.description, od.unit_price, od.quantity, " +
            "od.total_price, o.deduction, o.shipping_fee, grand_total_prize " +
            "FROM orders o " +
            "JOIN order_details od ON o.order_id = od.order_id " +
            "JOIN products p ON od.product_id = p.product_id " +
            "WHERE o.order_date BETWEEN :startDate AND :endDate AND o.order_status='Delivered' " +
            "ORDER BY o.order_date, o.order_id",
            nativeQuery = true)
    List<Object[]> findOrdersWithinDateRange(@Param("startDate") Date startDate, @Param("endDate") Date endDate);


    @Query(value = "SELECT o.order_date, o.order_id, od.product_id, p.name, p.description, od.unit_price, od.quantity, " +
            "od.total_price, o.deduction, o.shipping_fee, grand_total_prize " +
            "FROM orders o " +
            "JOIN order_details od ON o.order_id = od.order_id " +
            "JOIN products p ON od.product_id = p.product_id " +
            "WHERE o.order_date >= CURRENT_DATE - INTERVAL '7 days' AND o.order_status='Delivered'" +
            "ORDER BY o.order_date, o.order_id ",
            nativeQuery = true)
    List<Object[]> lastWeekOrders();



    @Query(value = "SELECT o.order_id, p.product_id, p.name, p.description, od.unit_price, od.quantity, od.total_price, o.deduction, o.shipping_fee, o.grand_total_prize " +
            "FROM orders o " +
            "JOIN order_details od ON o.order_id = od.order_id " +
            "JOIN products p ON od.product_id = p.product_id " +
            "WHERE DATE(o.order_date) = CURRENT_DATE AND o.order_status='Delivered'", nativeQuery = true)
    List<Object[]> currentDayOrders();


    @Query(value = "SELECT c.name, p.name, SUM(od.quantity) as ordered_qty, SUM(od.total_price) as ordered_total_price " +
            "FROM orders o " +
            "JOIN order_details od ON o.order_id = od.order_id " +
            "JOIN products p ON od.product_id = p.product_id " +
            "JOIN categories c ON p.category_id = c.category_id " +
            "WHERE EXTRACT(YEAR FROM o.order_date) = :year AND o.order_status='Delivered' " +
            "GROUP BY c.name, p.name " +
            "ORDER BY c.name, p.name",
            nativeQuery = true)
    List<Object[]> findYearlyOrdersWithCategory(@Param("year") int year);


    @Query(value = "SELECT DATE_TRUNC('year', o.order_date) AS year, "
            + "SUM(SUM(CASE WHEN o.order_status = 'Delivered' THEN o.grand_total_prize ELSE 0 END)) OVER (PARTITION BY DATE_TRUNC('year', o.order_date)) AS earnings, "
            + "COUNT(o.order_id) AS totalOrders, "
            + "SUM(CASE WHEN o.order_status = 'Delivered' THEN o.deduction ELSE 0 END) AS totalDeduction, "
            + "COUNT(CASE WHEN o.order_status = 'Delivered' THEN o.order_id END) AS deliveredOrders, "
            + "COUNT(CASE WHEN o.order_status = 'Cancel' THEN o.order_id END) AS cancelledOrders "
            + "FROM orders o "
            + "WHERE EXTRACT(YEAR FROM o.order_date) = :year "
            + "GROUP BY DATE_TRUNC('year', o.order_date)", nativeQuery = true)
    List<Object[]> yearlyReport(@Param("year") int year);

    @Query(value = "SELECT SUM(SUM(CASE WHEN o.order_status = 'Delivered' THEN o.grand_total_prize ELSE 0 END)) OVER (PARTITION BY DATE_TRUNC('year', o.order_date)) AS earnings, "
            + "COUNT(o.order_id) AS totalOrders, "
            + "SUM(CASE WHEN o.order_status = 'Delivered' THEN o.deduction ELSE 0 END) AS totalDeduction, "
            + "COUNT(CASE WHEN o.order_status = 'Delivered' THEN o.order_id END) AS deliveredOrders, "
            + "COUNT(CASE WHEN o.order_status = 'Cancel' THEN o.order_id END) AS cancelledOrders "
            + "FROM orders o "
            + "WHERE o.order_date BETWEEN :startDate AND :endDate "
            + "GROUP BY DATE_TRUNC('year', o.order_date)", nativeQuery = true)
    List<Object[]> findByOrderDatesBetween(@Param("startDate") Date startDate, @Param("endDate") Date endDate);


//   ------------ Best selling ---------------

    @Query(value = "SELECT p.product_id, p.name, SUM(od.quantity) as total_sold " +
            "FROM order_details od " +
            "JOIN products p ON od.product_id = p.product_id " +
            "GROUP BY p.product_id, p.name " +
            "ORDER BY total_sold DESC " +
            "LIMIT 10", nativeQuery = true)
    List<Object[]>findTopSellingProducts();

    @Query(value = "SELECT c.category_id, c.name, SUM(od.quantity) as total_sold " +
            "FROM order_details od " +
            "JOIN products p ON od.product_id = p.product_id " +
            "JOIN categories c ON p.category_id = c.category_id " +
            "GROUP BY c.category_id, c.name " +
            "ORDER BY total_sold DESC " +
            "LIMIT 10", nativeQuery = true)
    List<Object[]> findTopSellingCategories();


}
