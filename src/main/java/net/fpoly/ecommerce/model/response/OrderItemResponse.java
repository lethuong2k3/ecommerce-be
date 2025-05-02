package net.fpoly.ecommerce.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.fpoly.ecommerce.model.OrderItem;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemResponse {
    private Long id;

    private ProductDetailResponse productDetail;

    private Integer quantity;

    private BigDecimal itemPrice;

    private BigDecimal totalPrice;

    public static OrderItemResponse convertToOrderItemResponse(OrderItem orderItem) {
        ProductDetailResponse productDetailResponse = new ProductDetailResponse().convertToProductDetailResponse(orderItem.getProductDetail());
        return OrderItemResponse.builder()
                .id(orderItem.getId())
                .productDetail(productDetailResponse)
                .quantity(orderItem.getQuantity())
                .itemPrice(orderItem.getItemPrice())
                .totalPrice(orderItem.getTotalPrice())
                .build();
    }
}
