package com.wagnerrdemorais.poll.dto;

import java.util.List;

/**
 * Dto with poll and options descriptions, with links for voting
 */
public class VotePageDto {

    String pollTitle;
    String pollDescription;
    List<PollOptionLink> optionLinks;

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

    /**
     * Hold optionTitle and links for voting
     */
    public static class PollOptionLink {

        String optionTitle;
        String optionLink;

        /**
         * All args Constructor
         * @param optionTitle
         * @param optionLink
         */
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

