package com.ecommerce.admin.controller;

import com.ecommerce.library.model.Category;
import com.ecommerce.library.model.Product;
import com.ecommerce.library.service.CategoryService;
import com.ecommerce.library.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

@Controller
public class SearchController {

    private final ProductService productService;
    private final CategoryService categoryService;
    @Autowired
    public SearchController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @GetMapping("/product")
    public String allProduct(Model model) {
        Category category=new Category();
        model.addAttribute("categories1",category);
        return "products-result";
    }

    @GetMapping("/searchProduct")
    public String showSearchProduct(@RequestParam(name = "key", required = false)String key,Model model){
        List<Product>products=productService.searchProducts(key);
        List<Category> categories=categoryService.findAllByActivatedTrue();
        model.addAttribute("categories",categories);
        model.addAttribute("products",products);
        model.addAttribute("size",products.size());
        Category category=new Category();
        model.addAttribute("categories1",category);
        return "products-result";
    }

    @GetMapping("/searchCategory")
    public String showCategorySearch(@ModelAttribute("categories1")Category category,Model model){
        List<Product> products=productService.findAllByCategory(category.getName());
        model.addAttribute("products",products);
        model.addAttribute("size",products.size());
        List<Category> categories=categoryService.findAllByActivatedTrue();
        model.addAttribute("categories",categories);
        Category category1=new Category();
        model.addAttribute("categories1",category1);

        return "products-result";
    }
}
