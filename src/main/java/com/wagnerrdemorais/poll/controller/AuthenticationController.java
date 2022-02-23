package com.wagnerrdemorais.poll.controller;

import com.wagnerrdemorais.poll.controller.form.LoginForm;
import com.wagnerrdemorais.poll.service.TokenService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/login")
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    public AuthenticationController(AuthenticationManager authenticationManager, TokenService tokenService) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    @PostMapping
    public ResponseEntity<String> authenticate(@RequestBody LoginForm loginForm) {
        UsernamePasswordAuthenticationToken loginData = new UsernamePasswordAuthenticationToken(loginForm.getUsername(), loginForm.getPassword());
        String token = "";
        try {
            Authentication authentication = authenticationManager.authenticate(loginData);
            token = tokenService.generateToken(authentication);
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(token);
    }

}
