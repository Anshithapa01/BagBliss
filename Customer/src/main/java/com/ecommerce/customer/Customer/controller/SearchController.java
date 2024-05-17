package com.ecommerce.customer.Customer.controller;

import com.ecommerce.library.dto.CategoryDto;
import com.ecommerce.library.model.Category;
import com.ecommerce.library.model.Product;
import com.ecommerce.library.service.CategoryService;
import com.ecommerce.library.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class SearchController {

    ProductService productService;
    CategoryService categoryService;

    public SearchController(CategoryService categoryService,
                            ProductService productService){
        this.categoryService=categoryService;
        this.productService=productService;
    }

    @GetMapping("/search")
    public String showSearch(@RequestParam("key")String key, Model model){
        List<Product> products = productService.searchProduct(key);
        model.addAttribute("products",products);
        Category category=new Category();
        model.addAttribute("categories1",category);
        List<Category> categories=categoryService.findAllByActivatedTrue();
        model.addAttribute("categories",categories);
        model.addAttribute("size",products.size());
        return "search_result";
    }

    @GetMapping("/searchCategoryHome")
    public String showCategoryFilterHome(@RequestParam("name") String categoryName, Model model){
        List<Product> products=productService.findAllByCategoryName(categoryName);
        int size=products.size();
        model.addAttribute("products",products);
        model.addAttribute("size",size);
        CategoryDto category1=new CategoryDto();
        model.addAttribute("categories1",category1);
        List<Category> categories=categoryService.findAllByActivatedTrue();
        model.addAttribute("categories",categories);
        return "search_result";
    }
}
