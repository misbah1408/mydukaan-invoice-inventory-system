package com.mydukaan.model;

import com.mydukaan.enums.AccountType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ledger")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Ledger {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "display_name", nullable = false)
    private String displayName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountType accountType;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal balance;

    @OneToMany(mappedBy = "ledger", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Payment> transactions = new ArrayList<>();

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public void addTransaction(Payment payment) {
        transactions.add(payment);
        payment.setLedger(this);
    }

    public void removeTransaction(Payment payment) {
        transactions.remove(payment);
        payment.setLedger(null);
    }

    @Override
    public String toString() {
        return "Ledger{" +
                "id=" + id +
                ", displayName='" + displayName + '\'' +
                ", accountType=" + accountType +
                ", balance=" + balance +
                '}';
    }
}