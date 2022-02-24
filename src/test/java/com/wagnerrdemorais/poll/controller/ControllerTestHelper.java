package com.wagnerrdemorais.poll.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wagnerrdemorais.poll.controller.form.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

/**
 * Helper for controller tests
 */
public abstract class ControllerTestHelper {

    ResultActions runNewUser(MockMvc mockMvc, NewUserForm content) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.post("/users/new")
                .content(objectAsJsonString(content))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));
    }

    ResultActions runLogin(MockMvc mockMvc, LoginForm content) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.post("/login")
                .content(objectAsJsonString(content))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));
    }

    ResultActions runUpdatePoll(MockMvc mockMvc, PollForm updatedForm) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.put("/polls/update")
                .content(objectAsJsonString(updatedForm))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));
    }

    ResultActions runDeletePoll(MockMvc mockMvc, String id) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.delete("/polls/delete")
                .param("id", id));
    }

    ResultActions runAddPoll(MockMvc mockMvc, String content) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.post("/polls/add")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));
    }

    ResultActions runNewVote(MockMvc mockMvc, VoteForm content) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.post("/votes/newVote")
                .content(objectAsJsonString(content))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));
    }

    ResultActions runUpdateVote(MockMvc mockMvc, VoteForm content) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.post("/votes/updateVote")
                .content(objectAsJsonString(content))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));
    }

    ResultActions runAddPoll(MockMvc mockMvc, PollForm content) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.post("/polls/add")
                .content(objectAsJsonString(content))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));
    }

    ResultActions runGetPollByUserId(MockMvc mockMvc, String userId) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.get("/polls/listByUserId")
                .param("userId", userId));
    }

    ResultActions runGetPollListWithoutUser(MockMvc mockMvc) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.get("/polls/listWithoutUser"));
    }

    ResultActions runGetVotes(MockMvc mockMvc, String pollId) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.get("/votes/getVotes")
                .param("pollId", pollId));
    }

    ResultActions runGetMyPolls(MockMvc mockMvc, String token) throws Exception {
        return mockMvc.perform(get("/polls/myPolls")
                    .header("authorization", "Bearer " + token));
    }

    ResultActions runGetUserList(MockMvc mockMvc) throws Exception {
        return mockMvc.perform(get("/users/list"))
                .andDo(print());
    }

    ResultActions runNewUser(MockMvc mockMvc, String content) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.post("/users/new")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));
    }

    ResultActions runUpdateUser(MockMvc mockMvc, UpdateUserForm updatedForm) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.put("/users/update")
                .content(objectAsJsonString(updatedForm))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));
    }

    ResultActions runClaimPolls(MockMvc mockMvc, String token) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.put("/polls/claimPolls")
                .header("authorization", "Bearer " + token));
    }

    ResultActions runDeleteUser(MockMvc mockMvc, String id) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.delete("/users/delete")
                .param("id", id));
    }

    ResultActions runGetUserById(MockMvc mockMvc, String id) throws Exception {
        return mockMvc.perform(get("/users/get")
                        .param("userId", id))
                .andDo(print());
    }

    static String objectAsJsonString(final Object obj) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
