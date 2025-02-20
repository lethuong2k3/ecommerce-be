package net.fpoly.ecommerce.service;

import net.fpoly.ecommerce.model.Users;
import net.fpoly.ecommerce.model.WishList;
import net.fpoly.ecommerce.model.request.WishListRequest;

import java.security.Principal;
import java.util.List;

public interface WishListService {
    WishList createWishList(WishListRequest request, Principal principal);
    List<WishList> findAllByUserAndStatus(Principal principal);
    WishList deleteWishList(Long id, Principal principal);
    void deleteAllWishLists(List<WishListRequest> lstWishList, Principal principal);
}
