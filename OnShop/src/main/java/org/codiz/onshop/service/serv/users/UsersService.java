package org.codiz.onshop.service.serv.users;

import org.codiz.onshop.dtos.requests.UserRegistrationRequest;
import org.codiz.onshop.dtos.response.EntityCreationResponse;
import org.codiz.onshop.dtos.response.EntityDeletionResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UsersService {

    EntityCreationResponse registerUser(UserRegistrationRequest request);
    EntityDeletionResponse deleteUsers(String username);
    UserRegistrationRequest findUserByUsername(String username);
    List<UserRegistrationRequest> retrieveAllUsers();
}
