package com.ecommerce.customer.Customer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class SearchController {

    @PostMapping("/search")
    public String searchProduct(){

        return "redirect:/shop";
    }
}
