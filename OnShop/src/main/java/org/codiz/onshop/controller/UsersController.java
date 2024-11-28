package org.codiz.onshop.controller;

import lombok.RequiredArgsConstructor;
import org.codiz.onshop.dtos.requests.UserRegistrationRequest;
import org.codiz.onshop.dtos.response.EntityResponse;
import org.codiz.onshop.dtos.response.UserResponse;
import org.codiz.onshop.service.serv.users.UsersService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@CrossOrigin( origins = "http://127.0.0.1:5173/", allowCredentials = "true")
public class UsersController {

    private final UsersService usersService;



    @PostMapping("/register")
    public ResponseEntity<EntityResponse> registerUser(@RequestBody UserRegistrationRequest req){
        EntityResponse res = usersService.registerUser(req);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/all-users")
    public ResponseEntity<List<UserResponse>> getAllUsers(){
        return ResponseEntity.ok(usersService.retrieveAllUsers());
    }
}
