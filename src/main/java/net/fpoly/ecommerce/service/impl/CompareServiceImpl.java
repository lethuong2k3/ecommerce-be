package net.fpoly.ecommerce.service.impl;

import lombok.RequiredArgsConstructor;
import net.fpoly.ecommerce.model.Compare;
import net.fpoly.ecommerce.model.Product;
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
        Compare isComapre = compareRepo.findByProductAndUser(request.getProduct(), user);
        if (isComapre != null) {
            isComapre.setStatus(1);
            return compareRepo.save(isComapre);
        }
        Compare compare = new Compare();
        compare.setUser(user);
        compare.setProduct(request.getProduct());
        compare.setCreatedAt(new Date());
        compare.setStatus(1);
        return compareRepo.save(compare);
    }

    @Override
    public List<Compare> findAllByUserAndStatus(Principal principal) {
        Users user = userRepo.findByEmail(principal.getName());
        return compareRepo.findByUserAndStatus(user, 1);
    }

    @Override
    public Compare findByProductAndUser(Product product, Principal principal) {
        Users user = userRepo.findByEmail(principal.getName());
        return compareRepo.findByProductAndUser(product, user);
    }

    @Override
    public void deleteCompare(Long id, Principal principal) {
        Users user = userRepo.findByEmail(principal.getName());
        Compare compare = compareRepo.findByIdAndUser(id, user);
        if (compare != null) {
            compare.setStatus(0);
            compareRepo.save(compare);
        }
    }

    @Override
    public void deleteAllCompare(List<CompareRequest> requests, Principal principal) {
        requests.forEach(request -> deleteCompare(request.getId(), principal));
    }
}
