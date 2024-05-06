package com.ecommerce.library.repository;

import com.ecommerce.library.model.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponRepository extends JpaRepository<Coupon,Long> {
    @Query("select c from Coupon c where c.couponCode=?1")
    Coupon findByCouponCode(String couponCode);
}
