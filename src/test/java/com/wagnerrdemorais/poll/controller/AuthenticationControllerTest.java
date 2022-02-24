package com.wagnerrdemorais.poll.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wagnerrdemorais.poll.controller.form.LoginForm;
import com.wagnerrdemorais.poll.controller.form.NewUserForm;
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

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DirtiesContext
    void authenticate() throws Exception {
        NewUserForm newUserForm = new NewUserForm();
        newUserForm.setUsername("test");
        newUserForm.setPassword("test");

        runAdd(newUserForm)
                .andExpect(status().isOk());

        LoginForm loginForm = new LoginForm();
        loginForm.setUsername("test");
        loginForm.setPassword("test");

        String jwtToken = runLogin(loginForm)
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Assertions.assertNotNull(jwtToken);
    }

    @Test
    @DirtiesContext
    void authenticateBad() throws Exception {
        NewUserForm newUserForm = new NewUserForm();
        newUserForm.setUsername("test");
        newUserForm.setPassword("test");

        runAdd(newUserForm)
                .andExpect(status().isOk());

        LoginForm loginForm = new LoginForm();
        loginForm.setUsername("test");
        loginForm.setPassword("NotCorrrect");

        runLogin(loginForm)
                .andExpect(status().isBadRequest());
    }

    @Test
    @DirtiesContext
    void rateLimit() throws Exception {
        NewUserForm newUserForm = new NewUserForm();
        newUserForm.setUsername("test");
        newUserForm.setPassword("test");

        runAdd(newUserForm)
                .andExpect(status().isOk());

        LoginForm loginForm = new LoginForm();
        loginForm.setUsername("test");
        loginForm.setPassword("test");

        for (int i = 0; i < 20; i++) {
            runLogin(loginForm)
                    .andExpect(status().isOk());
        }
        runLogin(loginForm)
                .andExpect(status().isTooManyRequests());
    }

    private ResultActions runAdd(NewUserForm content) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.post("/users/new")
                .content(asJsonString(content))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));
    }

    private ResultActions runLogin(LoginForm content) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.post("/login")
                .content(asJsonString(content))
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

}