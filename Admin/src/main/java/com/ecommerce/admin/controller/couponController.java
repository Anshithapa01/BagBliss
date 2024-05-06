package com.ecommerce.admin.controller;


import com.ecommerce.library.dto.CouponDto;
import com.ecommerce.library.model.Coupon;
import com.ecommerce.library.service.CouponService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class couponController {

    private CouponService couponService;
    @Autowired
    public couponController(CouponService couponService) {
        this.couponService = couponService;
    }

    @GetMapping("/coupon")
    public String showcoupon(Model model){

        List<Coupon> coupons=couponService.findAll();

        model.addAttribute("coupons",coupons);
        return "coupon";
    }
    @GetMapping("/addcoupon")
    public String ShowAddcoupon(Model model){
        CouponDto couponDto=new CouponDto();
        model.addAttribute("coupon",couponDto);
        return "add-coupon";
    }

    @PostMapping("/savecoupon")
    public String showSavecoupon(@Valid @ModelAttribute("coupon")CouponDto couponDto,
                                 BindingResult result,Model model){
        if(result.hasErrors()){
            model.addAttribute("couponDto", couponDto);
            result.toString();
            return "add-coupon";
        }
        couponService.save(couponDto);

        return "redirect:/coupon";
    }

    @GetMapping("/disablecoupon")
    public String showDisablecoupon(@RequestParam("couponId")Long id){

        couponService.disableCoupon(id);
        return "redirect:/coupon";
    }

    @GetMapping("/enablecoupon")
    public String showEnablecoupon(@RequestParam("couponId")Long id){

        couponService.enableCoupon(id);
        return "redirect:/coupon";
    }
    @GetMapping("/editcoupon")
    public String showcouponUpdate(@RequestParam("couponId")Long id,Model model){
        Coupon coupon=couponService.findByid(id);
        model.addAttribute("coupon",coupon);
        return "edit-coupon";
    }
    @PostMapping("/updatecoupon")
    public String updatecoupon(@Valid @ModelAttribute("coupon")CouponDto couponDto,
                               BindingResult result,Model model){
        if(result.hasErrors()){
            model.addAttribute("couponDto", couponDto);
            result.toString();
            return "edit-coupon";
        }
        couponService.updateCoupon(couponDto);
        return "redirect:/coupon";
    }
}
