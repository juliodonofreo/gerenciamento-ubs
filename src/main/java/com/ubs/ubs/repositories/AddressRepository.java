package com.ubs.ubs.repositories;

import com.ubs.ubs.entities.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {
}
