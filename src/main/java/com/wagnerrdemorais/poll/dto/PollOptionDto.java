package com.wagnerrdemorais.poll.dto;

import java.util.List;

/**
 * PollOptionDto used to return PollOption information to view.
 */
public class PollOptionDto {

    private Long id;

    private String title;

    private List<VoteDto> voteList;

    /**
     * All Args Constructor
     * @param id Long
     * @param title String
     * @param voteList List<VoteDto>
     */
    public PollOptionDto(Long id, String title, List<VoteDto> voteList) {
        this.id = id;
        this.title = title;
        this.voteList = voteList;
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

    public List<VoteDto> getVoteList() {
        return voteList;
    }

    public void setVoteList(List<VoteDto> voteList) {
        this.voteList = voteList;
    }
}
