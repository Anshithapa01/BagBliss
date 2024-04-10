package com.ecommerce.library.dto;

import com.ecommerce.library.model.Category;
import com.ecommerce.library.model.Image;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.awt.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    private Long id;

    @NotNull
    @Size(min = 1 , message = "Product name can't be null")
    private String name;

    private String description;

    private int currentQuantity;

    private double costPrice;

    private double salePrice;

    private List<Image> image;

    private Category category;

    private boolean activated;

    private boolean deleted;
}
