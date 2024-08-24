package com.matjar.user.service;

import com.matjar.user.model.Address;
import com.matjar.user.repository.AddressRepository;
import org.springframework.stereotype.Service;

@Service
public class AddressService {

    private final AddressRepository addressRepository;

    public AddressService(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    public Address insert(Address address){
        return addressRepository.save(address);
    }
}
