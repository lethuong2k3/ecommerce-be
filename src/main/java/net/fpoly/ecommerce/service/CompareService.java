package net.fpoly.ecommerce.service;

import net.fpoly.ecommerce.model.Compare;
import net.fpoly.ecommerce.model.request.CompareRequest;

import java.security.Principal;
import java.util.List;

public interface CompareService {
    Compare createCompare(CompareRequest request, Principal principal);

    List<Compare> findAllByUser(Principal principal);

    void deleteCompare(CompareRequest request, Principal principal);

    void deleteAllCompare(List<CompareRequest> requests, Principal principal);
}
