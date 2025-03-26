package net.fpoly.ecommerce.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "payment_type")
@Data
public class PaymentType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String value;

    @Column
    private String imageUrl;

    @Column
    private String description;
}
