package com.wagnerrdemorais.poll.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wagnerrdemorais.poll.controller.form.*;
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

    @Test
    void givenNoUser_whenGetByUserId_thenShouldReturnBadRequest() throws Exception {
        runGetPollByUserId(mockMvc, "1")
                .andExpect(status().isBadRequest());
    }

    @Test
    @DirtiesContext
    void givenNewPollWithUser_whenGetPollByUserId_thenPollShouldBeReturned() throws Exception {
        NewUserForm newUserForm = new NewUserForm();
        newUserForm.setUsername("test");
        newUserForm.setPassword("test");

        runNewUser(mockMvc, objectAsJsonString(newUserForm))
                .andExpect(status().isOk());

        PollForm testPollForm = createTestPollFormWithUser(1L);
        runAddPoll(mockMvc, objectAsJsonString(testPollForm))
                .andExpect(status().isOk());

        String response = runGetPollByUserId(mockMvc, "1")
                .andExpect(status().isOk()).andReturn()
                .getResponse().getContentAsString();

        String expected = "[{\"id\":1,\"title\":\"TestTitle\",\"description\":\"TestDescription\",\"requireAuth\":false,\"optionList\":[{\"id\":1,\"title\":\"TestOption\",\"voteList\":[]}]}]";
        assertEquals(expected, response);
    }

    @Test
    @DirtiesContext
    void givenWrongToken_whenRequireAuth_souldReturnBadRequest() throws Exception {
        runRequireAuth(mockMvc, List.of(1L), "jwtToken")
                .andExpect(status().is(401));
    }

    @Test
    @DirtiesContext
    void givenPollListWithAndWithoutUser_whenGetListWithoutUser_thenShouldReturnOnlyPollsWithoutUser() throws Exception {
        NewUserForm newUserForm = new NewUserForm();
        newUserForm.setUsername("test");
        newUserForm.setPassword("test");

        runNewUser(mockMvc, objectAsJsonString(newUserForm))
                .andExpect(status().isOk());

        PollForm testPollForm = createTestPollFormWithUser(1L);
        runAddPoll(mockMvc, objectAsJsonString(testPollForm))
                .andExpect(status().isOk());

        runAddPoll(mockMvc, objectAsJsonString(createTestPollForm()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        String response = runGetPollListWithoutUser(mockMvc)
                .andExpect(status().isOk()).andReturn()
                .getResponse().getContentAsString();

        String expected = "[{\"id\":2,\"title\":\"TestTitle\",\"description\":\"TestDescription\",\"requireAuth\":false,\"optionList\":[{\"id\":2,\"title\":\"TestOption\",\"voteList\":[]}]}]";
        assertEquals(expected, response);
    }

    @Test
    @DirtiesContext
    void givenWrongToken_whenClaimPolls_thenShouldRespondWithUnauthorized() throws Exception {
        runClaimPolls(mockMvc, "jwtToken")
                .andExpect(status().is(401));
    }

    /**
     * Creates a list of polls with user, and requiring authentication,
     * when voting without authentication, should return unauthorized
     * when voting with authentication, should return okay
     * @throws Exception E
     */
    @Test
    @DirtiesContext
    void flow_test() throws Exception {
        NewUserForm newUserForm = new NewUserForm();
        newUserForm.setUsername("test");
        newUserForm.setPassword("test");

        runNewUser(mockMvc, objectAsJsonString(newUserForm))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        LoginForm loginForm = new LoginForm();
        loginForm.setUsername("test");
        loginForm.setPassword("test");

        String jwtToken = runLogin(mockMvc, loginForm)
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        PollForm testPollForm = createTestPollFormWithUser(1L);
        runAddPoll(mockMvc, objectAsJsonString(testPollForm))
                .andExpect(status().isOk());

        runAddPoll(mockMvc, objectAsJsonString(createTestPollForm()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        String response = runClaimPolls(mockMvc, jwtToken)
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        String expected = "[{\"id\":2,\"title\":\"TestTitle\",\"description\":\"TestDescription\",\"requireAuth\":false,\"optionList\":[{\"id\":2,\"title\":\"TestOption\",\"voteList\":[]}]}]";
        assertEquals(expected, response);

        String expected1 = "[]";
        String response1 = runClaimPolls(mockMvc, jwtToken)
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        assertEquals(expected1, response1);

        String myPolls = runGetMyPolls(mockMvc, jwtToken)
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        String expec = "[{\"id\":1,\"title\":\"TestTitle\",\"description\":\"TestDescription\",\"requireAuth\":false,\"optionList\":[{\"id\":1,\"title\":\"TestOption\",\"voteList\":[]}]},{\"id\":2,\"title\":\"TestTitle\",\"description\":\"TestDescription\",\"requireAuth\":false,\"optionList\":[{\"id\":2,\"title\":\"TestOption\",\"voteList\":[]}]}]";

        assertEquals(expec, myPolls);

        String updated = runRequireAuth(mockMvc, List.of(1L), jwtToken)
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        String exp2 = "[{\"id\":1,\"title\":\"TestTitle\",\"description\":\"TestDescription\",\"requireAuth\":true,\"optionList\":[{\"id\":1,\"title\":\"TestOption\",\"voteList\":[]}]}]";

        assertEquals(exp2, updated);

        VoteForm voteForm = new VoteForm(1L, "test");
        runNewVote(mockMvc, voteForm)
                .andExpect(status().is(401));

        runNewVote(mockMvc, voteForm, jwtToken)
                .andExpect(status().isOk());
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

    private PollForm createTestPollFormWithUser(Long userId) {
        PollOptionForm pollOptionForm = new PollOptionForm();
        pollOptionForm.setTitle("TestOption");

        PollForm pollForm = new PollForm();
        pollForm.setDescription("TestDescription");
        pollForm.setTitle("TestTitle");
        pollForm.setOptionList(List.of(pollOptionForm));
        pollForm.setUserId(userId);
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