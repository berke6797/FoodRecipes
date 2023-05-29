package com.bilgeadam.service;

import com.bilgeadam.repository.IAddressRepository;
import com.bilgeadam.repository.entity.Address;
import com.bilgeadam.utility.ServiceManager;
import org.springframework.stereotype.Service;

@Service
public class AddressService extends ServiceManager<Address,Long> {
    private final IAddressRepository addressRepository;

    public AddressService(IAddressRepository addressRepository) {
        super(addressRepository);
        this.addressRepository = addressRepository;
    }
}
