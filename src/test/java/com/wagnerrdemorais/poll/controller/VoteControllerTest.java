package com.wagnerrdemorais.poll.controller;

import com.wagnerrdemorais.poll.controller.form.PollForm;
import com.wagnerrdemorais.poll.controller.form.PollOptionForm;
import com.wagnerrdemorais.poll.controller.form.VoteForm;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class VoteControllerTest extends ControllerTestHelper {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DirtiesContext
    void givenNewPoll_whenVoting_thenVoteShouldBeSaved() throws Exception {
        runAddPoll(mockMvc, createTestPollForm())
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        VoteForm voteForm = new VoteForm(1L, "test");
        String saved = runNewVote(mockMvc, voteForm)
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Assertions.assertEquals("{\"id\":1,\"opinion\":\"test\",\"pollOptionId\":1}", saved);
    }

    @Test
    @DirtiesContext
    void givenAddedPollWithTwoVotes_whenGetVotes_thenShouldReturnPollWithTwoVotes() throws Exception {
        runAddPoll(mockMvc, createTestPollForm())
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        VoteForm voteForm = new VoteForm(1L, null);
        runNewVote(mockMvc, voteForm).andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        voteForm = new VoteForm(1L, "test");
        runNewVote(mockMvc, voteForm).andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        String contentAsString = runGetVotes(mockMvc, "1")
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        String expected = "{\"pollTitle\":\"TestTitle\",\"pollDescription\":\"TestDescription\",\"optionVotes\":[{\"optionTitle\":\"TestOption\",\"voteCount\":2,\"opinions\":[\"test\"]}]}";
        Assertions.assertEquals(expected, contentAsString);
    }

    @Test
    @DirtiesContext
    void givenNewVote_whenUpdateVote_thenVoteShouldBeUpdated() throws Exception {
        runAddPoll(mockMvc, createTestPollForm())
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        VoteForm voteForm = new VoteForm(1L, null);
        runNewVote(mockMvc, voteForm).andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        voteForm = new VoteForm(1L, 1L, "test");
        runUpdateVote(mockMvc, voteForm).andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        String contentAsString = runGetVotes(mockMvc, "1")
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        String expected = "{\"pollTitle\":\"TestTitle\",\"pollDescription\":\"TestDescription\",\"optionVotes\":[{\"optionTitle\":\"TestOption\",\"voteCount\":1,\"opinions\":[\"test\"]}]}";
        Assertions.assertEquals(expected, contentAsString);
    }

    @Test
    @DirtiesContext
    void givenAVoteToUpdate_whenRequestingToNewVote_shouldRespondWithBadRequest() throws Exception {
        VoteForm voteForm = new VoteForm(1L, 1L, null);
        runNewVote(mockMvc, voteForm).andExpect(status().isBadRequest());
    }

    @Test
    @DirtiesContext
    void givenEmptyPolls_whenGetVotesWithNonExistingId_thenShouldRespondWithBadRequest() throws Exception {
        runGetVotes(mockMvc, "1")
                .andExpect(status().isBadRequest());
    }

    private PollForm createTestPollForm() {
        PollOptionForm pollOptionForm = new PollOptionForm();
        pollOptionForm.setTitle("TestOption");

        PollForm pollForm = new PollForm();
        pollForm.setDescription("TestDescription");
        pollForm.setTitle("TestTitle");
        pollForm.setOptionList(List.of(pollOptionForm));
        return pollForm;
    }

}