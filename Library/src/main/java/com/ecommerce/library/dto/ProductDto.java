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

    @NotBlank(message ="Name cannot be null" )
    @Size(min = 3,message = "minimum 3 letter")
    private String name;

    @NotBlank
    private String description;

    @NotBlank
    private String long_description;

    @NotBlank
    private int currentQuantity;

    @NotBlank
    private double costPrice;

    private double salePrice;

    @NotBlank
    private List<Image> image;

    @NotBlank
    private Category category;

    private boolean activated;

    private boolean deleted;
}
