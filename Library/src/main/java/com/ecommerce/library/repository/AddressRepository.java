package com.ecommerce.library.repository;

import com.ecommerce.library.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepository extends JpaRepository<Address,Long> {

    @Query("select a from Address a where a.customer.email=?1")
    List<Address> findAddressByCustomer(String email);


}
