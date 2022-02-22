package com.wagnerrdemorais.poll.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wagnerrdemorais.poll.controller.form.PollForm;
import com.wagnerrdemorais.poll.controller.form.PollOptionForm;
import com.wagnerrdemorais.poll.controller.form.VoteForm;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class VoteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DirtiesContext
    void vote() throws Exception {
        runAdd(asJsonString(createTestPollForm()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        VoteForm voteForm = new VoteForm(1L, "test");
        String saved = runNewVote(asJsonString(voteForm)).andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Assertions.assertEquals("{\"id\":1,\"opinion\":\"test\",\"pollOptionId\":1}", saved);
    }

    @Test
    @DirtiesContext
    void givenAddedPollWithTwoVotes_whenGetVotes_thenShouldReturnPollWithTwoVotes() throws Exception {
        runAdd(asJsonString(createTestPollForm()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        VoteForm voteForm = new VoteForm(1L, null);

        runNewVote(asJsonString(voteForm)).andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        voteForm = new VoteForm(1L, "test");

        runNewVote(asJsonString(voteForm)).andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        String contentAsString = runGetVotes("1")
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        String expected = "{\"pollTitle\":\"TestTitle\",\"pollDescription\":\"TestDescription\",\"optionVotes\":[{\"optionTitle\":\"TestOption\",\"voteCount\":2,\"opinions\":[\"test\"]}]}";
        Assertions.assertEquals(expected, contentAsString);
    }

    @Test
    @DirtiesContext
    void givenEmptyPolls_whenGetVotesWithNonExistingId_thenShouldRespondWithBadRequest() throws Exception {
        runGetVotes("1")
                .andExpect(status().isBadRequest());
    }

    private ResultActions runNewVote(String content) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.post("/votes/vote")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));
    }

    private ResultActions runAdd(String content) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.post("/polls/add")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));
    }

    private ResultActions runGetVotes(String pollId) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.get("/votes/getVotes")
                .param("pollId", pollId));
    }

    public static String asJsonString(final Object obj) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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