package com.wagnerrdemorais.pool.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Poll {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    @OneToMany(cascade = CascadeType.ALL)
    private List<PollOption> optionList = new ArrayList<>();

    public Poll() {
    }

    public Poll(Long id, String title, String description, List<PollOption> optionList) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.optionList = optionList;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<PollOption> getOptionList() {
        return optionList;
    }

    public void setOptionList(List<PollOption> optionList) {
        this.optionList = optionList;
    }
}

