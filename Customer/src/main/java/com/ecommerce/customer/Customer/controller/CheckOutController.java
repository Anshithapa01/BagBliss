package com.ecommerce.customer.Customer.controller;

import com.ecommerce.library.dto.AddressDto;
import com.ecommerce.library.model.Address;
import com.ecommerce.library.model.Coupon;
import com.ecommerce.library.model.Customer;
import com.ecommerce.library.model.ShoppingCart;
import com.ecommerce.library.service.AddressService;
import com.ecommerce.library.service.CustomerService;
import com.ecommerce.library.service.ShoppingCartService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
public class CheckOutController {

    AddressService addressService;
    CustomerService customerService;
    ShoppingCartService shopCartService;
@Autowired
    public CheckOutController(AddressService addressService, CustomerService customerService, ShoppingCartService shopCartService) {
        this.addressService = addressService;
        this.customerService= customerService;
        this.shopCartService=shopCartService;

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
        List<Address> addresses=addressService.findAddressByCustomer(username);
        model.addAttribute("customer",customer);
        model.addAttribute("addresses",addresses);
        model.addAttribute("cartItem",shoppingCarts);
        model.addAttribute("totel",total);
        model.addAttribute("payable",total);

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
