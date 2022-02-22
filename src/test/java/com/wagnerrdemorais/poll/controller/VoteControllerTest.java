package com.wagnerrdemorais.poll.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wagnerrdemorais.poll.controller.form.PollForm;
import com.wagnerrdemorais.poll.controller.form.PollOptionForm;
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

        String saved = runSave("1", "test").andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Assertions.assertEquals("{\"id\":1,\"opinion\":\"test\",\"pollOptionId\":1}", saved);
    }

    private ResultActions runSave(String optionId, String opinion) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.get("/vote/new")
                .param("optId", optionId)
                .param("opinion", opinion));
    }

    private ResultActions runAdd(String content) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.post("/poll/add")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));
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