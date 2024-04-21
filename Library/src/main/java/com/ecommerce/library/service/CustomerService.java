package com.ecommerce.library.service;

import com.ecommerce.library.dto.CustomerDto;
import com.ecommerce.library.model.Customer;
import jakarta.validation.Valid;

import java.util.List;

public interface CustomerService {
    Customer findByEmail(String email);
    void saveCustomer(@Valid CustomerDto customerDto);
List<Customer> findAll();

    void blockById(Long id);

    void enableById(Long id);

    void changePassword(Customer customer);
    List<Customer> findAllActivatedByTrue();



    Customer getById(Long id);
     void updateResetPasswordToken(String token, String email);
    Customer getByResetPasswordToken(String token);
    void updatePassword(Customer customer, String newPassword);
    Customer getByReferalToken(String token);
    void updateReferalCodeToken(String token,String email);


}
