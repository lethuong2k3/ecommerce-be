package net.fpoly.ecommerce.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "shipment")
@Data
public class Shipment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    private String firstName;

    @Column(length = 100, nullable = false)
    private String lastName;

    @Column(length = 100, nullable = false)
    private String address;

    @Column(length = 50, nullable = false)
    private String idAddress;

    @Column(length = 20, nullable = false)
    private String phone;

    @Column(length = 100, nullable = false)
    private String email;

    @Column()
    private String notes;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private Users user;

    @OneToMany(mappedBy = "shipment")
    @JsonIgnore
    private List<Order> orders;
}
