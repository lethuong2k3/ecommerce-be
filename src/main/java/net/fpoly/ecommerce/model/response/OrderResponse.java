package net.fpoly.ecommerce.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.fpoly.ecommerce.model.Order;
import net.fpoly.ecommerce.model.OrderItem;
import net.fpoly.ecommerce.model.OrderStatus;
import net.fpoly.ecommerce.model.Users;

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

    private LocalDateTime expirationTime;

    private Double totalAmount;

    private OrderStatus orderStatus;

    private List<OrderItemResponse> orderItems;

    public static OrderResponse convertToOrderResponse(Order order) {
        List<OrderItemResponse> orderItems = order.getOrderItems().stream().map(OrderItemResponse::convertToOrderItemResponse).toList();
        return OrderResponse.builder()
                .id(order.getId())
                .orderDate(order.getOrderDate())
                .totalAmount(order.getTotalAmount())
                .orderStatus(order.getOrderStatus())
                .orderItems(orderItems)
                .build();
    }


}
