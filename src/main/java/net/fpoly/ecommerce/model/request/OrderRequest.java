package net.fpoly.ecommerce.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.fpoly.ecommerce.model.OrderItem;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderRequest {

    private List<OrderItem> orderItems;

    private ShipmentRequest shipmentRequest;

    private Long paymentTypeId;

    private String sortBy;

    private BigDecimal shippingFee;

}
