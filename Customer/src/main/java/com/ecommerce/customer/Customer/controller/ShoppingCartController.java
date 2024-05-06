package com.ecommerce.customer.Customer.controller;


import com.ecommerce.library.dto.CustomerDto;
import com.ecommerce.library.model.Customer;
import com.ecommerce.library.model.ShoppingCart;
import com.ecommerce.library.service.CustomerService;
import com.ecommerce.library.service.ProductService;
import com.ecommerce.library.service.ShoppingCartService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

@Controller

public class ShoppingCartController {
    ShoppingCartService shopCartService;

    ProductService productService;
    CustomerService customerService;
    @Autowired
    public ShoppingCartController(ShoppingCartService shopCartService, ProductService productService,
                                  CustomerService customerService) {
        this.shopCartService = shopCartService;
        this.productService = productService;
        this.customerService = customerService;
    }

    @GetMapping("/cart")
    public String showCart(Model model, Principal principal, HttpSession session){
        if (principal==null){
            return "redirect:/login";
        }
        String username=principal.getName();
        List<ShoppingCart> shoppingCart=shopCartService.findShoppingCartByCustomer(username);

        double total=shopCartService.grandTotal(username);
        model.addAttribute("total",total);
        model.addAttribute("title", "Cart");
        model.addAttribute("shoppingCart",shoppingCart);
//        session.setAttribute("totalItems", shoppingCart.getTotalItems());

        return "cart";
    }



    @GetMapping("/addToCart")
    public String showAddToCart(@RequestParam("productId")Long id,
                                CustomerDto customerDto,
                                Principal principal,
                                Model model,
                                HttpSession session)
    {
        if(principal==null){
            return "redirect:/login";
        }
        int quantity=1;
        String username=principal.getName();
        Customer customer=customerService.findByEmail(username);
        ShoppingCart shoppingCart =shopCartService.addItemToCart(id,quantity,customer.getCustomer_id());
        model.addAttribute("shoppingCart", shoppingCart);
        return "redirect:/cart";

    }



    @GetMapping("/deleteCartItem")
    public String showDelete(@RequestParam("cartId")Long id,Principal principal){
        List<Customer> customerDto1=customerService.findAll();
        shopCartService.deleteById(id);
        return "redirect:/cart";
    }





    @GetMapping("/incrementQuantity")
    public String showQuantityIncrement(@RequestParam("cartId")Long id,@RequestParam("productId") Long pId){
        shopCartService.increment(id,pId);
        return "redirect:/cart";
    }




    @GetMapping("/decrementQuantity")
    public String showQuantityDecrement(@RequestParam("cartId")Long id){
        shopCartService.decrement(id);
        return "redirect:/cart";
    }

}
