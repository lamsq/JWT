package com.example.jwt.service;

import com.example.jwt.model.User;
import com.example.jwt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailService implements UserDetailsService {
    private final UserRepository ur;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User u = ur.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User doesnt exist"));

        return org.springframework.security.core.userdetails.User
                .withUsername(u.getUsername()).password(u.getPassword())
                .roles(u.getRole().name()).accountLocked(!u.isAccountNonLocked()).build();
    }
}
