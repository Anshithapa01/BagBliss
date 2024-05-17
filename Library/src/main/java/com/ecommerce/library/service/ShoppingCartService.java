package com.ecommerce.library.service;

import com.ecommerce.library.model.ShoppingCart;

import java.util.List;

public interface ShoppingCartService {
     ShoppingCart addItemToCart(Long productId, int quantity, Long id);


    List<ShoppingCart> findShoppingCartByCustomer(String email);
    void deleteById(long id);
//    ,Long customerId
  Double grandTotal(String username);

    Double finalGrandTotal(String username);

    Double shippingFee(String username);

    void increment(Long id, Long productId);

    void save(ShoppingCart cart);

    ShoppingCart findById(Long id);

    void decrement(Long id);



}

