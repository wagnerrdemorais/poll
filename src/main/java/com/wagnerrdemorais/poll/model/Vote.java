package com.wagnerrdemorais.poll.model;

import javax.persistence.*;

/**
 * Vote database entity
 */
@Entity
public class Vote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String opinion;

    /**
     * One Option can have many votes
     */
    @ManyToOne(cascade = CascadeType.ALL)
    private PollOption pollOption;

    /**
     * No args constructor
     */
    public Vote() {
    }

    /**
     * All args constructor
     *
     * @param pollOption PollOption
     * @param opinion    String
     */
    public Vote(Long id, PollOption pollOption, String opinion) {
        this.id = id;
        this.pollOption = pollOption;
        this.opinion = opinion;
    }

    public Long getId() {
        return id;
    }

    public String getOpinion() {
        return opinion;
    }

    public void setOpinion(String opinion) {
        this.opinion = opinion;
    }

    public PollOption getPollOption() {
        return pollOption;
    }

    public void setPollOption(PollOption pollOption) {
        this.pollOption = pollOption;
    }
}
