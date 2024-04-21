package com.ecommerce.customer.Customer.config;

import com.ecommerce.customer.Customer.exception.CustomerBlockedException;
import com.ecommerce.library.model.Customer;
import com.ecommerce.library.model.Role;
import com.ecommerce.library.repository.CustomerRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerDetailService implements UserDetailsService {

    private CustomerRepository customerRepository;


    public CustomerDetailService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Customer customer = customerRepository.findByEmail(email);

            if(customer !=null) {
                if (customer.isActivated()) {

                    List<GrantedAuthority> authorities = new ArrayList<>();
                    for (Role role : customer.getRoles()) {
                        authorities.add(new SimpleGrantedAuthority(role.getName()));
                    }

                    return new CustomerDetails(
                            customer.getEmail(),
                            customer.getPassword(),
                            authorities,
                            customer.getName(),
                            customer.getMobile(),
                            customer.isActivated());


                } else {

                    throw new CustomerBlockedException("Your account has been blocked. Please contact support.");
                }
            }else{
                throw new UsernameNotFoundException("Invalid username or password.");
            }

    }
}
