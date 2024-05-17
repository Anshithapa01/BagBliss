package com.ecommerce.customer.Customer.controller;

import com.ecommerce.library.dto.CategoryDto;
import com.ecommerce.library.dto.ProductDto;
import com.ecommerce.library.service.CategoryService;
import com.ecommerce.library.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Controller
public class FilterController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;


    @GetMapping("/high-price")
    public String filterHighPrice(@RequestParam(defaultValue = "0") int pageNo, Model model) {
        try {
            List<CategoryDto> categories = categoryService.getCategoriesAndSize();
            model.addAttribute("categories", categories);
            int pageSize = 10; 
            Page<ProductDto> products = productService.filterHighProducts(pageNo, pageSize);
            List<ProductDto> listView = productService.listViewProducts();
            model.addAttribute("title", "Shop Detail");
            model.addAttribute("page", "Shop Detail");
            model.addAttribute("productViews", listView);
            model.addAttribute("products", products);

            // Pass pagination-related attributes to the view
            model.addAttribute("currentPage", pageNo);
            model.addAttribute("totalPages", products.getTotalPages());

            return "shop-list";
        } catch (DataAccessException ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error retrieving products", ex);
        }
    }



    @GetMapping("/lower-price")
    public String filterLowerPrice(Model model,@RequestParam(defaultValue = "0") int pageNo) {
        try {
            List<CategoryDto> categories = categoryService.getCategoriesAndSize();
            model.addAttribute("categories", categories);
            int pageSize = 10; 
            Page<ProductDto> products = productService.filterLowerProducts(pageNo, pageSize);
            List<ProductDto> listView = productService.listViewProducts();
            model.addAttribute("title", "Shop Detail");
            model.addAttribute("page", "Shop Detail");
            model.addAttribute("productViews", listView);
            model.addAttribute("products", products);

            // Pass pagination-related attributes to the view
            model.addAttribute("currentPage", pageNo);
            model.addAttribute("totalPages", products.getTotalPages());

            return "shop-list";
        } catch (DataAccessException ex) {
        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error retrieving products", ex);
        }
    }



    @GetMapping("/ascending-name")
    public String filterByNameAscending(Model model, @RequestParam(defaultValue = "0") int pageNo) {
        try {
            List<CategoryDto> categories = categoryService.getCategoriesAndSize();
            model.addAttribute("categories", categories);
            int pageSize = 10; 
            Page<ProductDto> products = productService.filterByNameAscending(pageNo, pageSize);
            List<ProductDto> listView = productService.listViewProducts();
            model.addAttribute("title", "Shop Detail");
            model.addAttribute("page", "Shop Detail");
            model.addAttribute("productViews", listView);
            model.addAttribute("products", products);

            // Pass pagination-related attributes to the view
            model.addAttribute("currentPage", pageNo);
            model.addAttribute("totalPages", products.getTotalPages());

            return "shop-list";
        } catch (DataAccessException ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error retrieving products", ex);
        }
    }

    @GetMapping("/descending-name")
    public String filterByNameDescending(Model model, @RequestParam(defaultValue = "0") int pageNo) {
        try{
            List<CategoryDto> categories = categoryService.getCategoriesAndSize();
            model.addAttribute("categories", categories);
            int pageSize = 10; 
            Page<ProductDto> products = productService.filterByNameDescending(pageNo, pageSize);
            List<ProductDto> listView = productService.listViewProducts();
            model.addAttribute("title", "Shop Detail");
            model.addAttribute("page", "Shop Detail");
            model.addAttribute("productViews", listView);
            model.addAttribute("products", products);

            // Pass pagination-related attributes to the view
            model.addAttribute("currentPage", pageNo);
            model.addAttribute("totalPages", products.getTotalPages());

            return "shop-list";
        } catch (DataAccessException ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error retrieving products", ex);
        }
    }


    @GetMapping("/descending-id")
    public String filterByIdDescending(Model model, @RequestParam(defaultValue = "0") int pageNo) {
        try{
            List<CategoryDto> categories = categoryService.getCategoriesAndSize();
            model.addAttribute("categories", categories);
            int pageSize = 10;
            Page<ProductDto> products = productService.filterByIdDescending(pageNo, pageSize);
            List<ProductDto> listView = productService.listViewProducts();
            model.addAttribute("title", "Shop Detail");
            model.addAttribute("page", "Shop Detail");
            model.addAttribute("productViews", listView);
            model.addAttribute("products", products);

            // Pass pagination-related attributes to the view
            model.addAttribute("currentPage", pageNo);
            model.addAttribute("totalPages",1);

            return "shop-list";
        } catch (DataAccessException ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error retrieving products", ex);
        }
    }


    @GetMapping("/random")
    public String filterByRandom(Model model, @RequestParam(defaultValue = "0") int pageNo) {
        try{
            List<CategoryDto> categories = categoryService.getCategoriesAndSize();
            model.addAttribute("categories", categories);
            int pageSize = 10; 
            Page<ProductDto> products = productService.filterByRandom(pageNo, pageSize);
            List<ProductDto> listView = productService.listViewProducts();
            model.addAttribute("title", "Shop Detail");
            model.addAttribute("page", "Shop Detail");
            model.addAttribute("productViews", listView);
            model.addAttribute("products", products);

            // Pass pagination-related attributes to the view
            model.addAttribute("currentPage", pageNo);
            model.addAttribute("totalPages", products.getTotalPages());

            return "shop-list";
        } catch (DataAccessException ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error retrieving products", ex);
        }
    }

    @ExceptionHandler(ResponseStatusException.class)
    public String handleResponseStatusException(ResponseStatusException ex, Model model) {
        model.addAttribute("errorMessage", ex.getReason());
        return "error-page";
    }

}
