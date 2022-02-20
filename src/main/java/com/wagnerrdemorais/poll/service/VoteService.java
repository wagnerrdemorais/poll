package com.wagnerrdemorais.poll.service;

import com.wagnerrdemorais.poll.controller.form.VoteForm;
import com.wagnerrdemorais.poll.model.PollOption;
import com.wagnerrdemorais.poll.model.Vote;
import com.wagnerrdemorais.poll.repository.OptionRepository;
import com.wagnerrdemorais.poll.repository.VoteRepository;
import org.springframework.stereotype.Service;

@Service
public class VoteService {

    private final VoteRepository voteRepository;
    private final OptionRepository optionRepository;

    public VoteService(VoteRepository voteRepository, OptionRepository optionRepository) {
        this.voteRepository = voteRepository;
        this.optionRepository = optionRepository;
    }

    public Vote saveVote(Vote vote) {
        return this.voteRepository.save(vote);
    }

    public Vote saveVote(VoteForm voteForm) {
        Vote vote;
        PollOption pollOption = optionRepository.getById(voteForm.getOptionId());
        if (voteForm.getVoteId() == null) {
            vote = new Vote(pollOption, voteForm.getOpinion());
        } else {
            vote = this.voteRepository.getById(voteForm.getVoteId());
            vote.setOpinion(voteForm.getOpinion());
            vote.setPollOption(pollOption);
        }

        Vote savedVote = saveVote(vote);
        optionRepository.save(vote.getPollOption());
        return savedVote;
    }

}
