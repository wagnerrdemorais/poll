package com.wagnerrdemorais.poll.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * PollOption database entity
 */
@Entity
public class PollOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private int voteCount;

    /**
     * No args Constructor
     */
    public PollOption() {
    }

    /**
     * All args Constructor
     * @param id Long
     * @param title String
     * @param voteCount int
     */
    public PollOption(Long id, String title, int voteCount) {
        this.id = id;
        this.title = title;
        this.voteCount = voteCount;
    }

    public Long getId() {
        return id;
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
