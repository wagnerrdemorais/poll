package com.wagnerrdemorais.poll.config;

import com.wagnerrdemorais.poll.model.User;
import com.wagnerrdemorais.poll.repository.UserRepository;
import com.wagnerrdemorais.poll.service.TokenService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

/**
 * Filter for authentication and validating token
 */
public class AuthenticationTokenFilter extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final UserRepository userRepository;

    public AuthenticationTokenFilter(TokenService tokenService, UserRepository userRepository) {
        this.tokenService = tokenService;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain)
            throws ServletException, IOException {
        String token = getToken(req);

        if (tokenService.isValid(token)) {
            authenticate(token);
        }

        filterChain.doFilter(req, res);
    }

    /**
     * Validates given JWT Token and proceeds with authentication
     * @param token String
     */
    private void authenticate(String token) {
        Long userId = tokenService.getUserId(token);
        Optional<User> userOpt = userRepository.findById(userId);

        if (userOpt.isEmpty()) {
            return;
        }

        User user = userOpt.get();
        UsernamePasswordAuthenticationToken auth
                = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    /**
     * Retrieves Bearer token from request
     * @param request HttpServletRequest
     * @return String
     */
    private String getToken(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            return null;
        }
        return authorization.substring(7);
    }
}
