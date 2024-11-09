package org.codiz.onshop.controller;

import lombok.RequiredArgsConstructor;
import org.codiz.onshop.dtos.requests.UserRegistrationRequest;
import org.codiz.onshop.dtos.response.EntityCreationResponse;
import org.codiz.onshop.service.serv.users.UsersService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class UsersController {

    private final UsersService usersService;

    @PostMapping("/register")
    public ResponseEntity<EntityCreationResponse> registerUser(@RequestBody UserRegistrationRequest req){
        EntityCreationResponse res = usersService.registerUser(req);
        return ResponseEntity.ok(res);
    }
}
