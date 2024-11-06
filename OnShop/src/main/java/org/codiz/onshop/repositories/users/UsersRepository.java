package org.codiz.onshop.repositories.users;

import org.codiz.onshop.entities.users.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<Users, String> {

}
