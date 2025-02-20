package net.fpoly.ecommerce.service.impl;

import lombok.AllArgsConstructor;
import net.fpoly.ecommerce.model.Contact;
import net.fpoly.ecommerce.model.request.ContactRequest;
import net.fpoly.ecommerce.repository.ContactRepo;
import net.fpoly.ecommerce.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContactServiceImpl implements ContactService {

    @Autowired
    private ContactRepo repo;

    @Override
    public Contact createContact(ContactRequest request) {
        Contact contact = new Contact();
        contact.setName(request.getName());
        contact.setEmail(request.getEmail());
        contact.setMessage(request.getMessage());
        return repo.save(contact);
    }
}
