package com.wagnerrdemorais.poll.service;

import com.wagnerrdemorais.poll.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.util.ReflectionTestUtils;

class TokenServiceTest {

    TokenService subject;

    @BeforeEach
    void setup() {
        subject = new TokenService();
        ReflectionTestUtils.setField(subject, "expiration", "300000");
        ReflectionTestUtils.setField(subject, "secret", "test");

    }

    @Test
    void generateToken() {
        User user = new User(1L, "test", "test");

        UsernamePasswordAuthenticationToken loginData = Mockito.mock(UsernamePasswordAuthenticationToken.class);
        Mockito.when(loginData.getPrincipal()).thenReturn(user);

        String s = subject.generateToken(loginData);
        Assertions.assertNotNull(s);
    }

    @Test
    void isValid() {
        User user = new User(1L, "test", "test");

        UsernamePasswordAuthenticationToken loginData = Mockito.mock(UsernamePasswordAuthenticationToken.class);
        Mockito.when(loginData.getPrincipal()).thenReturn(user);

        String s = subject.generateToken(loginData);
        Assertions.assertNotNull(s);

        boolean valid = subject.isValid(s);
        Assertions.assertTrue(valid);
    }

    @Test
    void getUserId() {
        User user = new User(1L, "test", "test");

        UsernamePasswordAuthenticationToken loginData = Mockito.mock(UsernamePasswordAuthenticationToken.class);
        Mockito.when(loginData.getPrincipal()).thenReturn(user);

        String s = subject.generateToken(loginData);
        Assertions.assertNotNull(s);
        Long userId = subject.getUserId(s);
        Assertions.assertEquals(1L, userId);
    }
}