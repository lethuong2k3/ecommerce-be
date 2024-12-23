package net.fpoly.ecommerce.repository;

import net.fpoly.ecommerce.model.OrderItem;
import net.fpoly.ecommerce.model.OrderStatus;
import net.fpoly.ecommerce.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Repository
public interface OrderItemRepo extends JpaRepository<OrderItem, Long> {
    OrderItem findByOrders_IdAndProductDetail_Id(Long orderId, Long detailId);

    @Query(value = "select ot from OrderItem ot join ot.orders o where o.user = :user and o.orderStatus = :orderStatus")
    List<OrderItem> findAllByUserAndOrderStatus(@PathVariable Users user, @PathVariable OrderStatus orderStatus);
}
