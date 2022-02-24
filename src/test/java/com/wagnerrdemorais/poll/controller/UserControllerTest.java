package com.wagnerrdemorais.poll.controller;

import com.wagnerrdemorais.poll.controller.form.NewUserForm;
import com.wagnerrdemorais.poll.controller.form.UpdateUserForm;
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
class UserControllerTest extends ControllerTestHelper {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DirtiesContext
    void listUsers() throws Exception {
        NewUserForm newUserForm = new NewUserForm();
        newUserForm.setUsername("test");
        newUserForm.setPassword("test");

        runNewUser(mockMvc, objectAsJsonString(newUserForm))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        String expected = "[{\"id\":1,\"username\":\"test\"}]";
        String response = runGetUserList(mockMvc).andReturn().getResponse().getContentAsString();

        Assertions.assertEquals(expected, response);
    }

    @Test
    @DirtiesContext
    void getUser() throws Exception {
        NewUserForm newUserForm = new NewUserForm();
        newUserForm.setUsername("test");
        newUserForm.setPassword("test");

        runNewUser(mockMvc, objectAsJsonString(newUserForm))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        String expected = "{\"id\":1,\"username\":\"test\"}";
        String response = runGetUserById(mockMvc, "1").andReturn().getResponse().getContentAsString();

        Assertions.assertEquals(expected, response);
    }

    @Test
    @DirtiesContext
    void newUser() throws Exception {
        NewUserForm newUserForm = new NewUserForm();
        newUserForm.setUsername("test");
        newUserForm.setPassword("test");

        String addedUser = runNewUser(mockMvc, objectAsJsonString(newUserForm))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        String expected = "{\"id\":1,\"username\":\"test\"}";
        Assertions.assertEquals(expected, addedUser);
    }

    @Test
    @DirtiesContext
    void updateUser() throws Exception {
        NewUserForm newUserForm = new NewUserForm();
        newUserForm.setUsername("test");
        newUserForm.setPassword("test");

        runNewUser(mockMvc, objectAsJsonString(newUserForm))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        UpdateUserForm updateUserForm = new UpdateUserForm();
        updateUserForm.setId(1L);
        updateUserForm.setUsername("updated");
        updateUserForm.setPassword("updated");

        String updated = runUpdateUser(mockMvc, updateUserForm)
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        String expected = "{\"id\":1,\"username\":\"updated\"}";
        Assertions.assertEquals(expected, updated);
    }

    @Test
    @DirtiesContext
    void deleteUserById() throws Exception {
        NewUserForm newUserForm = new NewUserForm();
        newUserForm.setUsername("test");
        newUserForm.setPassword("test");

        runNewUser(mockMvc, objectAsJsonString(newUserForm))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        String userList = runGetUserList(mockMvc)
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        String expected = "[{\"id\":1,\"username\":\"test\"}]";
        Assertions.assertEquals(expected, userList);

        runDeleteUser(mockMvc, "1").andExpect(status().isOk());

        String list2 = runGetUserList(mockMvc)
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Assertions.assertEquals("[]", list2);
    }

    @Test
    @DirtiesContext
    void getWrongUser() throws Exception {
        runGetUserById(mockMvc, "1").andExpect(status().isBadRequest());

    }

    @Test
    @DirtiesContext
    void deleteWrongUser() throws Exception {
        runDeleteUser(mockMvc, "1").andExpect(status().isBadRequest());
    }
}