package com.wagnerrdemorais.poll.config.bean;

/**
 * Configuration class with properties for Connection Rating
 */
public class RateConfigBean {

    private final Long capacity;
    private final Long tokens;
    private final Long minute;

    /**
     * All args Constructor
     * @param capacity Long
     * @param tokens Long
     * @param minutes Long
     */
    public RateConfigBean(Long capacity, Long tokens, Long minutes) {
        this.capacity = capacity;
        this.tokens = tokens;
        this.minute = minutes;
    }

    public Long getCapacity() {
        return capacity;
    }


    public Long getTokens() {
        return tokens;
    }

    public Long getMinute() {
        return minute;
    }

}
