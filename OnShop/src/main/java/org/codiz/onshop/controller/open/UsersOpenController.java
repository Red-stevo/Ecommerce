package org.codiz.onshop.controller.open;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.codiz.onshop.dtos.requests.LoginRequests;
import org.codiz.onshop.dtos.requests.UserRegistrationRequest;
import org.codiz.onshop.dtos.response.AuthenticationResponse;
import org.codiz.onshop.dtos.response.EntityResponse;
import org.codiz.onshop.service.serv.users.UsersService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/open")
public class UsersOpenController {

    private final UsersService usersService;

    @GetMapping("login")
    public ResponseEntity<ResponseEntity<AuthenticationResponse>> loginUser(@RequestBody LoginRequests loginRequests){
        return ResponseEntity.ok(usersService.loginUser(loginRequests));
    }

    @GetMapping("/refresh")
    public ResponseEntity<AuthenticationResponse> refreshTokens(HttpServletRequest request) {
        // Refresh the token
        AuthenticationResponse authenticationResponse = usersService.refreshToken(request);

        // Return the new access token with a 200 OK status
        return ResponseEntity.ok(authenticationResponse);

    }

    @PostMapping("/register")
    public ResponseEntity<EntityResponse> registerUser(@RequestBody UserRegistrationRequest req){
        EntityResponse res = usersService.registerUser(req);
        return ResponseEntity.ok(res);
    }
}
