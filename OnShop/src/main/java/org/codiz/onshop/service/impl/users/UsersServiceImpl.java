package org.codiz.onshop.service.impl.users;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.codiz.onshop.ControllerAdvice.custom.UserDoesNotExistException;
import org.codiz.onshop.dtos.requests.*;
import org.codiz.onshop.dtos.response.*;
import org.codiz.onshop.entities.users.Role;
import org.codiz.onshop.entities.users.UserProfiles;
import org.codiz.onshop.entities.users.Users;
import org.codiz.onshop.repositories.users.UserProfilesRepository;
import org.codiz.onshop.repositories.users.UsersRepository;
import org.codiz.onshop.service.CloudinaryService;
import org.codiz.onshop.service.serv.users.UsersService;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class UsersServiceImpl implements UsersService {

    private final UsersRepository usersRepository;
    private final ModelMapper modelMapper;
    private final CloudinaryService cloudinaryService;
    private final UserProfilesRepository userProfilesRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JWTGenService jwtGenService;
    private final CookieUtils cookieUtils;
    private final HttpServletResponse response;


    public EntityResponse registerUser(UserRegistrationRequest request) {

        Users user = new Users();

        if (userExists(request.getUserEmail()) && isPasswordStrong(request.getPassword())) {
            user.setUsername(request.getUsername());
            final String password = passwordEncoder.encode(request.getPassword());
            user.setPassword(password);
            @Email
            final String email = request.getUserEmail();
            user.setUserEmail(email);
            user.setPhoneNumber(request.getPhoneNumber());
            user.setRole(Role.CUSTOMER);

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

        if (userExists(admin_user_email)){
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

    public String updateUser(UserRegistrationRequest request,String userId) {
        Users users = usersRepository.findUsersByUserId(userId);

        users.setUsername(request.getUsername());
        users.setPassword(request.getPassword());
        users.setRole(Role.valueOf(String.valueOf(request.getRole())));
        users.setUserEmail(request.getUserEmail());
        users.setPhoneNumber(request.getPhoneNumber());
        usersRepository.save(users);
        return "user updated";
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




    private  boolean userExists(String email) {
        return !usersRepository.existsByUserEmail(email);
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


    public UserProfileResponse showUserProfile(String userId){
        Users users = usersRepository.findUsersByUsername(userId).get();
        UserProfiles profiles = userProfilesRepository.findByUserId(users);

        UserProfileResponse userProfileResponse = new UserProfileResponse();
        userProfileResponse.setUsername(users.getUsername());
        userProfileResponse.setFullName(profiles.getFullName());
        userProfileResponse.setGender(String.valueOf(profiles.getGender()));
        userProfileResponse.setEmail(users.getUserEmail());
        userProfileResponse.setPhoneNumber(users.getPhoneNumber());
        userProfileResponse.setAddress(profiles.getAddress());
        return userProfileResponse;
    }

    public String updateProfile(UserProfileUpdateRequest request) {
        Users users = usersRepository.findUsersByUserId(request.getUserId());

        UserProfiles profiles = userProfilesRepository.findByUserId(users);
        if (profiles == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Profile not found");
        }


        profiles.setFullName(request.getFirstName());
        profiles.setGender(request.getGender());
        profiles.setAddress(request.getAddress());
        profiles.setSecondaryEmail(request.getSecondaryEmail());

        userProfilesRepository.save(profiles);
        return "profile updated";

    }

    public String updateEmail(String userId,String email) {
        Users users = usersRepository.findUsersByUserId(userId);
        if (users == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        if (Objects.equals(users.getUserEmail(), email)){
            users.setUserEmail(email);
        } else if (Objects.equals(users.getProfile().getSecondaryEmail(), email)) {
            users.setUserEmail(email);
        }

        usersRepository.save(users);
        return "email updated";
    }

    public String updateProfileImage(String userId, FileUploads uploads){
        Users users = usersRepository.findUsersByUserId(userId);
        UserProfiles profiles = users.getProfile();
        profiles.setImageUrl(cloudinaryService.uploadImage(uploads));
        userProfilesRepository.save(profiles);
        return "profile updated";
    }



    @Transactional
    public ResponseEntity<AuthenticationResponse> loginUser(LoginRequests loginRequests) {
        // Authenticate the user
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequests.getUsername(), loginRequests.getPassword()));


        // Fetch user details
        Users user = usersRepository.findUsersByUsername(loginRequests.getUsername()).orElseThrow(()-> {
            return new UserDoesNotExistException("User Does Not Exist.");
        });


        if (user == null) {
            log.error("User not found with email: {}", loginRequests.getUsername());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // Generate access token
        String accessToken = jwtGenService.generateAccessToken(user);
        log.info("Access token generated successfully");


        // Set the access token in a secure cookie
        AuthenticationResponse authResponse = new AuthenticationResponse();
        authResponse.setMessage("Authentication successful.");
        authResponse.setToken(accessToken);
        authResponse.setUserId(user.getUserId());
        authResponse.setUserRole(user.getRole().toString());

        response.setHeader("Set-Cookie", cookieUtils.responseCookie(user).toString());

        // Return an AuthenticationResponse object containing both tokens
        return new ResponseEntity<>(authResponse, HttpStatus.OK);


    }

    private static boolean isPasswordStrong(String password){
        String passwordRegex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$!)(}{%^&+=])(?=\\S+$).{8,}$";
        return password.matches(passwordRegex);
    }


    public UserGeneralResponse resetPassword(ResetPasswordDetails resetPasswordDetails) {
        log.info("Service to reset the password");

        // Find user by email
        Users user = usersRepository.findByUserEmail(resetPasswordDetails.getEmail()).orElseThrow(() -> new UserDoesNotExistException("User Does Not Exist."));

        // Validate the old password by comparing it with the encoded password in the database
        if (!passwordEncoder.matches(resetPasswordDetails.getOldPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid current password");
        }

        // Set the new password
        String newPassword = resetPasswordDetails.getNewPassword();
        if (!isPasswordStrong(resetPasswordDetails.getNewPassword())){
            throw new IllegalArgumentException("Weak password");
        }
        user.setPassword(passwordEncoder.encode(newPassword));

        // Save the updated user with the new password
        usersRepository.save(user);

        UserGeneralResponse userGeneralResponse = new UserGeneralResponse();
        userGeneralResponse.setMessage("Password updated successfully.");
        userGeneralResponse.setDate(new Date());
        userGeneralResponse.setHttpStatus(HttpStatus.OK);

        return userGeneralResponse;
    }
}
