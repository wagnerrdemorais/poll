package com.wagnerrdemorais.poll.model;

import javax.persistence.*;

@Entity
public class Vote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String opinion;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private PollOption pollOption;

    public Vote() {
    }

    public Vote(PollOption pollOption, String opinion) {
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
