package net.fpoly.ecommerce.repository;

import net.fpoly.ecommerce.model.Product;
import net.fpoly.ecommerce.model.Users;
import net.fpoly.ecommerce.model.WishList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WishListRepo extends JpaRepository<WishList, Long> {
    List<WishList> findAllByUserAndStatus(Users user, Integer status);
    WishList findByProductAndUser(Product product, Users user);
    WishList findByIdAndUser(Long id, Users user);
}
