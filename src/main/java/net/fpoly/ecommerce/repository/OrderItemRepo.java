package net.fpoly.ecommerce.repository;

import net.fpoly.ecommerce.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepo extends JpaRepository<OrderItem, Long> {
    OrderItem findByOrders_IdAndProductDetail_Id(Long orderId, Long detailId);
}
