package org.codiz.onshop.service.impl;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.codiz.onshop.dtos.requests.UserRegistrationRequest;
import org.codiz.onshop.dtos.response.EntityCreationResponse;
import org.codiz.onshop.entities.users.Role;
import org.codiz.onshop.entities.users.Users;
import org.codiz.onshop.repositories.users.UsersRepository;
import org.codiz.onshop.service.serv.UsersService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Date;
import java.sql.Timestamp;

@Service
@RequiredArgsConstructor
public class UsersServiceImpl implements UsersService {

    private UsersRepository usersRepository;


    public EntityCreationResponse registerUser(UserRegistrationRequest request) {

        Users user = new Users();

        if (userExists(user.getUserId())){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User already exists");
        }

        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        user.setUserEmail(request.getUserEmail());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setRole(request.getRole());

        usersRepository.save(user);


        EntityCreationResponse response = new EntityCreationResponse();
        response.setMessage("Successfully registered user");
        response.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        response.setStatus(HttpStatus.OK);
        return response;


    }

    @Value("${default-username}")
    private String admin_username;
    @Value("${default-user-password}")
    private String admin_user_password;
    @Value("${default-user-role}")
    private Role admin_user_role;
    @Value("${default-user-email}")
    private String admin_user_email;
    @Value("${default-user-phone}")
    private String admin_user_phone;

    @PostConstruct
    private EntityCreationResponse createAdminUser() {
        Users user = new Users();

        if (userExists(user.getUserId())){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User already exists");
        }
        user.setUsername(admin_username);
        user.setPassword(admin_user_password);
        user.setRole(admin_user_role);
        user.setUserEmail(admin_user_email);
        user.setPhoneNumber(admin_user_phone);
        usersRepository.save(user);

        EntityCreationResponse response = new EntityCreationResponse();
        response.setMessage("Successfully created admin user");
        response.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        response.setStatus(HttpStatus.OK);
        return response;
    }

    private  boolean userExists(String userId) {
        return usersRepository.existsById(userId);
    }

}
