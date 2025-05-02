package net.fpoly.ecommerce.service;

import net.fpoly.ecommerce.model.Shipment;
import net.fpoly.ecommerce.model.Users;
import net.fpoly.ecommerce.model.request.ShipmentRequest;

public interface ShipmentService {
    Shipment createShipment(ShipmentRequest shipment, Users user);
}
