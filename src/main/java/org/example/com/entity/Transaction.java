package org.example.com.entity;

import lombok.*;
import org.example.com.entity.enums.TransactionType;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.UUID;

/**
 * Represents a transaction entity.
 *
 * @author Natalie Werch
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Enumerated(EnumType.STRING)
    private TransactionType type;

    private Double amount;

    private String description;

    @Column(name = "created_at")
    private Timestamp createdAt;

    public Transaction(TransactionType type, Double amount, String description, Account accountDebit, Account accountCredit) {
        this.type = type;
        this.amount = amount;
        this.description = description;
        this.accountDebit = accountDebit;
        this.accountCredit = accountCredit;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = new Timestamp(System.currentTimeMillis());
    }

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "debit_account_id", referencedColumnName = "id")
    private Account accountDebit;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "credit_account_id", referencedColumnName = "id")
    private Account accountCredit;
}
