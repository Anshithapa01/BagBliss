package com.ecommerce.library.service;

import com.ecommerce.library.dto.ProductDto;
import com.ecommerce.library.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {
    List<ProductDto> findAll();

    Product save(List<MultipartFile>imageProduct, ProductDto productDto);

    Product update(List<MultipartFile> imageProduct, ProductDto productDto);

    void enableById(Long id);

    void deleteById(Long id);

     ProductDto getById(Long id);

    Page<Product> pageProducts(int pageNo);

    Page<ProductDto> searchProducts(int pageNo, String keyword);








//     Optional<Product> findBYId(Long id);



    // List<ProductDto> products();

    //List<ProductDto> allProduct();

    // List<ProductDto> findAllByOrderDesc();





    // List<ProductDto> randomProduct();


    // Page<ProductDto> getAllProducts(int pageNo);

    // Page<ProductDto> getAllProductsForCustomer(int pageNo);

    //List<ProductDto> findAllByCategory(String category);

    // List<ProductDto> filterHighProducts();

    // List<ProductDto> filterLowerProducts();

    // List<ProductDto> listViewProducts();

    // List<ProductDto> findByCategoryId(Long id);

  //  Page<Product> searchProductsList(String keyword);

}
