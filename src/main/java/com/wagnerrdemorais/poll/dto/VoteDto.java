package com.wagnerrdemorais.poll.dto;

public class VoteDto {

    Long id;
    String opinion;
    Long pollOptionId;

    public VoteDto() {
    }

    public VoteDto(Long id, String opinion, Long pollOptionId) {
        this.id = id;
        this.opinion = opinion;
        this.pollOptionId = pollOptionId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOpinion() {
        return opinion;
    }

    public void setOpinion(String opinion) {
        this.opinion = opinion;
    }

    public Long getPollOptionId() {
        return pollOptionId;
    }

    public void setPollOptionId(Long pollOptionId) {
        this.pollOptionId = pollOptionId;
    }
}
