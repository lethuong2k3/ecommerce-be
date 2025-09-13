package net.fpoly.ecommerce.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.fpoly.ecommerce.model.*;

import java.math.BigDecimal;
import java.time.Instant;
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

    private Long orderCode;

    private Instant orderDate;

    private BigDecimal totalAmount;

    private OrderStatus orderStatus;

    private List<OrderItemResponse> orderItems;

    private List<OrderHistory> orderHistories;

    private Payment payment;

    private Shipment shipment;

    public static OrderResponse convertToOrderResponse(Order order) {
        return OrderResponse.builder()
                .id(order.getId())
                .orderCode(order.getOrderCode())
                .orderDate(order.getOrderDate())
                .totalAmount(order.getTotalAmount())
                .orderStatus(order.getOrderStatus())
                .payment(order.getPayment())
                .build();
    }

    public static OrderResponse convertToCartResponse(Order order) {
        List<OrderItemResponse> orderItems = order.getOrderItems().stream().map(OrderItemResponse::convertToOrderItemResponse).toList();
        return OrderResponse.builder()
                .id(order.getId())
                .totalAmount(order.getTotalAmount())
                .orderItems(orderItems)
                .build();
    }

    public static OrderResponse convertToOrderDetailResponse(Order order) {
        List<OrderItemResponse> orderItems = order.getOrderItems().stream().map(OrderItemResponse::convertToOrderItemResponse).toList();
        return OrderResponse.builder()
                .id(order.getId())
                .orderCode(order.getOrderCode())
                .orderDate(order.getOrderDate())
                .totalAmount(order.getTotalAmount())
                .orderStatus(order.getOrderStatus())
                .payment(order.getPayment())
                .orderItems(orderItems)
                .shipment(order.getShipment())
                .orderHistories(order.getOrderHistories())
                .build();
    }

}
