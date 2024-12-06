package org.codiz.onshop.repositories.users;

import org.codiz.onshop.entities.users.RefreshTokens;
import org.codiz.onshop.entities.users.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
;

import java.util.Optional;

@Repository
public interface RefreshTokensRepository extends JpaRepository<RefreshTokens, Integer> {
    Boolean existsRefreshTokensByRefreshToken(String refreshToken);

    Optional<RefreshTokens> findByRefreshToken(String refreshToken);

    RefreshTokens findByUser(Users user);

    boolean existsRefreshTokensByUser(Users user);

}
