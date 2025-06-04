package net.fpoly.ecommerce.service;

import net.fpoly.ecommerce.model.Compare;
import net.fpoly.ecommerce.model.Product;
import net.fpoly.ecommerce.model.Users;
import net.fpoly.ecommerce.model.request.CompareRequest;

import java.security.Principal;
import java.util.List;

public interface CompareService {
    Compare createCompare(CompareRequest request, Principal principal);

    List<Compare> findAllByUserAndStatus(Principal principal);

    Compare findByProductAndUser(Product product, Principal principal);

    void deleteCompare(Long id, Principal principal);

    void deleteAllCompare(List<CompareRequest> requests, Principal principal);
}
