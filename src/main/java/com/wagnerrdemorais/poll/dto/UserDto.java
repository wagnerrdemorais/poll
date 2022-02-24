package com.wagnerrdemorais.poll.dto;

/**
 * Dto with userID and Username
 */
public class UserDto {

    Long id;
    String username;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
