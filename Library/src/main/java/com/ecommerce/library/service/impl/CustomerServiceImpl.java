package com.ecommerce.library.service.impl;

import com.ecommerce.library.dto.CustomerDto;
import com.ecommerce.library.model.Customer;
import com.ecommerce.library.repository.CustomerRepository;
import com.ecommerce.library.service.CustomerService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {

    private CustomerRepository customerRepository;

@Autowired
    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;

    }



    @Override
    public Customer findByEmail(String email) {
        return customerRepository.findByEmail(email);
    }

    @Override
    public void saveCustomer(@Valid CustomerDto customerDto) {

        Customer customer=new Customer();

        customer.setName(customerDto.getName());
        customer.setEmail(customerDto.getEmail());
        customer.setPassword(customerDto.getPassword());
        customer.setMobile(customerDto.getMobile());
        customer.setRole("User");
        customer.setActivated(true);
        customer.setBlocked(false);


        customerRepository.save(customer);
    }




    @Override
    public int hashCode() {
        return super.hashCode();
    }

   @Override
    public void blockById(Long id) {
    Customer customer=customerRepository.getById(id);
    customer.setActivated(false);
    customer.setBlocked(true);
    customerRepository.save(customer);
    }

    @Override
    public void enableById(Long id) {
        Customer customer=customerRepository.getById(id);
        customer.setActivated(true);
        customer.setBlocked(false);
        customerRepository.save(customer);

    }

    @Override
    public List<Customer> findAllActivatedByTrue() {
        return customerRepository.findAllByActivatedTrue();
    }

    @Override
    public List<Customer> findAll() {
    return customerRepository.findAll();
    }



    @Override
    public Customer getById(Long id) {
        return customerRepository.getById(id);
    }

    @Override
    public void changePassword(Customer customer) {
        Customer customer1=customerRepository.findByEmail(customer.getEmail());
        customer1.setPassword(customer.getPassword());
        customerRepository.save(customer1);
    }
    @Override
    public void updateResetPasswordToken(String token, String email)  {
        Customer customer = customerRepository.findByEmail(email);
        if (customer != null) {
            customer.setResetPasswordToken(token);
            customerRepository.save(customer);
        }
    }

    public Customer getByResetPasswordToken(String token) {
        return customerRepository.findByResetPasswordToken(token);
    }

    @Override
    public void updateReferalCodeToken(String token, String email) {
        Customer customer=customerRepository.findByEmail(email);
        if(customer!=null){
            customer.setReferralToken(token);
            customerRepository.save(customer);
        }
    }

    @Override
    public Customer getByReferalToken(String token) {
        return customerRepository.findByReferralToken(token);
    }

    public void updatePassword(Customer customer, String newPassword) {
        //BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
       // String encodedPassword = passwordEncoder.encode(newPassword);
        customer.setPassword(newPassword);

        customer.setResetPasswordToken(null);
        customerRepository.save(customer);
    }


    @Override
    public Customer update(String email, String name, Long mobile) {
        Customer customer = customerRepository.findByEmail(email);
        customer.setName(name);
        customer.setMobile(mobile);
        return customerRepository.save(customer);
    }
}
