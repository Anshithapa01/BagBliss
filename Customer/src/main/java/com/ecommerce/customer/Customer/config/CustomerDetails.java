package com.ecommerce.customer.Customer.config;

import com.ecommerce.library.model.Customer;
import com.ecommerce.library.model.Role;
import com.ecommerce.library.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class CustomerDetails extends User implements Serializable {

    private String name;

    private long mobile;

    private boolean is_activated;

    public CustomerDetails(String email, String password, Collection<? extends GrantedAuthority> authorities,
                           String name, Long mobile,boolean is_activated) {
        super(email, password, authorities);
        this.name=name;
        this.mobile = mobile;
        this.is_activated= is_activated;
    }
}
