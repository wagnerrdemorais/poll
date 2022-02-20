package com.wagnerrdemorais.poll.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wagnerrdemorais.poll.controller.form.PollForm;
import com.wagnerrdemorais.poll.controller.form.PollOptionForm;
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

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PollControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DirtiesContext
    void givenEmptyDatabase_whenAddNewPoll_thenAddedPollShouldBeAvailable() throws Exception {
        runGetListWithOkResponseAndContent("[]");

        String response = runAdd(asJsonString(createTestPollForm()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        runGetListWithOkResponseAndContent(response);
    }

    @Test
    @DirtiesContext
    void givenEmptyDatabase_whenAddAndUpdatingAPoll_thenPollShouldBeUpdated() throws Exception {
        runGetListWithOkResponseAndContent("[]");

        String response = runAdd(asJsonString(createTestPollForm()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        runGetListWithOkResponseAndContent(response);

        PollForm updatedForm = asPollFormJson(response);
        updatedForm.setDescription("UpdatedDescription");
        updatedForm.setTitle("UpdatedTitle");

        String updated = runUpdate(updatedForm)
                .andExpect(status().isOk())
                .andReturn()
                .getResponse().getContentAsString();

        runGetListWithOkResponseAndContent(updated);
    }

    @Test
    @DirtiesContext
    void givenEmptyDatabase_whenUpdatePollWithNullId_thenShouldReturnBadRequest() throws Exception {
        runGetListWithOkResponseAndContent("[]");

        String response = runAdd(asJsonString(createTestPollForm()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        runGetListWithOkResponseAndContent(response);

        PollForm updatedForm = asPollFormJson(response);
        updatedForm.setId(null);

        runUpdate(updatedForm)
                .andExpect(status().isBadRequest());
    }

    @Test
    @DirtiesContext
    void givenOneAddedPoll_whenDeletingItById_thenPollShouldBeDeleted() throws Exception {
        runGetListWithOkResponseAndContent("[]");

        String response = runAdd(asJsonString(createTestPollForm()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        runGetListWithOkResponseAndContent(response);

        runDelete("1")
                .andExpect(status().isOk());

        runGetListWithOkResponseAndContent("[]");
    }

    @Test
    @DirtiesContext
    void givenOneAddedPoll_whenDeletingItById_WithWrongId_shouldRespondWithBadRequest() throws Exception {
        runGetListWithOkResponseAndContent("[]");

        String response = runAdd(asJsonString(createTestPollForm()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        runGetListWithOkResponseAndContent(response);

        runDelete("2")
                .andExpect(status().isBadRequest());

        runGetListWithOkResponseAndContent(response);
    }

    @Test
    @DirtiesContext
    void givenOneAddedPoll_whenGetPollById_thenShouldReturnCorrespondingPoll() throws Exception {
        runGetListWithOkResponseAndContent("[]");

        String added = runAdd(asJsonString(createTestPollForm()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        runGetById("1")
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(added)));
    }

    @Test
    @DirtiesContext
    void givenOneAddedPoll_whenGetPollById_withNonExistingId_thenShouldRespondWithBadRequest() throws Exception {
        runGetListWithOkResponseAndContent("[]");

        runAdd(asJsonString(createTestPollForm()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        runGetById("0")
                .andExpect(status().isBadRequest());
    }

    private ResultActions runGetById(String id) throws Exception {
        return this.mockMvc.perform(get("/poll/get")
                        .param("id", id))
                .andDo(print());
    }

    private void runGetListWithOkResponseAndContent(String content) throws Exception {
        this.mockMvc.perform(get("/poll/list"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(content)));
    }

    private ResultActions runAdd(String content) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.post("/poll/add")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));
    }

    private ResultActions runUpdate(PollForm updatedForm) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.put("/poll/update")
                        .content(asJsonString(updatedForm))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));

    }

    private ResultActions runDelete(String id) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.delete("/poll/delete")
                        .param("id", id));
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

    public static String asJsonString(final Object obj) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static PollForm asPollFormJson(final String value) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(value, PollForm.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}