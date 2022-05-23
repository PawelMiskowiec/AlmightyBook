package com.example.almightybook.security;

import com.example.almightybook.users.db.UserEntityRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@AllArgsConstructor
public class AlmightybookUserDetailsService implements UserDetailsService {

    private final UserEntityRepository repository;
    private final AdminConfig config;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if(config.getUsername().equalsIgnoreCase(username)){
            return config.adminUser();
        }
        return repository.findByUsernameIgnoreCase(username)
                        .map(UserEntityDetails::new)
                        .orElseThrow(() -> new UsernameNotFoundException(username));
    }



}
