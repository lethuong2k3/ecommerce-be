package net.fpoly.ecommerce.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.fpoly.ecommerce.model.Color;
import net.fpoly.ecommerce.model.Product;
import net.fpoly.ecommerce.model.ProductDetail;
import net.fpoly.ecommerce.model.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDetailResponse {
    private Long id;

    ProductResponse product;

    private double price;

    Size size;

    Color color;

    private int amount;

    private int status;

    public static ProductDetailResponse convertToProductDetailResponse(ProductDetail productDetail) {
        ProductResponse productResponse = new ProductResponse().convertToProductResponse(productDetail.getProduct());
        return ProductDetailResponse.builder()
                .id(productDetail.getId())
                .product(productResponse)
                .price(productDetail.getPrice())
                .size(productDetail.getSize())
                .color(productDetail.getColor())
                .amount(productDetail.getAmount())
                .status(productDetail.getStatus())
                .build();
    }

}
