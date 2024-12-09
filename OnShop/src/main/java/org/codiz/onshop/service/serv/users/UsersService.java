package org.codiz.onshop.service.serv.users;

import jakarta.servlet.http.HttpServletRequest;
import org.codiz.onshop.dtos.requests.LoginRequests;
import org.codiz.onshop.dtos.requests.UserRegistrationRequest;
import org.codiz.onshop.dtos.response.AuthenticationResponse;
import org.codiz.onshop.dtos.response.EntityResponse;
import org.codiz.onshop.dtos.response.EntityDeletionResponse;
import org.codiz.onshop.dtos.response.UserResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UsersService {

    EntityResponse registerUser(UserRegistrationRequest request);
    EntityDeletionResponse deleteUsers(String username);
    UserRegistrationRequest findUserByUsername(String username);
    List<UserResponse> retrieveAllUsers();
    ResponseEntity<AuthenticationResponse> loginUser(LoginRequests loginRequests);
    AuthenticationResponse refreshToken(HttpServletRequest request);
    EntityResponse registerAdminUser(UserRegistrationRequest request);
}
