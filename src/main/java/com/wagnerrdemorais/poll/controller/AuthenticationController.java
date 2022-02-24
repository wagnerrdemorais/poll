package com.wagnerrdemorais.poll.controller;

import com.wagnerrdemorais.poll.config.bean.RateConfigBean;
import com.wagnerrdemorais.poll.controller.form.LoginForm;
import com.wagnerrdemorais.poll.service.TokenService;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;
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

import java.time.Duration;

@Controller
@RequestMapping("/login")
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final Bucket bucket;

    /**
     * All args constructor
     * @param authenticationManager AuthenticationManager
     * @param tokenService TokenService
     * @param rateConfigBean RateConfigBean
     */
    public AuthenticationController(AuthenticationManager authenticationManager, TokenService tokenService,
                                    RateConfigBean rateConfigBean) {

        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;

        Bandwidth limit = Bandwidth.classic(rateConfigBean.getCapacity(),
                Refill.greedy(rateConfigBean.getTokens(),
                        Duration.ofMinutes(rateConfigBean.getMinute())));

        this.bucket = Bucket4j.builder()
                .addLimit(limit)
                .build();
    }

    /**
     * Login endpoint, with Connection rating using Bucket4J
     * @param loginForm LoginForm
     * @return ResponseEntity<String>
     */
    @PostMapping
    public ResponseEntity<String> authenticate(@RequestBody LoginForm loginForm) {
        if (bucket.tryConsume(1)) {
            UsernamePasswordAuthenticationToken loginData
                    = new UsernamePasswordAuthenticationToken(loginForm.getUsername(), loginForm.getPassword());

            String token;
            try {
                Authentication authentication = authenticationManager.authenticate(loginData);
                token = tokenService.generateToken(authentication);
            } catch (AuthenticationException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            return ResponseEntity.status(HttpStatus.OK).body(token);
        }

        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
    }
}
