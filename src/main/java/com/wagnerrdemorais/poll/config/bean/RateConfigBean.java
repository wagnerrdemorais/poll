package com.wagnerrdemorais.poll.config.bean;

public class RateConfigBean {

    private Long capacity;
    private Long tokens;
    private Long minute;

    public RateConfigBean(Long capacity, Long tokens, Long minutes) {
        this.capacity = capacity;
        this.tokens = tokens;
        this.minute = minutes;
    }

    public Long getCapacity() {
        return capacity;
    }

    public void setCapacity(Long capacity) {
        this.capacity = capacity;
    }

    public Long getTokens() {
        return tokens;
    }

    public void setTokens(Long tokens) {
        this.tokens = tokens;
    }

    public Long getMinute() {
        return minute;
    }

    public void setMinute(Long minute) {
        this.minute = minute;
    }
}
