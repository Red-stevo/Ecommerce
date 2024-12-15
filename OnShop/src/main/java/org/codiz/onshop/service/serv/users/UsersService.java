package org.codiz.onshop.service.serv.users;

import jakarta.servlet.http.HttpServletRequest;
import org.codiz.onshop.dtos.requests.FileUploads;
import org.codiz.onshop.dtos.requests.LoginRequests;
import org.codiz.onshop.dtos.requests.UserProfileUpdateRequest;
import org.codiz.onshop.dtos.requests.UserRegistrationRequest;
import org.codiz.onshop.dtos.response.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    UserProfileResponse showUserProfile(String userId);
    HttpStatus updateProfile(UserProfileUpdateRequest request);
    HttpStatus updateEmail(String userId,String email);
    HttpStatus updateProfileImage(String userId, MultipartFile upload) throws IOException;
}
