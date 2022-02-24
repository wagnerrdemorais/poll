package com.wagnerrdemorais.poll.service;

import com.wagnerrdemorais.poll.controller.form.VoteForm;
import com.wagnerrdemorais.poll.model.PollOption;
import com.wagnerrdemorais.poll.model.Vote;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Map;

class VoteServiceTest extends RepoTestHelper {

    VoteService subject;

    @BeforeEach
    void setUp() {
        initializeVotesAndOptions();

        this.subject = new VoteService(voteRepository, optionRepository);
    }

    @Test
    void givenNoSavedVotes_whenSaveVote_thenVoteShouldBeSaved() {
        Vote vote = new Vote(3L, new PollOption(), "testOpinion");
        Vote saveVote = this.subject.saveVote(vote);
        Assertions.assertEquals(vote, saveVote);
    }

    @Test
    void givenNewVote_whenSavingVote_thenVoteShouldBeSaved() {
        VoteForm voteForm = new VoteForm(1L, "testOp");
        Vote saveVote = subject.saveVote(voteForm);
        Assertions.assertEquals("testOp", saveVote.getOpinion());
    }

    @Test
    void givenNewVote_whenUpdateVote_thenVoteShouldBeUpdated() {
        Vote vote = subject.getVoteById(1L);
        vote.setOpinion("test1");

        VoteForm voteForm = new VoteForm(vote.getId(), vote.getPollOption().getId(), "testOp");
        Vote savedVote = subject.saveVote(voteForm);

        Assertions.assertEquals("testOp", savedVote.getOpinion());
    }

    /**
     * Initializes voteMap and optionMap with test data
     */
    private void initializeVotesAndOptions() {
        Map<Long, PollOption> mockPollOptionMap = Map.of(1L, new PollOption(1L, "Option1", new ArrayList<>()),
                2L, new PollOption(2L, "Option2", new ArrayList<>()));

        Map<Long, Vote> mockVoteMap = Map.of(1L, new Vote(1L, mockPollOptionMap.get(1L), "opinion1"),
                2L, new Vote(2L, mockPollOptionMap.get(2L), "opinion2"));

        voteMap.putAll(mockVoteMap);
        optionMap.putAll(mockPollOptionMap);
    }
}