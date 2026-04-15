package com.mydukaan.repository;

import com.mydukaan.model.Ledger;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LedgerRepository extends JpaRepository<Ledger, Long> {
    Optional<List<Ledger>> findByStoreId(Long storeId);
}
