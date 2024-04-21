package com.ecommerce.customer.Customer.controller;

import com.ecommerce.library.dto.CategoryDto;
import com.ecommerce.library.dto.ProductDto;
import com.ecommerce.library.model.Category;
import com.ecommerce.library.model.Product;
import com.ecommerce.library.service.CategoryService;
import com.ecommerce.library.service.ProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.util.List;

@Controller
public class ProductController {


    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/product-shop/{pageNo}")
    public String shop(@PathVariable("pageNo") int pageNo,Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        Page<Product> products = productService.pageProducts(pageNo);
        List<Category> categories= categoryService.findAllByActivatedTrue();
        model.addAttribute("title", "Shop");
        model.addAttribute("caregories",categories);
        model.addAttribute("size", products.getSize());
        model.addAttribute("products",products);
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", products.getTotalPages());
        return "shop";
    }

    @GetMapping("/find-products/{id}")
    public String findProductById(@PathVariable("id") Long id, Model model) {
        List<Category> categories = categoryService.findAllByActivatedTrue();
        ProductDto productDto=productService.getById(id);
        List<Product> productDtoList = productService.findAllByCategory(productDto.getCategory().getId());
        int currentQuantity = productDto.getCurrentQuantity();
        boolean inStock = currentQuantity > 0;
        boolean limitedStock = currentQuantity > 0 && currentQuantity < 10;
        boolean outOfStock = currentQuantity == 0;

        // Add availability flags to the model
        model.addAttribute("inStock", inStock);
        model.addAttribute("limitedStock", limitedStock);
        model.addAttribute("outOfStock", outOfStock);

        model.addAttribute("categories",categories);
        model.addAttribute("productDto",productDto);
        model.addAttribute("products",productDtoList);
        return "product-description";
    }

}
