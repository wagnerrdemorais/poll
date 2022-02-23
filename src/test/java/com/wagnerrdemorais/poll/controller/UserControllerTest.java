package com.wagnerrdemorais.poll.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wagnerrdemorais.poll.controller.form.NewUserForm;
import com.wagnerrdemorais.poll.controller.form.UpdateUserForm;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DirtiesContext
    void listUsers() throws Exception {
        NewUserForm newUserForm = new NewUserForm();
        newUserForm.setUsername("test");
        newUserForm.setPassword("test");

        runAdd(asJsonString(newUserForm))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        String expected = "[{\"id\":1,\"username\":\"test\"}]";
        String response = runGetList().andReturn().getResponse().getContentAsString();

        Assertions.assertEquals(expected, response);
    }

    @Test
    @DirtiesContext
    void getUser() throws Exception {
        NewUserForm newUserForm = new NewUserForm();
        newUserForm.setUsername("test");
        newUserForm.setPassword("test");

        runAdd(asJsonString(newUserForm))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        String expected = "{\"id\":1,\"username\":\"test\"}";
        String response = runGetById("1").andReturn().getResponse().getContentAsString();

        Assertions.assertEquals(expected, response);
    }

    @Test
    @DirtiesContext
    void newUser() throws Exception {
        NewUserForm newUserForm = new NewUserForm();
        newUserForm.setUsername("test");
        newUserForm.setPassword("test");

        String addedUser = runAdd(asJsonString(newUserForm))
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

        runAdd(asJsonString(newUserForm))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        UpdateUserForm updateUserForm = new UpdateUserForm();
        updateUserForm.setId(1L);
        updateUserForm.setUsername("updated");
        updateUserForm.setPassword("updated");

        String updated = runUpdate(updateUserForm)
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

        runAdd(asJsonString(newUserForm))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        String userList = runGetList().andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        String expected = "[{\"id\":1,\"username\":\"test\"}]";
        Assertions.assertEquals(expected, userList);

        runDelete("1").andExpect(status().isOk());

        String list2 = runGetList().andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        Assertions.assertEquals("[]", list2);
    }

    @Test
    @DirtiesContext
    void getWrongUser() throws Exception {
        runGetById("1").andExpect(status().isBadRequest());

    }

    @Test
    @DirtiesContext
    void deleteWrongUser() throws Exception {
        runDelete("1").andExpect(status().isBadRequest());
    }

    public static String asJsonString(final Object obj) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private ResultActions runGetList() throws Exception {
        return this.mockMvc.perform(get("/users/list"))
                .andDo(print());
    }

    private ResultActions runAdd(String content) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.post("/users/new")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));
    }

    private ResultActions runUpdate(UpdateUserForm updatedForm) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.put("/users/update")
                .content(asJsonString(updatedForm))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

    }

    private ResultActions runDelete(String id) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.delete("/users/delete")
                .param("id", id));
    }

    private ResultActions runGetById(String id) throws Exception {
        return this.mockMvc.perform(get("/users/get")
                        .param("userId", id))
                .andDo(print());
    }
}