package net.fpoly.ecommerce.service.impl.order;

import net.fpoly.ecommerce.model.Shipment;
import net.fpoly.ecommerce.model.Users;
import net.fpoly.ecommerce.model.request.ShipmentRequest;
import net.fpoly.ecommerce.repository.ShipmentRepo;
import net.fpoly.ecommerce.service.ShipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShipmentServiceImpl implements ShipmentService {

    @Autowired
    private ShipmentRepo shipmentRepo;

    @Override
    public Shipment createShipment(ShipmentRequest shipmentRequest, Users user) {
        Shipment shipment= new Shipment();
        shipment.setFirstName(shipmentRequest.getFirstName());
        shipment.setLastName(shipmentRequest.getLastName());
        shipment.setAddress(shipmentRequest.getAddress());
        shipment.setPhone(shipmentRequest.getPhone());
        shipment.setEmail(shipmentRequest.getEmail());
        shipment.setNotes(shipmentRequest.getNotes());
        shipment.setIdAddress(shipmentRequest.getIdAddress());
        return shipmentRepo.save(shipment);
    }
}
