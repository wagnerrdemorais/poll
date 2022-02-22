package com.wagnerrdemorais.poll.model;

import javax.persistence.*;
import java.util.List;

/**
 * PollOption database entity
 */
@Entity
public class PollOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @ManyToOne(cascade = CascadeType.ALL)
    private Poll poll;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "pollOption")
    private List<Vote> voteList;

    /**
     * No args Constructor
     */
    public PollOption() {
    }

    /**
     * All args Constructor
     *
     * @param id       Long
     * @param title    String
     * @param voteList List<Vote>
     */
    public PollOption(Long id, String title, List<Vote> voteList) {
        this.id = id;
        this.title = title;
        this.voteList = voteList;
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

    public Poll getPoll() {
        return poll;
    }

    public void setPoll(Poll poll) {
        this.poll = poll;
    }

    public List<Vote> getVoteList() {
        return voteList;
    }

}
