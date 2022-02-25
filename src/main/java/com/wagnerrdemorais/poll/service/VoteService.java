package com.wagnerrdemorais.poll.service;

import com.wagnerrdemorais.poll.controller.form.VoteForm;
import com.wagnerrdemorais.poll.model.PollOption;
import com.wagnerrdemorais.poll.model.Vote;
import com.wagnerrdemorais.poll.repository.OptionRepository;
import com.wagnerrdemorais.poll.repository.VoteRepository;
import org.springframework.stereotype.Service;

/**
 * Service to handle Vote operations
 */
@Service
public class VoteService {

    private final VoteRepository voteRepository;
    private final OptionRepository optionRepository;

    /**
     * All args constructor
     *
     * @param voteRepository   VoteRepository
     * @param optionRepository OptionRepository
     */
    public VoteService(VoteRepository voteRepository, OptionRepository optionRepository) {
        this.voteRepository = voteRepository;
        this.optionRepository = optionRepository;
    }

    /**
     * Saves the given Vote and returns the saved object
     *
     * @param vote Vote
     * @return Vote
     */
    public Vote saveVote(Vote vote) {
        return this.voteRepository.save(vote);
    }

    /**
     * Converts given VoteForm To Vote, saves it and return the saved object
     *
     * @param voteForm VoteForm
     * @return Vote
     */
    public Vote saveVote(VoteForm voteForm) {
        Vote vote = voteFormToVote(voteForm);
        return saveVote(vote);
    }

    /**
     * Converts VoteForm to Vote
     *
     * @param voteForm VoteForm
     * @return Vote
     */
    private Vote voteFormToVote(VoteForm voteForm) {
        Vote vote;
        PollOption pollOption = optionRepository.getById(voteForm.getOptionId());

        if (voteForm.getVoteId() == null) {
            vote = new Vote(voteForm.getVoteId(), pollOption, voteForm.getOpinion());
        } else {
            vote = this.voteRepository.getById(voteForm.getVoteId());
            vote.setOpinion(voteForm.getOpinion());
            vote.setPollOption(pollOption);
        }
        return vote;
    }

    /**
     * Return Vote for given id if found
     * @param id Long
     * @return Vote
     */
    public Vote getVoteById(Long id) {
        return this.voteRepository.getById(id);
    }
}