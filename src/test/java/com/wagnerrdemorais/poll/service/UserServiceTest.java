package com.wagnerrdemorais.poll.service;

import com.wagnerrdemorais.poll.controller.form.NewUserForm;
import com.wagnerrdemorais.poll.controller.form.UpdateUserForm;
import com.wagnerrdemorais.poll.model.User;
import com.wagnerrdemorais.poll.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

class UserServiceTest {

    private UserService subject;
    private final Map<Long, User> userMap = new HashMap<>();

    @BeforeEach
    private void setup() {
        UserRepository userRepository = Mockito.mock(UserRepository.class);

        when(userRepository.save(Mockito.any(User.class))).thenAnswer(invocationOnMock -> {
            User argument = (User) invocationOnMock.getArguments()[0];
            userMap.put(argument.getId(), argument);
            return argument;
        });

        when(userRepository.getById(Mockito.anyLong())).thenAnswer(invocationOnMock -> {
            Long argument = (Long) invocationOnMock.getArguments()[0];
            return userMap.get(argument);
        });

        doAnswer(invocationOnMock -> {
            Long id = (Long) invocationOnMock.getArguments()[0];
            userMap.remove(id);
            return null;
        }).when(userRepository).deleteById(Mockito.anyLong());

        when(userRepository.findAll())
                .thenAnswer(invocationOnMock -> new ArrayList<>(userMap.values()));

        when(userRepository.existsById(Mockito.any(Long.class))).thenAnswer(invocationOnMock -> {
            Long argument = (Long) invocationOnMock.getArguments()[0];
            return userMap.containsKey(argument);
        });

        this.subject = new UserService(userRepository);
    }

    @Test
    void addUser() {
        NewUserForm user = new NewUserForm();
        user.setUsername("test");
        user.setPassword("test");

        User savedUser = this.subject.addUser(user);

        Assertions.assertNotNull(savedUser);
        Assertions.assertEquals(user.getUsername(), savedUser.getUsername());
        Assertions.assertTrue(new BCryptPasswordEncoder().matches(user.getPassword(), savedUser.getPassword()));
    }

    @Test
    void saveUser() {
        User user = new User();
        user.setUsername("test");
        user.setPassword("test");

        User savedUser = this.subject.saveUser(user);

        Assertions.assertNotNull(savedUser);
        Assertions.assertEquals(user.getUsername(), savedUser.getUsername());
        Assertions.assertEquals(user.getPassword(), savedUser.getPassword());
    }

    @Test
    void listUsers() {
        User user1 = new User(1L, "test", "test");
        User user2 = new User(2L, "test2", "test2");

        userMap.put(user1.getId(), user1);
        userMap.put(user2.getId(), user2);

        List<User> users = subject.listUsers();
        Assertions.assertEquals(2L, users.size());
    }

    @Test
    void updateUser() {
        User user1 = new User(1L, "test", "test");
        userMap.put(user1.getId(), user1);

        UpdateUserForm updateUserForm = new UpdateUserForm();
        updateUserForm.setId(user1.getId());
        updateUserForm.setUsername("updated");
        updateUserForm.setPassword("updated");

        User savedUser = this.subject.updateUser(updateUserForm);
        Assertions.assertNotNull(savedUser);
        Assertions.assertEquals("updated", savedUser.getUsername());
        Assertions.assertTrue(new BCryptPasswordEncoder().matches("updated", savedUser.getPassword()));
    }

    @Test
    void deleteUserById() {
        User user1 = new User(1L, "test", "test");
        userMap.put(user1.getId(), user1);

        Assertions.assertEquals(1L, subject.listUsers().size());

        subject.deleteUserById(1L);
        Assertions.assertEquals(0L, subject.listUsers().size());
    }

    @Test
    void getUserById() {
        User user1 = new User(1L, "test", "test");
        userMap.put(user1.getId(), user1);
        Assertions.assertEquals(user1, subject.getUserById(1L));
    }

    @Test
    void existsById() {
        User user1 = new User(1L, "test", "test");
        userMap.put(user1.getId(), user1);
        Assertions.assertTrue(subject.userExistsById(1L));
    }
}