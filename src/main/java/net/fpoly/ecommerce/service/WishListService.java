package net.fpoly.ecommerce.service;

import net.fpoly.ecommerce.model.Product;
import net.fpoly.ecommerce.model.Users;
import net.fpoly.ecommerce.model.WishList;
import net.fpoly.ecommerce.model.request.WishListRequest;

import java.security.Principal;
import java.util.List;

public interface WishListService {
    List<WishList> findAllByUserAndStatus(Principal principal);
    WishList findByIdAndUser(Long id, Principal principal);
    WishList findByProductAndUser(Product product, Principal principal);
    WishList createWishList(WishListRequest request, Principal principal);
    WishList deleteWishList(Long id, Principal principal);
    void deleteAllWishLists(List<WishListRequest> lstWishList, Principal principal);
}
