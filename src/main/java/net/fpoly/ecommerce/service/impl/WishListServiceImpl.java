package net.fpoly.ecommerce.service.impl;

import lombok.RequiredArgsConstructor;
import net.fpoly.ecommerce.model.Users;
import net.fpoly.ecommerce.model.WishList;
import net.fpoly.ecommerce.model.request.WishListRequest;
import net.fpoly.ecommerce.repository.UserRepo;
import net.fpoly.ecommerce.repository.WishListRepo;
import net.fpoly.ecommerce.service.WishListService;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WishListServiceImpl implements WishListService {

    private final WishListRepo wishListRepo;

    private final UserRepo userRepo;

    @Override
    public WishList createWishList(WishListRequest request, Principal principal) {
        Users user = userRepo.findByEmail(principal.getName());
        WishList isWishList = wishListRepo.findByProductAndUser(request.getProduct(), user);
        if (isWishList != null) {
            isWishList.setStatus(1);
            return wishListRepo.save(isWishList);
        }
        WishList wishList = new WishList();
        wishList.setProduct(request.getProduct());
        wishList.setUser(user);
        wishList.setStatus(1);
        return wishListRepo.save(wishList);
    }

    @Override
    public List<WishList> findAllByUserAndStatus(Principal principal) {
        Users user = userRepo.findByEmail(principal.getName());
        return wishListRepo.findAllByUserAndStatus(user, 1);
    }

    @Override
    public WishList deleteWishList(Long id, Principal principal) {
        Users user = userRepo.findByEmail(principal.getName());
        WishList wishList = wishListRepo.findByIdAndUser(id, user);
        if (wishList != null) {
            wishList.setStatus(0);
            return wishListRepo.save(wishList);
        }
        return null;
    }

    @Override
    public void deleteAllWishLists(List<WishListRequest> requests, Principal principal) {
        requests.forEach(request -> deleteWishList(request.getId(), principal));
    }


}
