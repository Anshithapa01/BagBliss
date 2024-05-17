package com.ecommerce.customer.Customer.controller;

import com.ecommerce.library.dto.CouponDto;
import com.ecommerce.library.model.Address;
import com.ecommerce.library.model.Coupon;
import com.ecommerce.library.model.Customer;
import com.ecommerce.library.model.ShoppingCart;
import com.ecommerce.library.service.AddressService;
import com.ecommerce.library.service.CouponService;
import com.ecommerce.library.service.CustomerService;
import com.ecommerce.library.service.ShoppingCartService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
import java.util.List;

@Controller
public class CoupenController {
    ShoppingCartService shoppingCartService;
    CouponService couponService;
    AddressService addressService;
    CustomerService customerService;

    public CoupenController(ShoppingCartService shoppingCartService, CouponService couponService,
                            AddressService addressService, CustomerService customerService) {
        this.shoppingCartService = shoppingCartService;
        this.couponService = couponService;
        this.addressService = addressService;
        this.customerService = customerService;
    }
    @PostMapping("/applyCoupon")
    public ResponseEntity<String> showApplyCoupon(@ModelAttribute("coupon") CouponDto couponDto,
                                  Principal principal,
                                  Model model){

        String username=principal.getName();
        Customer customer=customerService.findByEmail(username);
        List<ShoppingCart> shopingCarts=shoppingCartService.findShoppingCartByCustomer(username);
        Coupon coupon=couponService.findByCouponCode(couponDto.getCouponCode());
        double grandTotel=shoppingCartService.grandTotal(username);
        if(coupon==null){
            String errorMessage = "Coupon not found";
            model.addAttribute("errorMessage", errorMessage);
            String updatedHtmlContent = "<div class='d-block'>" +
                    "<p class = 'alert alert-danger' id='errorMessage'>" +
                    errorMessage+
                    "</p></div>";
            return ResponseEntity.ok()
                    .header("errorMessage", String.valueOf(errorMessage))
                    .body(updatedHtmlContent);
        }
        if(coupon.isExpired()==true){
            model.addAttribute("errorMessage","Coupon not found");
            String updatedHtmlContent = "<th>Total</th>" +
                    "<td colspan='2' class='product-subtotal'>" +
                    "<span class='font-xl text-brand fw-900 payment_field' id='totalPrice'>" +
                    grandTotel + "</span></td>";

            return ResponseEntity.ok()
                    .header("payable", String.valueOf(grandTotel))
                    .body(updatedHtmlContent);
        }

        List<Address> addresses=addressService.findAddressByCustomer(username);
        double payableAmount;
        if(grandTotel<coupon.getMinimumOrderAmount())
        {
            model.addAttribute("errorMessage","order amount is less. ");
            model.addAttribute("addresses",addresses);
            model.addAttribute("cartItem",shopingCarts);
            model.addAttribute("totel",grandTotel);
            model.addAttribute("customer",customer);
            model.addAttribute("payable",grandTotel);
            String errorMessage = "Order amount is less";
            String updatedHtmlContent ="<div class='d-block'>" +
                    "<p class = 'alert alert-danger' id='errorMessage'>" +
                    errorMessage+
                    "</p></div>";

            return ResponseEntity.ok()
                    .header("errorMessage", String.valueOf(errorMessage))
                    .body(updatedHtmlContent);
        }
        double offerPercentage= Double.parseDouble(coupon.getOfferPercentage());
        double offer=Double.parseDouble(String.format("%.2f", ( (grandTotel * offerPercentage) / 100)));
        if(offer<coupon.getMaximumOfferAmount()) {
           payableAmount = Double.parseDouble(String.format("%.2f", (grandTotel - offer)));

        }
        else{
            payableAmount = Double.parseDouble(String.format("%.2f", (grandTotel -100)));
        }
        couponService.dicreseCoupon(coupon.getId());
        model.addAttribute("addresses",addresses);
        model.addAttribute("cartItem",shopingCarts);
        model.addAttribute("totel",grandTotel);
        model.addAttribute("customer",customer);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("payable", String.valueOf(payableAmount));
        responseHeaders.set("offer", String.valueOf(offer));
        String updatedHtmlContent = "<th>Total</th>" +
                "<td colspan='2' class='product-subtotal'>" +
                "<span class='font-xl text-brand fw-900 payment_field' id='totalPrice'>" +
                payableAmount + "</span></td>";

        // Return the updated HTML content along with the updated payable value in the response
        return ResponseEntity.ok()
                .headers(responseHeaders)
                .body(updatedHtmlContent);
    }


    @PostMapping("/removeCoupon")
    public ResponseEntity<String> showRemoveCoupon(@ModelAttribute("coupon") CouponDto couponDto,
                                  Principal principal,
                                  Model model) {
        String username=principal.getName();
        double grandTotal=shoppingCartService.grandTotal(username);
        model.addAttribute("payable",grandTotal);
        model.addAttribute("success","Coupon removed successfully");
        String updatedHtmlContent = "<th>Total</th>" +
                "<td colspan='2' class='product-subtotal'>" +
                "<span class='font-xl text-brand fw-900 payment_field' id='totalPrice'>" +
                grandTotal + "</span></td>";

        // Return the updated HTML content along with the updated payable value in the response
        return ResponseEntity.ok()
                .header("payable", String.valueOf(grandTotal))
                .body(updatedHtmlContent);
    }
}
