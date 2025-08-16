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

import java.util.Date;
import java.util.List;


@Repository
public interface OrderRepo extends JpaRepository<Order, Long> {
    Order findByUserAndOrderStatus(Users user, OrderStatus orderStatus);
    List<Order> findByUserAndOrderStatusNot(Users user, OrderStatus orderStatus);
    @Query("""
        SELECT o from Order o WHERE (
                                  :keyword IS NULL OR
                                  CAST(o.id AS string) LIKE %:keyword% OR
                                  UPPER(o.orderStatus) LIKE CONCAT('%', UPPER(:keyword), '%') OR
                                  UPPER(o.payment.paymentType.value) LIKE CONCAT('%', UPPER(:keyword), '%')
                                    )
                                 AND (:startDate IS NULL OR o.orderDate >= :startDate)\s
                                 AND (:endDate IS NULL OR o.orderDate <= :endDate)\s
                                 AND o.user = :user
                                 AND o.orderStatus <> 'CART'
    """)
    Page<Order> findByKeywordAndBetweenDate(
            @Param("keyword") String keyword,
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate,
            @Param("user") Users user,
            Pageable pageable
    );}


