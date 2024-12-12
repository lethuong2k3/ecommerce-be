package net.fpoly.ecommerce.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "order_item")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne()
    @JoinColumn(name = "product_id")
    private ProductDetail productDetail;

    @ManyToOne()
    @JoinColumn(name = "order_id")
    @JsonBackReference
    private Order orders;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private Double itemPrice;
}
