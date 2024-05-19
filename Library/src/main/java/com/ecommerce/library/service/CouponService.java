package com.ecommerce.library.service;

import com.ecommerce.library.dto.CouponDto;
import com.ecommerce.library.model.Coupon;

import java.util.List;

public interface CouponService {
    void save(CouponDto couponDto);

    List<Coupon> findAll();
    void enableCoupon(Long id);
    void disableCoupon(Long id);
    Coupon findByCouponCode(String coupencode);

    void dicreseCoupon(long id);

    void increaseCoupon(long id);

    Coupon findByid(Long id);

    void updateCoupon(CouponDto couponDto);

    List<Coupon> getEnabledCoupons();
}
