package com.wagnerrdemorais.poll.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wagnerrdemorais.poll.controller.form.PollForm;
import com.wagnerrdemorais.poll.controller.form.PollOptionForm;
import com.wagnerrdemorais.poll.dto.PollDto;
import com.wagnerrdemorais.poll.dto.PollOptionDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
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
    void givenEmptyDatabase_whenAddNewPoll_thenShouldGenerateLinkForVoting() throws Exception {
        String response = runAdd(asJsonString(createTestPollForm()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        String testOptionLink = "[{\"optionTitle\":\"TestOption\",\"optionLink\":\"http://localhost/votes/vote?optId=1&opinion=\"}]";
        assertEquals(testOptionLink, response);
    }

    @Test
    @DirtiesContext
    void givenEmptyDatabase_whenAddAndUpdatingAPoll_thenPollShouldBeUpdated() throws Exception {
        runAdd(asJsonString(createTestPollForm()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        String dtoToUpdate = runGetById("1").
                andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        PollForm toUpdate = createAndUpdateFormFromDto(dtoToUpdate, "test1", "test2", "test3");

        String updated = runUpdate(toUpdate)
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        String updated1 = runGetById("1")
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        assertEquals(updated, updated1);
    }

    @Test
    @DirtiesContext
    void givenEmptyDatabase_whenUpdatePollWithNullId_thenShouldReturnBadRequest() throws Exception {
        PollForm updatedForm = new PollForm();
        updatedForm.setId(null);
        runUpdate(updatedForm)
                .andExpect(status().isBadRequest());
    }

    @Test
    @DirtiesContext
    void givenOneAddedPoll_whenDeletingItById_thenPollShouldBeDeleted() throws Exception {
        runAdd(asJsonString(createTestPollForm()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        runDelete("1").andExpect(status().isOk());
        runGetListWithOkResponseAndContent("[]");
    }

    @Test
    @DirtiesContext
    void givenOneAddedPoll_whenDeletingItById_WithWrongId_shouldRespondWithBadRequest() throws Exception {
        runAdd(asJsonString(createTestPollForm()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        runDelete("2")
                .andExpect(status().isBadRequest());
    }

    @Test
    @DirtiesContext
    void givenOneAddedPoll_whenGetPollById_thenShouldReturnCorrespondingPoll() throws Exception {
        PollForm testPollForm = createTestPollForm();
        runAdd(asJsonString(testPollForm))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        String dtoResponse = runGetById("1")
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        PollDto pollDto = new ObjectMapper().readValue(dtoResponse, PollDto.class);

        assertEquals(1L, pollDto.getId());
        assertEquals(testPollForm.getDescription(), pollDto.getDescription());
        assertEquals(testPollForm.getTitle(), pollDto.getTitle());
    }

    @Test
    @DirtiesContext
    void givenOneAddedPoll_whenGetPollById_withNonExistingId_thenShouldRespondWithBadRequest() throws Exception {
        runAdd(asJsonString(createTestPollForm()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        runGetById("0")
                .andExpect(status().isBadRequest());
    }

    private ResultActions runGetById(String id) throws Exception {
        return this.mockMvc.perform(get("/polls/get")
                        .param("id", id))
                .andDo(print());
    }

    private void runGetListWithOkResponseAndContent(String content) throws Exception {
        this.mockMvc.perform(get("/polls/list"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(content)));
    }

    private ResultActions runAdd(String content) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.post("/polls/add")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));
    }

    private ResultActions runUpdate(PollForm updatedForm) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.put("/polls/update")
                .content(asJsonString(updatedForm))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

    }

    private ResultActions runDelete(String id) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.delete("/polls/delete")
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

    private PollForm createAndUpdateFormFromDto(String dto, String title, String description, String optionDesc) throws JsonProcessingException {
        PollDto pollDto = new ObjectMapper().readValue(dto, PollDto.class);
        PollForm update = new PollForm();
        update.setId(pollDto.getId());
        update.setTitle(title);
        update.setDescription(description);
        update.setOptionList(new ArrayList<>());
        for (PollOptionDto pollOptionDto : pollDto.getOptionList()) {
            PollOptionForm pollOptionForm = new PollOptionForm();
            pollOptionForm.setId(pollOptionDto.getId());
            pollOptionForm.setTitle(optionDesc);
            update.getOptionList().add(pollOptionForm);
        }
        return update;
    }

}