package net.fpoly.ecommerce.repository;

import net.fpoly.ecommerce.model.Order;
import net.fpoly.ecommerce.model.OrderStatus;
import net.fpoly.ecommerce.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepo extends JpaRepository<Order, Long> {
    Order findByUserAndOrderStatus(Users user, OrderStatus orderStatus);
    List<Order> findByOrderStatus(OrderStatus orderStatus);
}
