package net.fpoly.ecommerce.service.impl.order;

import net.fpoly.ecommerce.model.OrderHistory;
import net.fpoly.ecommerce.model.Users;
import net.fpoly.ecommerce.repository.OrderHistoryRepo;
import net.fpoly.ecommerce.repository.UserRepo;
import net.fpoly.ecommerce.service.OrderHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
public class OrderHistoryImpl implements OrderHistoryService {

    @Autowired
    private OrderHistoryRepo repo;

    @Autowired
    private UserRepo userRepo;

    @Override
    public List<OrderHistory> getOrderHistory(Long orderCode, Principal principal) {
        Users user = userRepo.findByEmail(principal.getName());
        return repo.findByOrderCode(orderCode, user);
    }
}
