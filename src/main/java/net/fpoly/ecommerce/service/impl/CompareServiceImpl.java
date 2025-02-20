package net.fpoly.ecommerce.service.impl;

import lombok.RequiredArgsConstructor;
import net.fpoly.ecommerce.model.Compare;
import net.fpoly.ecommerce.model.Users;
import net.fpoly.ecommerce.model.request.CompareRequest;
import net.fpoly.ecommerce.repository.CompareRepo;
import net.fpoly.ecommerce.repository.UserRepo;
import net.fpoly.ecommerce.service.CompareService;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CompareServiceImpl implements CompareService {

    private final CompareRepo compareRepo;

    private final UserRepo userRepo;

    @Override
    public Compare createCompare(CompareRequest request, Principal principal) {
        Users user = userRepo.findByEmail(principal.getName());
        Compare compare = new Compare();
        compare.setUser(user);
        compare.setProduct(request.getProduct());
        compare.setCreatedAt(new Date());
        return compareRepo.save(compare);
    }

    @Override
    public List<Compare> findAllByUser(Principal principal) {
        Users user = userRepo.findByEmail(principal.getName());
        return compareRepo.findByUser(user);
    }

    @Override
    public void deleteCompare(CompareRequest request, Principal principal) {
        Users user = userRepo.findByEmail(principal.getName());
        Compare compare = compareRepo.findByIdAndUser(request.getId(), user);
        compareRepo.delete(compare);
    }

    @Override
    public void deleteAllCompare(List<CompareRequest> requests, Principal principal) {
        requests.forEach(request -> deleteCompare(request, principal));
    }
}
