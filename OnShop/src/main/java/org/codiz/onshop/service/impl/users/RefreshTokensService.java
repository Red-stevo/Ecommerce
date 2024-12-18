package org.codiz.onshop.service.impl.users;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.codiz.onshop.ControllerAdvice.custom.InvalidTokensException;
import org.codiz.onshop.dtos.response.AuthenticationResponse;
import org.codiz.onshop.entities.users.Users;
import org.codiz.onshop.repositories.users.RefreshTokensRepository;
import org.springframework.http.HttpCookie;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class RefreshTokensService {

    private final RefreshTokensRepository refreshTokensRepository;
    private final JWTGenService jwtGenService;
    private final CookieUtils cookieUtils;
    private final HttpServletResponse response;


    public AuthenticationResponse refreshToken(HttpServletRequest request) {

        String refreshToken = cookieUtils.extractJwtFromCookie(request);


        log.warn("Extracted the refresh token.");
       if (refreshToken == null || refreshToken.isEmpty())
           throw new InvalidTokensException("No tokens found in request");


        Users users = refreshTokensRepository.findByRefreshToken(refreshToken).
                orElseThrow(() -> new InvalidTokensException("refresh token not found")).getUser();

        log.warn("token found in database.");
        if (!jwtGenService.isTokenValid(refreshToken,users)){
            throw new InvalidTokensException("Invalid token");
        }

        log.warn("Token checked for validity.");
        HttpCookie refresh = cookieUtils.responseCookie(users);
        response.setHeader("Set-Cookie", refresh.toString());
        String accessToken = jwtGenService.generateAccessToken(users);

        AuthenticationResponse authenticationResponse = new AuthenticationResponse();
        authenticationResponse.setToken(accessToken);
        authenticationResponse.setMessage("access token refreshed successfully");
        authenticationResponse.setUserRole(String.valueOf(users.getRole()));
        authenticationResponse.setUserId(String.valueOf(users.getUserId()));

        log.warn("token fully refreshed.");
        return authenticationResponse;
    }

}
