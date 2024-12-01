package org.codiz.onshop.repositories.users;

import org.codiz.onshop.entities.users.UserProfiles;
import org.codiz.onshop.entities.users.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserProfilesRepository extends JpaRepository<UserProfiles, String> {

    UserProfiles findByUserId(Users userId);
}
