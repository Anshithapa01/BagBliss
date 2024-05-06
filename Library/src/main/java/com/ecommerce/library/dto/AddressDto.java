package com.ecommerce.library.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class AddressDto {

    private Long id;

    @NotBlank(message = "Address cannot be Blank")
    private String addressLine1;

    @NotBlank(message = "Address cannot be Blank")
    private String addressLine2;

    @NotBlank(message = "City cannot be Blank")
    private String city;

    @NotBlank(message = "District cannot be Blank")
    private String district;

    @NotBlank(message = "State cannot be Blank")
    private String state;

    @NotBlank(message = "Country cannot be Blank")
    private String country;

    @Min(value = 100000,message = "Min 6 letters")
    @Max(value = 999999,message = "Only 6 letters")
    private int pincode;

    private boolean defaultAddress;

}
