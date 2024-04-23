package com.ecommerce.library.dto;

import com.ecommerce.library.model.Customer;
import com.ecommerce.library.model.Product;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
@Setter
public class ShoppingCartDto {
    private int id;

    private Customer customer;
    private Product product;
    private int quantity;
    private double unitPrice;
    private double totalPrice;
    private boolean deleted;

}
