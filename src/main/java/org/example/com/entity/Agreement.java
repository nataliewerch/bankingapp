package org.example.com.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(name = "agreement")
public class Agreement {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "interest_rate")
    private int interestRate;

    private String status;

    private double sum;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "account_id", referencedColumnName = "id")
    private Account account;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Product product;
}
