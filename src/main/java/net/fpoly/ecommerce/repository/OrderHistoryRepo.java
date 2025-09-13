package net.fpoly.ecommerce.repository;

import net.fpoly.ecommerce.model.OrderHistory;
import net.fpoly.ecommerce.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderHistoryRepo extends JpaRepository<OrderHistory, Long> {
    @Query("""
        SELECT oh
        FROM OrderHistory oh
        WHERE oh.order.orderCode = :orderCode AND oh.order.user = :user
        ORDER BY oh.changedAt ASC
    """)
    List<OrderHistory> findByOrderCode(@Param("orderCode") Long orderCode, @Param("user") Users user);
}
