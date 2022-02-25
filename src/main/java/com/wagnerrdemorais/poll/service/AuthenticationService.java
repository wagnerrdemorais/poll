package com.wagnerrdemorais.poll.service;

import com.wagnerrdemorais.poll.model.User;
import com.wagnerrdemorais.poll.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service for authentication operations
 */
@Service
public class AuthenticationService implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * All args constructor
     * @param userRepository UserRepository
     */
    public AuthenticationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Find user by username and return its details
     * @param username String
     * @return UserDetails
     * @throws UsernameNotFoundException UNFE
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository.findByUsername(username);
        return userOptional.orElseThrow(() -> new UsernameNotFoundException("User not found!"));
    }
}