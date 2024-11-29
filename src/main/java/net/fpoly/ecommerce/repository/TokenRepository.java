package net.fpoly.ecommerce.repository;

import net.fpoly.ecommerce.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {
    @Query("""
        SELECT t FROM Token t inner join Users u
        on t.user.id = u.id
        where t.user.id = :userId and t.loggedOut = false
    """)
    List<Token> findAllAccessTokenByUser(Long userId);

    Optional<Token> findByAccessToken(String token);

    Optional<Token> findByRefreshToken(String refreshToken);
}
