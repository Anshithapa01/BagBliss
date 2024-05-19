package com.ecommerce.library.repository;

import com.ecommerce.library.dto.ProductDto;
import com.ecommerce.library.model.Product;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    @Query(value = "SELECT * FROM products WHERE is_deleted = false ORDER BY random() LIMIT 10", nativeQuery = true)
    List<Product> randomProduct();


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

    @Query(value = "SELECT p.* " +
            "FROM products p " +
            "JOIN (" +
            "  SELECT od.product_id, SUM(od.quantity) as total_sold " +
            "  FROM order_details od " +
            "  GROUP BY od.product_id " +
            "  ORDER BY total_sold DESC " +
            "  LIMIT 10" +
            ") top_selling ON p.product_id = top_selling.product_id " +
            "ORDER BY top_selling.total_sold DESC", nativeQuery = true)
    List<Product> findTopSellingProducts();


    @Query(value = "select p from Product p inner join Category c on c.id = ?1 and p.category.id = ?1 where p.is_activated = true and p.is_deleted = false")
    List<Product> getProductByCategoryId(Long id);


    @Query("select p from Product p where p.name like %?1% or p.description like %?1%")
    Page<Product> searchProducts(String keyword, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE CONCAT('%', LOWER(?1), '%') OR LOWER(p.description) LIKE CONCAT('%', LOWER(?1), '%')")
    List<Product> searchProductsList(String keyword);

    @Query(value = "select * from products ORDER BY product_id DESC",nativeQuery = true)
    List<Product> findAllByOrderById();

    @Query("select  p from Product p")
    Page<Product> pageProducts(Pageable pageable);

    Page<Product> findAllByCategory_id(long id,Pageable pageable);

    List<Product> findAllByCategory_Id(long id);

    List<Product> findAllByCategoryName(String name);


    @Query(value = "SELECT p.*, c.category_id AS cat_id \n" +
            "FROM products p \n" +
            "JOIN categories c ON p.category_id = c.category_id \n" +
            "WHERE p.is_deleted = FALSE \n" +
            "AND c.is_deleted = FALSE \n" +
            "AND (LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(c.name) LIKE LOWER(CONCAT('%', :keyword, '%')))",nativeQuery = true)
    List<Product> listViewProductsUserSide(String keyword);


//    @Query(value = "SELECT p.*,c.category_id as cat_id FROM products p JOIN categories c ON p.category_id=c.category_id WHERE p.is_Deleted=FALSE AND c.is_deleted=FALSE AND (LOWER(p.name) LIKE %:keyword% OR LOWER(c.name) LIKE %:keyword%)",nativeQuery = true)
//    List<Product> listViewProductsUserSide(String keyword);

//    Search by filtered

    @Query(value = "SELECT product_id FROM products WHERE is_deleted = false ORDER BY product_id DESC LIMIT 10", nativeQuery = true)
    List<Long> findLast10ProductIds();


    @Query(value = "SELECT p.* FROM products p " +
            "JOIN categories c ON p.category_id = c.category_id " +
            "WHERE p.is_deleted = false AND p.product_id IN :ids AND " +
            "(LOWER(p.name) LIKE CONCAT('%', LOWER(:keyword), '%') " +
            "OR LOWER(c.name) LIKE CONCAT('%', LOWER(:keyword), '%'))", nativeQuery = true)
    List<Product> findProductsByIdsAndKeyword(@Param("ids") List<Long> ids, @Param("keyword") String keyword);


    @Query(value = "SELECT p.* " +
            "FROM products p " +
            "JOIN (" +
            "  SELECT od.product_id, SUM(od.quantity) as total_sold " +
            "  FROM order_details od " +
            "  GROUP BY od.product_id " +
            "  ORDER BY total_sold DESC " +
            "  LIMIT 10" +
            ") top_selling ON p.product_id = top_selling.product_id " +
            "WHERE LOWER(p.name) LIKE CONCAT('%', LOWER(:keyword), '%') " +
            "ORDER BY top_selling.total_sold DESC",
            nativeQuery = true)
    List<Product> findTopSellingProductsWithKeyword(@Param("keyword") String keyword);

    @Query("SELECT p FROM Product p WHERE lower(p.category.name) = lower(:categoryName) AND (lower(p.name) ILIKE %:keyword% OR lower(p.category.name) ILIKE %:keyword%)")
    List<Product> findByCategoryNameAndKeywordIgnoreCase(@Param("categoryName") String categoryName, @Param("keyword") String keyword);

    @Query(value = "SELECT p.* FROM products p " +
            "JOIN categories c ON p.category_id = c.category_id " +
            "WHERE p.is_deleted = false AND p.product_id IN :ids AND " +
            "(LOWER(p.name) LIKE CONCAT('%', LOWER(:keyword), '%') " +
            "OR LOWER(c.name) LIKE CONCAT('%', LOWER(:keyword), '%'))", nativeQuery = true)
    List<Product> randomProductWithKeyword(@Param("ids") List<Long> ids, @Param("keyword") String keyword);
}


