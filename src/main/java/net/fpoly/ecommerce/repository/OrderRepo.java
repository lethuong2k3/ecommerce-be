package net.fpoly.ecommerce.repository;

import net.fpoly.ecommerce.model.Order;
import net.fpoly.ecommerce.model.OrderStatus;
import net.fpoly.ecommerce.model.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;


@Repository
public interface OrderRepo extends JpaRepository<Order, Long> {
    Order findByUserAndOrderStatus(Users user, OrderStatus orderStatus);
    @Query("""
        SELECT o from Order o WHERE (
                                  :keyword IS NULL OR
                                  CAST(o.orderCode AS string) LIKE %:keyword% OR
                                  UPPER(o.orderStatus) LIKE CONCAT('%', UPPER(:keyword), '%') OR
                                  UPPER(o.payment.paymentType.value) LIKE CONCAT('%', UPPER(:keyword), '%')
                                    )
                                 AND (:startDate IS NULL OR o.orderDate >= :startDate)
                                 AND (:endDate IS NULL OR o.orderDate <= :endDate)
                                 AND o.user = :user
                                 AND o.orderStatus <> 'CART'
    """)
    Page<Order> findByKeywordAndBetweenDate(
            @Param("keyword") String keyword,
            @Param("startDate") Instant startDate,
            @Param("endDate") Instant endDate,
            @Param("user") Users user,
            Pageable pageable
    );
    @Query("select o from Order o where o.orderStatus = 'WAITING' and o.expiresAt < :now")
    List<Order> findExpiredWaiting(Instant now);
    Optional<Order> findByOrderCode(Long orderCode);
}


