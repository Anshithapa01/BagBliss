package com.ecommerce.library.service.impl;

import com.ecommerce.library.model.Customer;
import com.ecommerce.library.model.Product;
import com.ecommerce.library.model.ShoppingCart;
import com.ecommerce.library.repository.CustomerRepository;
import com.ecommerce.library.repository.ShopingCartRepository;
import com.ecommerce.library.service.CustomerService;
import com.ecommerce.library.service.ProductService;
import com.ecommerce.library.service.ShoppingCartService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
   private ShopingCartRepository shopingCartRepository;
   private ProductService productService;

   private CustomerRepository customerRepository;




    public ShoppingCartServiceImpl(ShopingCartRepository shopingCartRepository, CustomerService customerService,
                                   ProductService productService, CustomerRepository customerRepository
                                  ) {
        this.shopingCartRepository = shopingCartRepository;
        this.productService = productService;
        this.customerRepository=customerRepository;

    }




    @Override
    public ShoppingCart addItemToCart(Long productId, int quantity, Long id) {;
        ShoppingCart shoppingCarts=shopingCartRepository.findByUsersProduct(id,productId);
        Product product=productService.getProductById(productId);


    if (shoppingCarts != null) {

            int oldQuantity = shoppingCarts.getQuantity();
            shoppingCarts.setQuantity(oldQuantity + quantity);
            shoppingCarts.setUnitPrice(product.getSalePrice());
            double totalPrice = product.getSalePrice() * (oldQuantity + quantity);
            shoppingCarts.setTotalPrice(totalPrice);
            shoppingCarts.setDeleted(false);

    }

    else {

            shoppingCarts = new ShoppingCart();

            shoppingCarts.setProduct(product);
            shoppingCarts.setCustomer(customerRepository.getById(id));
            shoppingCarts.setQuantity(quantity);
            shoppingCarts.setUnitPrice(product.getSalePrice());
            double totalPrice = shoppingCarts.getUnitPrice() * shoppingCarts.getQuantity();
            shoppingCarts.setTotalPrice(totalPrice);
            shoppingCarts.setDeleted(false);

    }
        return shopingCartRepository.save(shoppingCarts);
    }





    @Override
    public List<ShoppingCart> findShoppingCartByCustomer(String email) {
        return shopingCartRepository.findShoppingCartByCustomer(email);
    }




    @Override
    public void deleteById(long id) {
        ShoppingCart shoppingCart = shopingCartRepository.getReferenceById(id);
        System.out.println(id);
        shopingCartRepository.deleteShoppingCartItemById(id);
        shopingCartRepository.save(shoppingCart);
    }




    @Override
    public Double grandTotal(String username) {
        Customer customer = customerRepository.findByEmail(username);

        if (customer != null) {
            Long customerId = customer.getCustomer_id();
            return shopingCartRepository.findGrandTotal(customerId);
        }

        return 0.0;
    }




    @Override
    public void increment(Long id,Long productId) {
        ShoppingCart shoppingCart1=shopingCartRepository.getReferenceById(id);
        Product product=productService.getProductById(productId);
        if(product.getCurrentQuantity()>shoppingCart1.getQuantity()) {
            shoppingCart1.setQuantity(shoppingCart1.getQuantity() + 1);
            shoppingCart1.setTotalPrice(shoppingCart1.getQuantity() * shoppingCart1.getUnitPrice());
            shopingCartRepository.save(shoppingCart1);
        }
    }




    @Override
    public void decrement(Long id) {
        ShoppingCart shoppingCart1=shopingCartRepository.getReferenceById(id);
        if(shoppingCart1.getQuantity()>1) {
            shoppingCart1.setQuantity(shoppingCart1.getQuantity() - 1);
            shoppingCart1.setTotalPrice(shoppingCart1.getQuantity() * shoppingCart1.getUnitPrice());
            shopingCartRepository.save(shoppingCart1);
        }
    }
}
