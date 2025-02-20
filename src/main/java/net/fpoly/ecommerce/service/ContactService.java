package net.fpoly.ecommerce.service;

import net.fpoly.ecommerce.model.Contact;
import net.fpoly.ecommerce.model.request.ContactRequest;

public interface ContactService {
    Contact createContact(ContactRequest request);
}
