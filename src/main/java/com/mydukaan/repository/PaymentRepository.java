package com.mydukaan.repository;

import com.mydukaan.enums.TransactionType;
import com.mydukaan.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByInvoiceId(Long id);

    @Query("SELECT p FROM Payment p WHERE p.ledger.id = :id AND p.type = 'OPENING_BALANCE'")
    Payment findOpeningBalanceByLedgerId(Long id);

    List<Payment> findByLedgerId(Long ledgerId);
}
