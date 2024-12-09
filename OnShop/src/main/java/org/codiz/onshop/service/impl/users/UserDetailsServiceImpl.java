package org.codiz.onshop.service.impl.users;

import org.codiz.onshop.ControllerAdvice.custom.UserDoesNotExistException;
import org.codiz.onshop.repositories.users.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UsersRepository repository;
    @Autowired
    public UserDetailsServiceImpl(UsersRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String email){
        return repository.findByUserEmail(email).orElseThrow(() -> new UserDoesNotExistException("User Does Not Exist."));
    }
}
