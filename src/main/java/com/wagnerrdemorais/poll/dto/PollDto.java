package com.wagnerrdemorais.poll.dto;

import com.wagnerrdemorais.poll.model.Poll;
import com.wagnerrdemorais.poll.model.PollOption;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<PollOptionDto> getOptionList() {
        return optionList;
    }

    public void setOptionList(List<PollOptionDto> optionList) {
        this.optionList = optionList;
    }

    /**
     * Converts this PollDto to Poll Entity
     * @return Poll
     */
    public Poll toEntity() {
        List<PollOption> pollOptionList = this.optionList.stream()
                .map(opt -> new PollOption(opt.getId(), opt.getTitle(), opt.getVoteCount()))
                .collect(Collectors.toList());

        return new Poll(this.id, this.title, this.description, pollOptionList);
    }

    /**
     * Receives a Poll and converts it to PollDto
     * @param poll Poll
     * @return PollDto
     */
    public static PollDto fromEntity(Poll poll) {
        List<PollOptionDto> pollOptionDtos = poll.getOptionList().stream()
                .map(opt -> new PollOptionDto(opt.getId(), opt.getTitle(), opt.getVoteCount()))
                .collect(Collectors.toList());

        return new PollDto(poll.getId(), poll.getTitle(), poll.getDescription(), pollOptionDtos);
    }
}