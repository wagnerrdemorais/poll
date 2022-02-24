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
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

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
class PollControllerTest extends ControllerTestHelper {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DirtiesContext
    void givenEmptyDatabase_whenAddNewPoll_thenShouldGenerateLinkForVoting() throws Exception {
        String response = runAddPoll(mockMvc, objectAsJsonString(createTestPollForm()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        String testOptionLink = "[{\"optionTitle\":\"TestOption\",\"optionLink\":\"http://localhost/votes/newVote?optId=1&opinion=\"}]";
        assertEquals(testOptionLink, response);
    }

    @Test
    @DirtiesContext
    void givenEmptyDatabase_whenAddAndUpdatingAPoll_thenPollShouldBeUpdated() throws Exception {
        runAddPoll(mockMvc, objectAsJsonString(createTestPollForm()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        String dtoToUpdate = runGetById("1").
                andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        PollForm toUpdate = createAndUpdateFormFromDto(dtoToUpdate, "test1", "test2", "test3");

        String updated = runUpdatePoll(mockMvc, toUpdate)
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
        runUpdatePoll(mockMvc, updatedForm)
                .andExpect(status().isBadRequest());
    }

    @Test
    @DirtiesContext
    void givenOneAddedPoll_whenDeletingItById_thenPollShouldBeDeleted() throws Exception {
        runAddPoll(mockMvc, objectAsJsonString(createTestPollForm()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        runDeletePoll(mockMvc, "1").andExpect(status().isOk());
        runGetListWithOkResponseAndContent("[]");
    }

    @Test
    @DirtiesContext
    void givenOneAddedPoll_whenDeletingItById_WithWrongId_shouldRespondWithBadRequest() throws Exception {
        runAddPoll(mockMvc, objectAsJsonString(createTestPollForm()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        runDeletePoll(mockMvc, "2")
                .andExpect(status().isBadRequest());
    }

    @Test
    @DirtiesContext
    void givenOneAddedPoll_whenGetPollById_thenShouldReturnCorrespondingPoll() throws Exception {
        PollForm testPollForm = createTestPollForm();
        runAddPoll(mockMvc, objectAsJsonString(testPollForm))
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
        runAddPoll(mockMvc, objectAsJsonString(createTestPollForm()))
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

    private PollForm createTestPollForm() {
        PollOptionForm pollOptionForm = new PollOptionForm();
        pollOptionForm.setTitle("TestOption");

        PollForm pollForm = new PollForm();
        pollForm.setDescription("TestDescription");
        pollForm.setTitle("TestTitle");
        pollForm.setOptionList(List.of(pollOptionForm));
        return pollForm;
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