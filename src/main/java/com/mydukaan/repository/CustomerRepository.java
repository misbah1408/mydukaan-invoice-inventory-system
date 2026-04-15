package com.mydukaan.repository;

import com.mydukaan.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    List<Customer> findByStoreId(Long storeId);
}
