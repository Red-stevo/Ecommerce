package org.codiz.onshop.configurations;

import lombok.RequiredArgsConstructor;
import org.codiz.onshop.AuthFilter.JwtAuthFilter;
import org.codiz.onshop.ControllerAdvice.SecurityExceptions.AccessDeniedExceptionHandler;
import org.codiz.onshop.entities.users.Role;
import org.codiz.onshop.entities.users.Users;
import org.codiz.onshop.repositories.users.UsersRepository;
import org.codiz.onshop.service.impl.users.JWTGenService;
import org.codiz.onshop.service.impl.users.RefreshTokensService;
import org.codiz.onshop.service.impl.users.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@RequiredArgsConstructor
public class SecurityConfiguration {
    private final JwtAuthFilter jwtAuthFilter;
    private final UserDetailsServiceImpl userDetailsService;
    private final UsersRepository usersRepository;
    private final JWTGenService jwtGenService;


    @Qualifier("DelegatedAuthenticationEntryPoint")
    private final AuthenticationEntryPoint authenticationEntryPoint;

    @Qualifier("AccessDeniedExceptionHandler")
    private final AccessDeniedExceptionHandler accessDeniedExceptionHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, RefreshTokensService refreshTokensService) throws Exception {
        return http
                .cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer.configure(http))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/**").permitAll()
                        /*.requestMatchers("/api/v1/admin/**").hasAuthority("ADMIN")
                        .requestMatchers("/api/v1/customer/**").hasAuthority("CUSTOMER")
                        .anyRequest().authenticated()*/)
                .formLogin(Customizer.withDefaults())
                .httpBasic(Customizer.withDefaults())
                .userDetailsService(userDetailsService)
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(authenticationEntryPoint)
                        .accessDeniedHandler(accessDeniedExceptionHandler))
                .oauth2Login(oauth -> oauth
                        .userInfoEndpoint(
                                userInfoEndpointConfig -> userInfoEndpointConfig.oidcUserService(this.iodcUserService())
                        )
                        .successHandler((request, response, authentication) -> {
                            OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
                            String email = oauth2User.getAttribute("email");
                            String username = oauth2User.getAttribute("name"); // Use 'name' as fallback for username

                            Users user = usersRepository.findUsersByUsername(username).orElseGet(() -> {
                                Users newUser = new Users();
                                newUser.setUsername(username);
                                newUser.setUserEmail(email);
                                newUser.setRole(Role.CUSTOMER);
                                return usersRepository.save(newUser);
                            });

                            // Generate tokens
                            String accessToken = jwtGenService.generateAccessToken(user);
                            String refreshToken = jwtGenService.generateRefreshToken(user);

                            // Set tokens in response
                            response.setHeader("Authorization", "Bearer " + accessToken);
                            response.setHeader("Refresh-Token", refreshToken);
                            response.sendRedirect("/home"); // Redirect if needed
                        })
                )

                .build();
    }

    private OAuth2UserService<OidcUserRequest, OidcUser> iodcUserService() {
        return new OidcUserService();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;

    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
