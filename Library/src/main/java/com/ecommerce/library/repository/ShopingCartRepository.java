package com.ecommerce.library.repository;

import com.ecommerce.library.model.ShoppingCart;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Transactional
public interface ShopingCartRepository extends JpaRepository<ShoppingCart,Long> {

    @Query("SELECT sc from ShoppingCart sc where sc.customer.customer_id=?1 and sc.product.id=?2 and sc.deleted=false")
    ShoppingCart findByUsersProduct(Long id, long productId);
    @Query("select sc from ShoppingCart sc where sc.customer.email=?1 and sc.deleted=false")
    List<ShoppingCart> findShoppingCartByCustomer(String email);


    @Query("SELECT COALESCE(SUM(sc.totalPrice), 0) FROM ShoppingCart sc WHERE sc.customer.customer_id = :id and sc.deleted=false")
   double findGrandTotal(Long id);

    @Modifying
    @Query("DELETE FROM ShoppingCart sc WHERE sc.id = :id")
    void deleteShoppingCartItemById(@Param("id") Long id);

}
