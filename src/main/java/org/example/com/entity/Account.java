package org.example.com.entity;

import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@Entity
@Table(name = "accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String name;
    private String type;
    private String status;
    private Double balance;

    @Column(name = "currency_code")
    private String currencyCode;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = new Timestamp(System.currentTimeMillis());
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = new Timestamp(System.currentTimeMillis());
    }

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "client_id", referencedColumnName = "id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Client client;

    @OneToMany(mappedBy = "accountDebit")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Transaction> debitTransactions = new ArrayList<>();

    @OneToMany(mappedBy = "accountCredit")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Transaction> CreditTransactions = new ArrayList<>();
}
