package org.codiz.onshop.repositories.users;

import org.codiz.onshop.entities.users.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsersRepository extends JpaRepository<Users, String> {

    boolean existsByUserEmail(String email);
    Optional<Users> findUsersByUsername(String username);

    Users findUsersByUserId(String userId);

    Optional<Users> findByUserEmail(String email);

    boolean existsByUserId(String userId);

    Optional<Users> findUsersByUserEmail(String email);
}
