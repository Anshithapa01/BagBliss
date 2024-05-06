package com.ecommerce.library.repository;

import com.ecommerce.library.dto.ProductDto;
import com.ecommerce.library.model.Product;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {


    @Query("select p from Product p where p.is_deleted = false and p.is_activated = true")
    List<Product> getAllProduct();

    @Query("select p from Product p where p.name like %?1% or p.description like %?1%")
    List<Product> findAllByNameOrDescription(String keyword);


    @Query("select p from Product p inner join Category c ON c.id = p.category.id" +
            " where p.category.name = ?1 and p.is_activated = true and p.is_deleted = false")
    List<Product> findAllByCategory(String category);

    @Query(value = "SELECT * FROM products WHERE is_deleted = false ORDER BY random()", nativeQuery = true)
    Page<Product> randomProduct(Pageable pageable);


    @Query(value = "SELECT * FROM products WHERE is_deleted = false ORDER BY sale_price DESC", nativeQuery = true)
    Page<Product> filterHighProducts(Pageable pageable);

    @Query(value = "SELECT * FROM products WHERE is_deleted = false ORDER BY sale_price ASC", nativeQuery = true)
    Page<Product> filterLowerProducts(Pageable pageable);


    @Query(value = "SELECT * FROM products WHERE is_deleted = false ORDER BY name ASC", nativeQuery = true)
    Page<Product> filterByNameAscending(Pageable pageable);

    @Query(value = "SELECT * FROM products WHERE is_deleted = false ORDER BY name DESC", nativeQuery = true)
    Page<Product> filterByNameDescending(Pageable pageable);

    @Query(value = "SELECT * FROM products WHERE is_deleted = false ORDER BY product_id DESC", nativeQuery = true)
    Page<Product> filterByIdDescending(Pageable pageable);

    @Query(value = "SELECT * FROM products where is_deleted = false and is_activated = true limit 4", nativeQuery = true)
    List<Product> listViewProduct();


    @Query(value = "select p from Product p inner join Category c on c.id = ?1 and p.category.id = ?1 where p.is_activated = true and p.is_deleted = false")
    List<Product> getProductByCategoryId(Long id);


    @Query("select p from Product p where p.name like %?1% or p.description like %?1%")
    Page<Product> searchProducts(String keyword, Pageable pageable);

    @Query("select p from Product p where p.name like %?1% or p.description like %?1%")
    List<Product> searchProductsList(String keyword);

    @Query(value = "select * from products ORDER BY product_id DESC",nativeQuery = true)
    List<Product> findAllByOrderById();

    @Query("select  p from Product p")
    Page<Product> pageProducts(Pageable pageable);

    List<Product> findAllByCategoryId(long id);

    
}
