package org.codiz.onshop.service.serv.users;

import org.codiz.onshop.dtos.requests.UserRegistrationRequest;
import org.codiz.onshop.dtos.response.EntityResponse;
import org.codiz.onshop.dtos.response.EntityDeletionResponse;
import org.codiz.onshop.dtos.response.UserResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UsersService {

    EntityResponse registerUser(UserRegistrationRequest request);
    EntityDeletionResponse deleteUsers(String username);
    UserRegistrationRequest findUserByUsername(String username);
    List<UserResponse> retrieveAllUsers();
}
