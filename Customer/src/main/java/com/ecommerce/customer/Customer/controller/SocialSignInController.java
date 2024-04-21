package com.ecommerce.customer.Customer.controller;

import com.fasterxml.jackson.core.JsonFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class SocialSignInController {

    @GetMapping("/sign-up")
    public String showGoogleSignInPage() {
        return "sign-up"; // Return the name of the HTML file without the extension
    }
}
