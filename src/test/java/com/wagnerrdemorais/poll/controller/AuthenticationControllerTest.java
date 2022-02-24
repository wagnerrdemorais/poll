package com.wagnerrdemorais.poll.controller;

import com.wagnerrdemorais.poll.controller.form.LoginForm;
import com.wagnerrdemorais.poll.controller.form.NewUserForm;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthenticationControllerTest extends ControllerTestHelper {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DirtiesContext
    void givenNewUser_whenAuthenticate_thenUserShouldBeAuthenticated() throws Exception {
        NewUserForm newUserForm = new NewUserForm();
        newUserForm.setUsername("test");
        newUserForm.setPassword("test");

        runNewUser(mockMvc, newUserForm)
                .andExpect(status().isOk());

        LoginForm loginForm = new LoginForm();
        loginForm.setUsername("test");
        loginForm.setPassword("test");

        String jwtToken = runLogin(mockMvc, loginForm)
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Assertions.assertNotNull(jwtToken);
    }

    @Test
    @DirtiesContext
    void givenNewUser_whenAuthenticateWithWrongInfo_thenShouldRespondWithUnauthorized() throws Exception {
        NewUserForm newUserForm = new NewUserForm();
        newUserForm.setUsername("test");
        newUserForm.setPassword("test");

        runNewUser(mockMvc, newUserForm)
                .andExpect(status().isOk());

        LoginForm loginForm = new LoginForm();
        loginForm.setUsername("test");
        loginForm.setPassword("wrongPass");

        runLogin(mockMvc, loginForm)
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DirtiesContext
    void givenRateLimit_whenRequestLoginMoreThanAllowed_thenShouldRespondWithTooManyRequests()
            throws Exception {
        NewUserForm newUserForm = new NewUserForm();
        newUserForm.setUsername("test");
        newUserForm.setPassword("test");

        runNewUser(mockMvc, newUserForm)
                .andExpect(status().isOk());

        LoginForm loginForm = new LoginForm();
        loginForm.setUsername("test");
        loginForm.setPassword("test");

        for (int i = 0; i < 20; i++) {
            runLogin(mockMvc, loginForm)
                    .andExpect(status().isOk());
        }
        runLogin(mockMvc, loginForm)
                .andExpect(status().isTooManyRequests());
    }
}