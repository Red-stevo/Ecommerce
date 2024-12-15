package org.codiz.onshop.controller.admin;

import lombok.RequiredArgsConstructor;
import org.codiz.onshop.dtos.requests.UserRegistrationRequest;
import org.codiz.onshop.dtos.response.EntityResponse;
import org.codiz.onshop.dtos.response.UserResponse;
import org.codiz.onshop.service.serv.users.UsersService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/auth")
@RequiredArgsConstructor
public class UsersAdminController {

    private final UsersService usersService;


    @PostMapping("create-admin")
    public ResponseEntity<EntityResponse> registerAdminUser(UserRegistrationRequest request){
        return ResponseEntity.ok(usersService.registerAdminUser(request));
    }


    @GetMapping("/admin/all-users")
    public ResponseEntity<List<UserResponse>> getAllUsers(){
        return ResponseEntity.ok(usersService.retrieveAllUsers());
    }

}
