package net.fpoly.ecommerce.repository;

import net.fpoly.ecommerce.model.PaymentInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentInfoRepo extends JpaRepository<PaymentInfo, Long> {
}
