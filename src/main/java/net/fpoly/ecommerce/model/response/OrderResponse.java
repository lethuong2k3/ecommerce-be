package net.fpoly.ecommerce.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.fpoly.ecommerce.model.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponse {
    private Long id;

    private Date orderDate;

    private BigDecimal totalAmount;

    private OrderStatus orderStatus;

    private List<OrderItemResponse> orderItems;

    private Payment payment;

    private Shipment shipment;

    public static OrderResponse convertToOrderResponse(Order order) {
        List<OrderItemResponse> orderItems = order.getOrderItems().stream().map(OrderItemResponse::convertToOrderItemResponse).toList();
        return OrderResponse.builder()
                .id(order.getId())
                .orderDate(order.getOrderDate())
                .totalAmount(order.getTotalAmount())
                .orderStatus(order.getOrderStatus())
                .orderItems(orderItems)
                .payment(order.getPayment())
                .shipment(order.getShipment())
                .build();
    }


}
