package com.wagnerrdemorais.poll.dto;

import java.util.List;

public class VotePageDto {

    String pollTitle;
    String pollDescription;
    List<PollOptionLink> optionLinks;

    public VotePageDto() {
    }

    public VotePageDto(String pollTitle, String pollDescription, List<PollOptionLink> optionLinks) {
        this.pollTitle = pollTitle;
        this.pollDescription = pollDescription;
        this.optionLinks = optionLinks;
    }

    public String getPollTitle() {
        return pollTitle;
    }

    public void setPollTitle(String pollTitle) {
        this.pollTitle = pollTitle;
    }

    public String getPollDescription() {
        return pollDescription;
    }

    public void setPollDescription(String pollDescription) {
        this.pollDescription = pollDescription;
    }

    public List<PollOptionLink> getOptionLinks() {
        return optionLinks;
    }

    public void setOptionLinks(List<PollOptionLink> optionLinks) {
        this.optionLinks = optionLinks;
    }

    public static class PollOptionLink {

        String optionTitle;
        String optionLink;

        public PollOptionLink() {
        }

        public PollOptionLink(String optionTitle, String optionLink) {
            this.optionTitle = optionTitle;
            this.optionLink = optionLink;
        }

        public String getOptionTitle() {
            return optionTitle;
        }

        public void setOptionTitle(String optionTitle) {
            this.optionTitle = optionTitle;
        }

        public String getOptionLink() {
            return optionLink;
        }

        public void setOptionLink(String optionLink) {
            this.optionLink = optionLink;
        }
    }
}

