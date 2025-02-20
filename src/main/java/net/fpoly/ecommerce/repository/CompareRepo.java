package net.fpoly.ecommerce.repository;

import net.fpoly.ecommerce.model.Compare;
import net.fpoly.ecommerce.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompareRepo extends JpaRepository<Compare, Long> {
    List<Compare> findByUser(Users user);
    Compare findByIdAndUser(Long id, Users user);
}
