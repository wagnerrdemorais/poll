package com.wagnerrdemorais.poll.dto;

import java.util.List;

/**
 * Contains the title, description and a list of votes and opinions for each poll Option
 */
public class PollVotesDto {

    String pollTitle;
    String pollDescription;
    List<OptionVotesDto> optionVotes;

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

    public List<OptionVotesDto> getOptionVotes() {
        return optionVotes;
    }

    public void setOptionVotes(List<OptionVotesDto> optionVotes) {
        this.optionVotes = optionVotes;
    }


    /**
     * Contains voteCount for each vote with a List of opinions
     */
    public static class OptionVotesDto {

        String optionTitle;
        int voteCount;
        List<String> opinions;

        public String getOptionTitle() {
            return optionTitle;
        }

        public void setOptionTitle(String optionTitle) {
            this.optionTitle = optionTitle;
        }

        public int getVoteCount() {
            return voteCount;
        }

        public void setVoteCount(int voteCount) {
            this.voteCount = voteCount;
        }

        public List<String> getOpinions() {
            return opinions;
        }

        public void setOpinions(List<String> opinions) {
            this.opinions = opinions;
        }
    }
}




