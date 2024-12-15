package org.codiz.onshop.controller.customers;

import lombok.RequiredArgsConstructor;
import org.codiz.onshop.dtos.requests.EmailUpdateModel;
import org.codiz.onshop.dtos.requests.FileUploads;
import org.codiz.onshop.dtos.requests.UserProfileUpdateRequest;
import org.codiz.onshop.dtos.response.UserProfileResponse;
import org.codiz.onshop.service.serv.users.UsersService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/customer/profile")
public class CustomerUserController {

    private final UsersService usersService;

    @GetMapping("/{userId}")
    public ResponseEntity<UserProfileResponse> showUserProfile(@PathVariable String userId){
        return ResponseEntity.ok(usersService.showUserProfile(userId));
    }

    @PutMapping("/update")
    public ResponseEntity<HttpStatus> updateProfile(@RequestBody UserProfileUpdateRequest request){
        return ResponseEntity.ok(usersService.updateProfile(request));
    }

    @PutMapping("/update/email/{userId}")
    public ResponseEntity<HttpStatus> updateEmail(@PathVariable String userId, @RequestBody EmailUpdateModel email){
        return ResponseEntity.ok(usersService.updateEmail(userId, email.getEmail()));
    }
    @PutMapping("/image/update/{userId}")
    public ResponseEntity<HttpStatus>  updateProfileImage(@PathVariable String userId, @RequestBody FileUploads uploads){
        return ResponseEntity.ok(usersService.updateProfileImage(userId,uploads));
    }
}
