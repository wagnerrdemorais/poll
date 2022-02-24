package com.wagnerrdemorais.poll.config;

import com.wagnerrdemorais.poll.repository.UserRepository;
import com.wagnerrdemorais.poll.service.AuthenticationService;
import com.wagnerrdemorais.poll.service.TokenService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Security configuration class
 */
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final AuthenticationService authenticationService;
    private final TokenService tokenService;
    private final UserRepository userRepository;

    public SecurityConfiguration(AuthenticationService authService,
                                 TokenService tokenService,
                                 UserRepository userRepository) {
        this.authenticationService = authService;
        this.tokenService = tokenService;
        this.userRepository = userRepository;
    }

    @Override
    @Bean
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(authenticationService).passwordEncoder(new BCryptPasswordEncoder());
    }

    /**
     * HttpSecurity configurations, with filter for jwt token
     * @param httpSecurity HttpSecurity
     * @throws Exception E
     */
    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .authorizeRequests()
                .antMatchers("/claimPolls").authenticated()
                .antMatchers("/myPolls").authenticated()
                .antMatchers("/**").permitAll()
                .and().sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().csrf().disable()
                .addFilterBefore(new AuthenticationTokenFilter(tokenService, userRepository),
                        UsernamePasswordAuthenticationFilter.class);
    }

    /**
     * enables resources for api-docs, html files and swagger-resources
     * @param web WebSecurity
     */
    @Override
    public void configure(WebSecurity web) {
        web.ignoring()
                .antMatchers("/**.html",
                        "/v2/api-docs",
                        "/webjars/**",
                        "/configuration/**",
                        "/swagger-resources/**");
    }
}