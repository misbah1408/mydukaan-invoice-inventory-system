package com.mydukaan.repository;

import com.mydukaan.model.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    List<Invoice> findByStoreId(Long storeId);

    List<Invoice> findByCustomerId(Long customerId);
}
