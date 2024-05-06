package com.ecommerce.library.dto;

import com.ecommerce.library.model.Category;
import com.ecommerce.library.model.Product;
import jakarta.mail.Message;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class OfferDto {
    private Long id;

    @NotBlank(message = "Offer name is required")
    private String offerName;

    @NotBlank(message = "Description needed")
    private String description;

    @Min(value = 1,message = "Minimum of 1 Percentage needed")
    private int offerPercentage;

    private String offerType;

    private Category category;

    private boolean active;

    private Product product;
}
