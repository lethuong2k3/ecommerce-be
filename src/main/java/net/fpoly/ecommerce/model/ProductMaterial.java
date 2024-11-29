package net.fpoly.ecommerce.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "product_material")
@Data
public class ProductMaterial {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    Product product;

    @ManyToOne
    @JoinColumn(name = "material_id")
    Material material;

    @Column
    private int status;
}
