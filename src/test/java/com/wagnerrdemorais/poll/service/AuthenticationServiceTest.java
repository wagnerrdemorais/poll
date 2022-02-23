package com.wagnerrdemorais.poll.service;

import com.wagnerrdemorais.poll.model.User;
import com.wagnerrdemorais.poll.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetails;

class AuthenticationServiceTest {

    AuthenticationService subject;
    UserRepository userRepository;

    @BeforeEach
    void setup() {
        userRepository = Mockito.mock(UserRepository.class);
        Mockito.when(userRepository.findByUsername(Mockito.anyString()))
                .thenReturn(java.util.Optional.of(new User(1L, "test", "test")));

        subject = new AuthenticationService(userRepository);
    }

    @Test
    void loadUserByUsername() {
        UserDetails test = subject.loadUserByUsername("test");
        Assertions.assertEquals("test", test.getUsername());
    }
}