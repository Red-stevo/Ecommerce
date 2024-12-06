package org.codiz.onshop.service.impl.users;

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
    public UserDetails loadUserByUsername(String username){
        return repository.findUsersByUsername(username).orElseThrow(() -> new RuntimeException("User Does Not Exist."));
    }
}
