package com.ecommerce.customer.Customer.controller;

import com.ecommerce.library.dto.AddressDto;
import com.ecommerce.library.dto.CustomerDto;
import com.ecommerce.library.model.Address;
import com.ecommerce.library.model.Customer;
import com.ecommerce.library.model.Order;
import com.ecommerce.library.service.AddressService;
import com.ecommerce.library.service.CustomerService;
import com.ecommerce.library.service.OrderService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class AccountController {

    AddressService addressService;

    CustomerService customerService;

    OrderService orderService;


    @Autowired
    public AccountController(AddressService addressService,
                             CustomerService customerService,
                             OrderService orderService) {
        this.addressService = addressService;
        this.customerService=customerService;
        this.orderService=orderService;
    }


    @GetMapping("/account")
    public String showAccount(Model model,Principal principal){
        if(principal==null){
            return "redirect:/login";
        }
        String username=principal.getName();
        Customer customer=customerService.findByEmail(username);
        model.addAttribute("customer",customer);
        // List<Order>orderDetails=orderService.findOrderByCustomer(username);
        //  model.addAttribute("orderDetails",orderDetails);
        model.addAttribute("address",customer.getAddress());
        return "account";
    }

        @GetMapping("/accountAddAddress")
        public String showAccountAddAddress(Principal principal, Model model){
        if(principal==null){
            return "redirect:/login";
        }
        AddressDto addressDto=new AddressDto();
        model.addAttribute("address",addressDto);
        return "addAddress";
        }
        @PostMapping("/saveAddressAccount")
    public String showSaveAccountAddress(@Valid @ModelAttribute("address")AddressDto addressDto,
                                         BindingResult result,
                                         HttpSession session,
                                         Principal principal){
            if (result.hasErrors()) {
                session.removeAttribute("error");
                return "addAddress";
            }
        String username=principal.getName();
        addressService.save(addressDto,username);
        return "redirect:/account";
        }

        @GetMapping("/editCustomerAddress")
        public String showEditCustomerAddress(@RequestParam("addressId")Long id,Model model){
            Optional<Address> optionalAddress = addressService.findByid(id);
            Address address = optionalAddress.orElse(new Address());
            model.addAttribute("address", address);
        return "edit-account-address";
        }


        @PostMapping("/updateAccountAddress")
        public String showUpdateCustomerAddress(@ModelAttribute("address")AddressDto addressDto){
        addressService.update(addressDto);
        return "redirect:/account";
        }


    @GetMapping("/editCustomerDetails")
    public String showEditCustomerDetails(@RequestParam("email")String email,Model model){
        Customer customer=customerService.findByEmail(email);
        model.addAttribute("customer",customer);
        return "edit-account";
    }

    @PostMapping("/updateAccountAccount")
    public String showUpdateCustomerAccount(@ModelAttribute("customer") Customer customer,
                                            @RequestParam("email") String email,
                                            @RequestParam("name") String name,
                                            @RequestParam("mobile") Long mobile) {
        customerService.update(email,name,mobile);
        return "redirect:/account";
    }

    @GetMapping("/deleteCustomerAddress")
    public String deleteCustomerAddress(@RequestParam("addressId")Long id,
                                        RedirectAttributes redirectAttributes,
                                        Model model) {

        boolean hasOrders = orderService.hasOrdersForAddress(id);
        if (hasOrders) {
            redirectAttributes.addFlashAttribute("error", "This address cannot be deleted because there are orders associated with it.");
        }else {
            addressService.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Address deleted successfully");
        }
        return "redirect:/account";
    }

}
