package com.ecommerce.library.dto;

import com.ecommerce.library.model.Category;
import com.ecommerce.library.model.Image;
import jakarta.validation.constraints.Min;
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

    @NotBlank(message = "Write something about product")
    private String description;

    @NotBlank(message = "Description cannot be null")
    private String long_description;

    @Min(value = 1, message = "Quantity required")
    private int currentQuantity;

    @Min(value = 1, message = "costPrice required")
    private double costPrice;

    @Min(value = 1, message = "salePrice required")
    private double salePrice;

    private List<Image> image;

    private Category category;

    private boolean activated;

    private boolean deleted;
}
