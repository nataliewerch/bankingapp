package org.example.com.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "manager")
public class Manager {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotBlank
    @Column(name = "first_name")
    private String firstname;

    @NotBlank
    @Column(name = "last_name")
    private String lastname;

    private String status;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "manager_id")
    private List<Product> products = new ArrayList<>();

    @OneToMany(mappedBy = "manager", cascade = CascadeType.ALL)
    private List<Client> clients;
}
