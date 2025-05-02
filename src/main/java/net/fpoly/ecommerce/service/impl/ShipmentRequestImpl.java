package net.fpoly.ecommerce.service.impl;

import net.fpoly.ecommerce.model.Shipment;
import net.fpoly.ecommerce.model.Users;
import net.fpoly.ecommerce.model.request.ShipmentRequest;
import net.fpoly.ecommerce.repository.ShipmentRepo;
import net.fpoly.ecommerce.service.ShipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShipmentRequestImpl implements ShipmentService {

    @Autowired
    private ShipmentRepo shipmentRepo;

    @Override
    public Shipment createShipment(ShipmentRequest shipmentRequest, Users user) {
        Shipment shipment= new Shipment();
        shipment.setFirstName(shipmentRequest.getFirstName());
        shipment.setLastName(shipmentRequest.getLastName());
        shipment.setCompanyName(shipmentRequest.getCompanyName());
        shipment.setCountry(shipmentRequest.getCountry());
        shipment.setAddress(shipmentRequest.getAddress());
        shipment.setCity(shipmentRequest.getCity());
        shipment.setState(shipmentRequest.getState());
        shipment.setPhone(shipmentRequest.getPhone());
        shipment.setZipCode(shipmentRequest.getZipCode());
        shipment.setEmail(shipmentRequest.getEmail());
        shipment.setNotes(shipmentRequest.getNotes());
        return shipmentRepo.save(shipment);
    }
}
