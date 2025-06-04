package net.fpoly.ecommerce.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "payment_info")
@Data
public class PaymentInfo {
    @Id
    private String orderId;

    private String requestId;

    @OneToMany(mappedBy = "paymentInfo")
    @JsonIgnore
    private List<Order> orders;
}
