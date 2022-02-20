package com.wagnerrdemorais.poll.controller.form;

/**
 * Used for receiving PollOptions in Controller
 */
public class PollOptionForm {

    private Long id;

    private String title;

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

}
