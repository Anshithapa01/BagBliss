package com.ecommerce.library.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CustomerDto {
    private Long customer_id;
    @NotBlank(message = "Enter Name")
    private String name;
    @NotBlank(message = "Enter email")
    private String email;

    @NotBlank(message = "Required")
    @Size(min = 8,message = "Minimum 8 letter")
    private String password;

    @NotBlank(message = "Required")
    private String repeatPassword;

    @NotNull(message = "Required")
    private Long mobile;

    private String role;
    private boolean activated;
    private boolean blocked;
    private long otp;



}
