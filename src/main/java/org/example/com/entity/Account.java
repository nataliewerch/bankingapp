package org.example.com.entity;

import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.UUID;

@Data
@NoArgsConstructor
@Entity
@Table(name = "account")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String name;
    private String type;
    private String status;
    private double balance;

    @Column(name = "currency_code")
    private String currencyCode;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;


    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "client_id", referencedColumnName = "id")
    private Client client;
}
