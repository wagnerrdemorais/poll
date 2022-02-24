package com.wagnerrdemorais.poll.dto;

import com.wagnerrdemorais.poll.model.Poll;
import com.wagnerrdemorais.poll.model.Vote;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Simple PollDto used to return Poll information to view.
 */
public class PollDto {

    Long id;
    String title;
    String description;
    List<PollOptionDto> optionList;

    /**
     * All args Constructor
     * @param id Long
     * @param title String
     * @param description String
     * @param optionList List<PollOptionDto>
     */
    public PollDto(Long id, String title, String description, List<PollOptionDto> optionList) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.optionList = optionList;
    }

    /**
     * No args constructor
     */
    public PollDto() {
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

    public String getDescription() {
        return description;
    }

    public List<PollOptionDto> getOptionList() {
        return optionList;
    }

    /**
     * Receives a Poll and converts it to PollDto
     * @param poll Poll
     * @return PollDto
     */
    public static PollDto fromEntity(Poll poll) {

        List<PollOptionDto> pollOptionDtos = poll.getOptionList().stream()
                .map(opt -> new PollOptionDto(opt.getId(), opt.getTitle(), convertVoteToDto(opt.getVoteList())))
                .collect(Collectors.toList());

        return new PollDto(poll.getId(), poll.getTitle(), poll.getDescription(), pollOptionDtos);
    }

    /**
     * Given a list of votes generates a list of voteDto
     * @param voteList List<Vote>
     * @return List<VoteDto>
     */
    private static List<VoteDto> convertVoteToDto(List<Vote> voteList) {
        return voteList.stream()
                .map(vote -> new VoteDto(vote.getId(), vote.getOpinion(), vote.getPollOption().getId()))
                .collect(Collectors.toList());
    }

}
