package com.mydukaan.service;

import com.mydukaan.exception.InvalidCredentialsException;
import com.mydukaan.model.User;
import com.mydukaan.repository.UserRepository;
import com.mydukaan.security.CustomUserDetails;
import org.jspecify.annotations.NullMarked;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @NullMarked
    public UserDetails loadUserByUsername(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(()-> new UsernameNotFoundException("User Not found"));

        return new CustomUserDetails(user);
    }
}