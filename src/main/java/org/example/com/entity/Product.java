package org.example.com.entity;

import lombok.*;
import org.example.com.entity.enums.CurrencyCode;
import org.example.com.entity.enums.ProductStatus;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a product entity.
 *
 * @author Natalie Werch
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private ProductStatus status;

    @Column(name = "currency_code")
    @Enumerated(EnumType.STRING)
    private CurrencyCode currencyCode;

    @Column(name = "interest_rate")
    private BigDecimal interestRate;

    @Column(name = "limit_prod")
    private Integer limit;

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

    @ManyToOne
    @JoinColumn(name = "manager_id", referencedColumnName = "id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Manager manager;

    @OneToMany(mappedBy = "product")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Agreement> agreements = new ArrayList<>();
}
