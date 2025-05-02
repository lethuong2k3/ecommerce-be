package net.fpoly.ecommerce.repository;

import net.fpoly.ecommerce.model.Shipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShipmentRepo extends JpaRepository<Shipment, Long> {
}
