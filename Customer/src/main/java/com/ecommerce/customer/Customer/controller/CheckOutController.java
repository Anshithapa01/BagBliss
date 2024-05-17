package com.ecommerce.customer.Customer.controller;

import com.ecommerce.library.dto.AddressDto;
import com.ecommerce.library.model.*;
import com.ecommerce.library.service.AddressService;
import com.ecommerce.library.service.CustomerService;
import com.ecommerce.library.service.OrderService;
import com.ecommerce.library.service.ShoppingCartService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
public class CheckOutController {

    AddressService addressService;
    CustomerService customerService;
    ShoppingCartService shopCartService;

    OrderService orderService;
@Autowired
    public CheckOutController(AddressService addressService, CustomerService customerService,
                              ShoppingCartService shopCartService,OrderService orderService) {
        this.addressService = addressService;
        this.customerService= customerService;
        this.shopCartService=shopCartService;
        this.orderService=orderService;

    }

    @GetMapping("/checkOut")
    public String showCheckOutPage(Model model, Principal principal){
        if(principal==null)
        {
            return "redirect:/login";
        }
        String username=principal.getName();
        Customer customer=  customerService.findByEmail(username);

        List<ShoppingCart> shoppingCarts=shopCartService.findShoppingCartByCustomer(username);
        System.out.println(username);
        Coupon coupon=new Coupon();
        model.addAttribute("coupon",coupon);

        if(shoppingCarts.isEmpty()){

            return "redirect:/cart?empties";
        }
        for(ShoppingCart cart:shoppingCarts){
            int quantity=cart.getQuantity();
            int productQuantity=cart.getProduct().getCurrentQuantity();
            if(quantity>productQuantity){
                return "redirect:/cart?quantityError";
            }
        }
        double total=shopCartService.grandTotal(username);
        double grandTotal=shopCartService.finalGrandTotal(username);
        double shipping=shopCartService.shippingFee(username);
        List<Address> addresses=addressService.findAddressByCustomer(username);
        model.addAttribute("customer",customer);
        model.addAttribute("addresses",addresses);
        model.addAttribute("shipping",shipping);
        model.addAttribute("cartItem",shoppingCarts);
        model.addAttribute("totel",total);
        model.addAttribute("payable",grandTotal);

    return "checkOut";
    }

    @GetMapping("/rePay/{id}")
    public String showRePayPage(@PathVariable("id")Long id,
            Model model, Principal principal){
        if(principal==null)
        {
            return "redirect:/login";
        }
        String username=principal.getName();
        Customer customer=  customerService.findByEmail(username);


        Order orders=orderService.findOrderById(id);
        List<OrderDetails> orderDetails = orderService.findByOrderId(id);
        System.out.println(username);
        Coupon coupon=new Coupon();
        model.addAttribute("coupon",coupon);

        if(orders==null){
            return "redirect:/orderDetails/0?empties";
        }

        double total= orders.getGrandTotalPrize()-orders.getShippingFee();
        double grandTotal=orders.getGrandTotalPrize();
        double shipping=orders.getShippingFee();
        List<Address> addresses=addressService.findAddressByCustomer(username);
        model.addAttribute("customer",customer);
        model.addAttribute("addresses",addresses);
        model.addAttribute("shipping",shipping);
        model.addAttribute("cartItem",orderDetails);
        model.addAttribute("totel",total);
        model.addAttribute("payable",grandTotal);
        model.addAttribute("id",id);
        return "checkOut";
    }

    @GetMapping("/addAddress")
    public String showAddAddress(Model model,
                                 Principal principal){
        if(principal==null){
            return "redirect:/login";
        }
        AddressDto addressDtos=new AddressDto();
        model.addAttribute("tittle","Add address");
        model.addAttribute("address",addressDtos);
        return "add-address";
    }



    @PostMapping("/saveAddress")
    public String saveAddress(@Valid @ModelAttribute("address") AddressDto addressDto,
                              BindingResult result,
                              HttpSession session,
                              Principal principal){
        if (result.hasErrors()) {
            session.removeAttribute("error");
            return "add-address";
        }
        if(principal==null) {
            return "redirect:/login";
        }

    String username=principal.getName();
    addressService.save(addressDto,username);
        return "redirect:/checkOut";
    }


    @GetMapping("/editAddress")
    public String showEditAddress(@RequestParam("addressId")Long id,Model model){
        Optional<Address> optionalAddress = addressService.findByid(id);
        Address address = optionalAddress.orElse(new Address());
        model.addAttribute("address", address);
    return "edit-address";
    }
    @PostMapping("/updateAddress")
    public String showEditAddress(@ModelAttribute("address")AddressDto addressDto){
        addressService.update(addressDto);
        return "redirect:/checkOut";
    }
}
