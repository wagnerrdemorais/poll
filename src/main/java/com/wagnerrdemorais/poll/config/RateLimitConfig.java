package com.wagnerrdemorais.poll.config;

import com.wagnerrdemorais.poll.config.bean.RateConfigBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RateLimitConfig {

    @Value("${authentication.rate.capacity}")
    private Long capacity;

    @Value("${authentication.rate.capacity}")
    private Long tokens;

    @Value("${authentication.rate.duration.min}")
    private Long minutes;

    @Bean
    public RateConfigBean getRateConfig(){
        return new RateConfigBean(capacity, tokens, minutes);
    }

}
