package com.wagnerrdemorais.poll.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wagnerrdemorais.poll.controller.form.LoginForm;
import com.wagnerrdemorais.poll.controller.form.NewUserForm;
import com.wagnerrdemorais.poll.controller.form.PollForm;
import com.wagnerrdemorais.poll.controller.form.VoteForm;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

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

    ResultActions runGetVotes(MockMvc mockMvc, String pollId) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.get("/votes/getVotes")
                .param("pollId", pollId));
    }

    public static String objectAsJsonString(final Object obj) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
