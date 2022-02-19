package com.wagnerrdemorais.pool.form;

import com.wagnerrdemorais.pool.model.Poll;

public class PollForm {

    Long id;
    String title;

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

    public Poll toEntity() {
        return new Poll(this.id, this.title);
    }
}
