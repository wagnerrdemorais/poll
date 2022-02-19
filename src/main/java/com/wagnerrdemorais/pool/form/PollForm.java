package com.wagnerrdemorais.pool.form;

import com.wagnerrdemorais.pool.model.Poll;
import com.wagnerrdemorais.pool.model.PollOption;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PollForm {

    Long id;
    String title;
    String description;
    List<PoolOptionForm> optionList = new ArrayList<>();

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

    public List<PoolOptionForm> getOptionList() {
        return optionList;
    }

    public void setOptionList(List<PoolOptionForm> optionList) {
        this.optionList = optionList;
    }

    public Poll toEntity() {
        List<PollOption> optionList = this.optionList.stream()
                .map(pof -> new PollOption(pof.getId(), pof.getTitle(), pof.getVoteCount()))
                .collect(Collectors.toList());

        return new Poll(this.id, this.title, this.description, optionList);
    }
}
