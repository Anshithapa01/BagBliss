package com.ecommerce.customer.Customer.controller;

import com.ecommerce.library.dto.CategoryDto;
import com.ecommerce.library.model.Category;
import com.ecommerce.library.model.Product;
import com.ecommerce.library.service.CategoryService;
import com.ecommerce.library.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
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
    public String showCategoryFilterHome(@RequestParam("name") String categoryName, Model model,
                                         HttpSession session,HttpServletRequest request){
        List<Product> products=productService.findAllByCategoryName(categoryName);
        int size=products.size();
        session.setAttribute("name",categoryName);
        session.setAttribute("currentPageUrl", request.getRequestURL().toString());
        model.addAttribute("products",products);
        model.addAttribute("size",size);
        CategoryDto category1=new CategoryDto();
        model.addAttribute("categories1",category1);
        List<Category> categories=categoryService.findAllByActivatedTrue();
        model.addAttribute("categories",categories);
        return "search_result";
    }

    @GetMapping("/filterSearch")
    public String filterSearch(@RequestParam("key") String key,
                               Model model, HttpSession session) {

        Object lastPortion1 = session.getAttribute("currentPageUrl");
        System.out.println(lastPortion1);
        String lastPortion=lastPortion1.toString();
        String[] parts = lastPortion.split("/");
        String lastSegment = parts[parts.length - 1];
        System.out.println("Last portion: " + lastSegment);
        System.out.println("key: " + key);
        List<Product> products=new ArrayList<>();
        switch (lastSegment){
            case "popular":
                products=productService.findTopSellingProductsWithKeyword(key);
                break;
            case "random":
                products=productService.randomProductWithKeyword(key);
                break;
            case "newArrival":
                products=productService.filterByIdDescendingWithKeyword(key);
                break;
            case "searchCategoryHome": {
                String categoryName=session.getAttribute("name").toString();
                products = productService.findProductsByCategoryNameAndKeyword(categoryName, key);
                break;
            }
            default:
                products = productService.searchProduct(key);

        }

        model.addAttribute("products",products);
        Category category=new Category();
        model.addAttribute("categories1",category);
        List<Category> categories=categoryService.findAllByActivatedTrue();
        model.addAttribute("categories",categories);
        model.addAttribute("size",products.size());
        return "search_result";
    }

}
