package com.mydukaan.repository;

import com.mydukaan.model.LedgerEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LedgerEntriesRepository extends JpaRepository<LedgerEntry, Long> {
    List<LedgerEntry> findByLedgerId(Long ledgerId);

    @Query("""
            SELECT l FROM LedgerEntry as l
            WHERE l.ledger.id = :ledgerId AND l.type = "OPENING_BALANCE"
            """)
    LedgerEntry findByLedgerIdWithOpeningBalance(Long ledgerId);

    void deleteByPaymentId(Long id);
}
