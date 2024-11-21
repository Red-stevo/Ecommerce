package org.codiz.onshop.service.impl.users;

import jakarta.annotation.PostConstruct;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import org.codiz.onshop.dtos.requests.UserRegistrationRequest;
import org.codiz.onshop.dtos.response.EntityResponse;
import org.codiz.onshop.dtos.response.EntityDeletionResponse;
import org.codiz.onshop.dtos.response.UserResponse;
import org.codiz.onshop.entities.users.Role;
import org.codiz.onshop.entities.users.Users;
import org.codiz.onshop.repositories.users.UsersRepository;
import org.codiz.onshop.service.serv.users.UsersService;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Timestamp;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UsersServiceImpl implements UsersService {

    private final UsersRepository usersRepository;
    private final ModelMapper modelMapper;


    public EntityResponse registerUser(UserRegistrationRequest request) {

        Users user = new Users();

        if (!userExists(request.getUsername(),request.getUserEmail())){
            user.setUsername(request.getUsername());
            final String password = request.getPassword();
            user.setPassword(password);
            @Email
            final String email = request.getUserEmail();
            user.setUserEmail(email);
            user.setPhoneNumber(request.getPhoneNumber());
            user.setRole(Role.valueOf(String.valueOf(request.getRole())));

            usersRepository.save(user);

            EntityResponse response = new EntityResponse();
            response.setMessage("Successfully created admin user");
            response.setCreatedAt(new Timestamp(System.currentTimeMillis()));
            response.setStatus(HttpStatus.OK);

            return response;
        }
        else {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User already exists");
        }


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
    private EntityResponse createAdminUser() {
        Users user = new Users();

        if (!userExists(admin_username,admin_user_email)){
            user.setUsername(admin_username);
            user.setPassword(admin_user_password);
            user.setRole(admin_user_role);
            user.setUserEmail(admin_user_email);
            user.setPhoneNumber(admin_user_phone);
            usersRepository.save(user);

            EntityResponse response = new EntityResponse();
            response.setMessage("Successfully created admin user");
            response.setCreatedAt(new Timestamp(System.currentTimeMillis()));
            response.setStatus(HttpStatus.OK);

            return response;
        }
         return null;
    }


    public EntityDeletionResponse deleteUsers(String username) {
        try {
            Users users = usersRepository.findUsersByUsername(username).get();
            usersRepository.delete(users);
            return deletionResponse();
        }catch (Exception e){
            return deletionFailedResponse();
        }

    }





    public UserRegistrationRequest findUserByUsername(String username) {
        Users users = usersRepository.findUsersByUsername(username).get();

        return modelMapper.map(users,UserRegistrationRequest.class);
    }




    public List<UserResponse> retrieveAllUsers() {
        List<Users> users = usersRepository.findAll();
        modelMapper.getConfiguration().setFieldAccessLevel(Configuration.AccessLevel.PRIVATE)
                .setFieldMatchingEnabled(true);
        return users.stream()
                .map(users1 -> modelMapper.map(users1,UserResponse.class)).toList();
    }




    private  boolean userExists(String username,String email) {
        return usersRepository.existsByUsernameOrUserEmail(username,email);
    }






    private EntityDeletionResponse deletionResponse(){
        EntityDeletionResponse response = new EntityDeletionResponse();
        response.setMessage("Successfully deleted user");
        response.setSuccess(true);
        response.setTimestamp(new Timestamp(System.currentTimeMillis()));
        response.setStatus(HttpStatus.OK);
        return response;
    }
    private EntityDeletionResponse deletionFailedResponse(){
        EntityDeletionResponse response = new EntityDeletionResponse();
        response.setMessage("could not successfully delete user");
        response.setSuccess(false);
        response.setTimestamp(new Timestamp(System.currentTimeMillis()));
        response.setStatus(HttpStatus.BAD_REQUEST);
        return response;
    }


}
