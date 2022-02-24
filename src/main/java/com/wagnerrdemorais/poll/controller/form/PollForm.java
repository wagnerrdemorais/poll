package com.wagnerrdemorais.poll.controller.form;

import java.util.ArrayList;
import java.util.List;

/**
 * Class used for receiving Polls in Controller
 */
public class PollForm {

    Long id;
    String title;
    String description;
    Long userId;
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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
