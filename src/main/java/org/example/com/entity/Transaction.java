package org.example.com.entity;

import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.UUID;

@Data
@NoArgsConstructor
@Entity
@Table(name = "transaction")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String type;
    private double amount;
    private String description;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = new Timestamp(System.currentTimeMillis());
    }

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "debit_account_id", referencedColumnName = "id")
    private Account accountDebit;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "credit_account_id", referencedColumnName = "id")
    private Account accountCredit;
}
