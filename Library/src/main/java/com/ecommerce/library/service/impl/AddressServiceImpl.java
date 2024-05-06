package com.ecommerce.library.service.impl;

import com.ecommerce.library.dto.AddressDto;
import com.ecommerce.library.model.Address;
import com.ecommerce.library.model.Customer;
import com.ecommerce.library.repository.AddressRepository;
import com.ecommerce.library.service.AddressService;
import com.ecommerce.library.service.CustomerService;
import com.ecommerce.library.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AddressServiceImpl implements AddressService {

    CustomerService customerService;

    AddressRepository addressRepository;


    @Autowired
    public AddressServiceImpl(CustomerService customerService, AddressRepository addressRepository) {
        this.customerService = customerService;
        this.addressRepository = addressRepository;
    }



    @Override
    public Address save(AddressDto addressDto, String email) {
        Customer customer = customerService.findByEmail(email);

        // Create a new address entity
        Address address = new Address();
        address.setAddressLine1(addressDto.getAddressLine1());
        address.setAddressLine2(addressDto.getAddressLine2());
        address.setCity(addressDto.getCity());
        address.setState(addressDto.getState());
        address.setCountry(addressDto.getCountry());
        address.setDistrict(addressDto.getDistrict());
        address.setPincode(addressDto.getPincode());
        address.setCustomer(customer);

        // Check if the checkbox is checked to set as default address
        if (addressDto.isDefaultAddress()) {
            // Set the new address as default
            address.set_default(true);

            // Find and unset any existing default address for the customer
            customer.getAddress().stream()
                    .filter(Address::is_default)
                    .findFirst()
                    .ifPresent(defaultAddress -> {
                        defaultAddress.set_default(false);
                        addressRepository.save(defaultAddress);
                    });
        }

        return addressRepository.save(address);
    }


    @Override
    public Optional<Address> findByid(Long id) {
        return addressRepository.findById(id);
    }

    @Override
    public Address update(AddressDto addressDto) {
        Address address=addressRepository.getReferenceById(addressDto.getId());
        address.setAddressLine1(addressDto.getAddressLine1());
        address.setAddressLine2(addressDto.getAddressLine2());
        address.setCity(addressDto.getCity());
        address.setState(addressDto.getState());
        address.setDistrict(addressDto.getDistrict());
        address.setCountry(addressDto.getCountry());
        address.setPincode(addressDto.getPincode());

        if (addressDto.isDefaultAddress()) {
            // Find and unset any existing default address for the customer
            address.getCustomer().getAddress().stream()
                    .filter(Address::is_default)
                    .findFirst()
                    .ifPresent(defaultAddress -> {
                        defaultAddress.set_default(false);
                        addressRepository.save(defaultAddress);
                    });
            // Set the current address as default
            address.set_default(true);
        }

        return addressRepository.save(address);
    }

    @Override
    public Address findByIdOrder(Long id) {
        return addressRepository.getReferenceById(id);
    }

    @Override
    public List<Address> findAddressByCustomer(String email) {
        return addressRepository.findAddressByCustomer(email);
    }

    @Override
    public void deleteById(Long addressId) {
        addressRepository.deleteById(addressId);
    }


}
