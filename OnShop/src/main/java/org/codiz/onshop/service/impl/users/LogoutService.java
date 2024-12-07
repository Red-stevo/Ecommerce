package org.codiz.onshop.service.impl.users;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.codiz.onshop.ControllerAdvice.custom.InvalidTokensException;
import org.codiz.onshop.dtos.response.UserGeneralResponse;
import org.codiz.onshop.entities.users.RefreshTokens;
import org.codiz.onshop.entities.users.Users;
import org.codiz.onshop.repositories.users.RefreshTokensRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;


import java.util.Date;

@Service
@RequiredArgsConstructor
@Slf4j
public class LogoutService {
    private final RefreshTokensRepository refreshTokensRepository;
    private final CookieUtils cookieUtils;
    private final HttpServletResponse response;
    private final JWTGenService jwtGenService;



    public UserGeneralResponse logOut(HttpServletRequest request) {

        log.info("service to log out");

        String refreshTokens = cookieUtils.extractJwtFromCookie(request);
        if (refreshTokens == null || refreshTokens.isEmpty()) {
            throw new InvalidTokensException("no tokens found in request, cannot log you out");
        }

        Users users = refreshTokensRepository.findByRefreshToken(refreshTokens).
                orElseThrow(() -> new InvalidTokensException("refresh token not found")).getUser();

        if (!jwtGenService.isTokenValid(refreshTokens,users)){
            throw new InvalidTokensException("Invalid token");
        }

        RefreshTokens tokens = refreshTokensRepository.findByRefreshToken(refreshTokens).orElseThrow(
                () -> new InvalidTokensException("refresh token not found")
        );
        refreshTokensRepository.delete(tokens);

        Cookie logOutCookie = new Cookie("x-log-out", null);
        logOutCookie.setMaxAge(0);
        logOutCookie.setPath("/");
        logOutCookie.setHttpOnly(true);
        response.addCookie(logOutCookie);

        log.info("logged out successfully");
        UserGeneralResponse genResponse = new UserGeneralResponse();
        genResponse.setMessage("logged out successfully");
        genResponse.setDate(new Date());
        genResponse.setHttpStatus(HttpStatus.OK);
        return genResponse;

    }


}
