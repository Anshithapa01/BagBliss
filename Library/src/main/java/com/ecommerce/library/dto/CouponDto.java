package com.ecommerce.library.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CouponDto {
    private Long id;

    @NotBlank(message = "Code is Required")
    private String couponCode;

    @NotBlank(message = "Description is Required")
    private String couponDescription;

    @NotBlank(message = "Percentage amount is Required")
    private String offerPercentage;

    @DateTimeFormat(pattern = "yyyy/mm/dd")
    private LocalDate startDate;

    @DateTimeFormat(pattern = "yyyy/mm/dd")
    private LocalDate expireDate;

    @NotNull(message = "Minimum order amount is required")
    @DecimalMin(value = "0.01" ,message = "Required")
    private Double minimumOrderAmount;

    @NotNull(message = "Maximum offer amount is required")
    @DecimalMin(value = "0.01" ,message = "Required")
    private Double maximumOfferAmount;

    @Min(value = 1 ,message = "Count is Required")
    private int count;

    private boolean enable;
}
