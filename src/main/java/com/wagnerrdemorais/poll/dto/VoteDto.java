package com.wagnerrdemorais.poll.dto;

/**
 * Dto for Vote entity
 */
public class VoteDto {

    Long id;
    String opinion;
    Long pollOptionId;

    /**
     * All args Constructor
     * @param id Long
     * @param opinion String
     * @param pollOptionId Long
     */
    public VoteDto(Long id, String opinion, Long pollOptionId) {
        this.id = id;
        this.opinion = opinion;
        this.pollOptionId = pollOptionId;
    }

    public Long getId() {
        return id;
    }

    public String getOpinion() {
        return opinion;
    }

    public Long getPollOptionId() {
        return pollOptionId;
    }
}
