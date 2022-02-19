package com.wagnerrdemorais.poll.dto;

/**
 * PollOptionDto used to return PollOption information to view.
 */
public class PollOptionDto {

    private Long id;

    private String title;

    private int voteCount;

    /**
     * All Args Constructor
     * @param id Long
     * @param title String
     * @param voteCount int
     */
    public PollOptionDto(Long id, String title, int voteCount) {
        this.id = id;
        this.title = title;
        this.voteCount = voteCount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }
}
