package com.wagnerrdemorais.poll.controller.form;

/**
 * Form with information received by the controller to register a new vote.
 */
public class VoteForm {

    Long voteId;
    Long optionId;
    String opinion;

    public VoteForm(Long optionId, String opinion) {
        this.optionId = optionId;
        this.opinion = opinion;
    }

    public VoteForm(Long voteId, Long optionId, String opinion) {
        this.voteId = voteId;
        this.optionId = optionId;
        this.opinion = opinion;
    }

    public Long getVoteId() {
        return voteId;
    }

    public Long getOptionId() {
        return optionId;
    }

    public String getOpinion() {
        return opinion;
    }

}
