package com.wagnerrdemorais.poll.controller.form;

import com.wagnerrdemorais.poll.model.Poll;
import com.wagnerrdemorais.poll.model.PollOption;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Class used for receiving Polls in Controller
 */
public class PollForm {

    Long id;
    String title;
    String description;
    List<PollOptionForm> optionList = new ArrayList<>();

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

    public List<PollOptionForm> getOptionList() {
        return optionList;
    }

    public void setOptionList(List<PollOptionForm> optionList) {
        this.optionList = optionList;
    }

    /**
     * Converts PollForm to Poll object
     * @return Poll
     */
    public Poll toEntity() {
        List<PollOption> optionList = this.optionList.stream()
                .map(pof -> new PollOption(pof.getId(), pof.getTitle(), pof.getVoteCount()))
                .collect(Collectors.toList());

        return new Poll(this.id, this.title, this.description, optionList);
    }
}
