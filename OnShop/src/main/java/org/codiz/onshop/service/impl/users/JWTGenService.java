package org.codiz.onshop.service.impl.users;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.codiz.onshop.entities.users.Users;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

/**
 *  method to handle the jwt token generation, extraction of claims present in the tokens and
 *  validating the authenticity of these claims
 * */
@Service
public class JWTGenService {

    @Value("${secret_key}")
    private String SECRET_KEY;
    @Value("${expiration_time}")
    private long expirationTime;
    @Value("${refresh_token}")
    private long REFRESH_TOKEN_EXPIRATION;

    /**
     method to validate if the username extracted from the token is valid username
     */
    public Boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    /**
     * method to check if the provided token is not expired yet
     * */
    private boolean isTokenExpired(String token) {
        return decryptJWT(token,Claims::getExpiration).before(new Date(System.currentTimeMillis()));
    }

    /**
     * method to extract the username from the provided tokens
     */
    public String extractUsername(String token) {
        return decryptJWT(token,Claims::getSubject);
    }

    /**
     *  method to generate the jwt tokens
     * */
    protected  String tokenGenerator(Users users, long duration) {
        return Jwts
                .builder()
                .subject(users.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + duration))
                .signWith(getSignInKey())
                .compact();
    }
    public String generateAccessToken(Users users) {
        return tokenGenerator(users, expirationTime);
    }
    public String generateRefreshToken(Users users) {
        return tokenGenerator(users, REFRESH_TOKEN_EXPIRATION);
    }
    /**
     *  method to extract a specific claim from the provided token e.g username, expiration time ...
     * */
    public <T> T decryptJWT(String token, Function<Claims, T> claimsResolver) {
        Claims claims = extractJWT(token);
        return claimsResolver.apply(claims);
    }

    /**
     *  method to extract a list of all the claims passed in the token
     * */
    public Claims extractJWT(String token) {
        return Jwts
                .parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64URL.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
