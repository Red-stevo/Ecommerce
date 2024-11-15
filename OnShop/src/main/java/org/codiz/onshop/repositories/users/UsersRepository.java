package org.codiz.onshop.repositories.users;

import org.codiz.onshop.entities.users.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsersRepository extends JpaRepository<Users, String> {

    boolean existsByUsernameOrUserEmail(String username,String email);
    Optional<Users> findUsersByUsername(String username);

    Users findUsersByUserId(String userId);
}
