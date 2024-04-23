package com.ecommerce.customer.Customer.controller;

import com.ecommerce.library.dto.ProductDto;
import com.ecommerce.library.model.Category;
import com.ecommerce.library.model.Customer;
import com.ecommerce.library.model.Product;
import com.ecommerce.library.model.ShoppingCart;
import com.ecommerce.library.service.CategoryService;
import com.ecommerce.library.service.ProductService;
import com.ecommerce.library.service.ShoppingCartService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.security.Principal;
import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    private ShoppingCartService shoppingCartService;




    @GetMapping(value = {"/","/index"} )
    public String home(Model model, Principal principal, HttpSession session) {
        List<Category> categories= categoryService.findAll();
        List<ProductDto> productDtos=productService.findAll();
        model.addAttribute("caregories",categories);
        model.addAttribute("products",productDtos);
        return "index";
    }

    @GetMapping("/home")
    public String index(Model model,HttpSession session,Principal principal){
        if(principal != null){
            session.setAttribute("email",principal.getName());
        }else{
            session.removeAttribute("email");
        }
        List<Category> categories= categoryService.findAll();
        List<ProductDto> productDtos=productService.findAll();
        model.addAttribute("caregories",categories);
        model.addAttribute("products",productDtos);
        return "home";
    }



}
