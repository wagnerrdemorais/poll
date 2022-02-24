package com.wagnerrdemorais.poll.service;

import com.wagnerrdemorais.poll.controller.form.NewUserForm;
import com.wagnerrdemorais.poll.controller.form.UpdateUserForm;
import com.wagnerrdemorais.poll.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;

class UserServiceTest extends RepoTestHelper {

    private UserService subject;

    @BeforeEach
    void setup() {
        this.subject = new UserService(userRepository);
    }

    @Test
    void givenNewUser_whenAddUser_thenUserShouldBeAdded() {
        NewUserForm user = new NewUserForm();
        user.setUsername("test");
        user.setPassword("test");

        User savedUser = this.subject.addUser(user);

        Assertions.assertNotNull(savedUser);
        Assertions.assertEquals(user.getUsername(), savedUser.getUsername());
        Assertions.assertTrue(new BCryptPasswordEncoder().matches(user.getPassword(), savedUser.getPassword()));
    }

    @Test
    void givenNewUser_whenSaveUser_thenUserShouldBeSaved() {
        User user = new User();
        user.setUsername("test");
        user.setPassword("test");

        User savedUser = this.subject.saveUser(user);

        Assertions.assertNotNull(savedUser);
        Assertions.assertEquals(user.getUsername(), savedUser.getUsername());
        Assertions.assertEquals(user.getPassword(), savedUser.getPassword());
    }

    @Test
    void givenAListOfUsers_whenListUsers_thenShouldReturnUserList() {
        User user1 = new User(1L, "test", "test");
        User user2 = new User(2L, "test2", "test2");

        userMap.put(user1.getId(), user1);
        userMap.put(user2.getId(), user2);

        List<User> users = subject.listUsers();
        Assertions.assertEquals(2L, users.size());
    }

    @Test
    void givenAUser_whenUpdateUser_thenUserShouldBeUpdated() {
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
    void givenAUser_whenDeleteUserById_thenUserShouldBeDeleted() {
        User user1 = new User(1L, "test", "test");
        userMap.put(user1.getId(), user1);

        Assertions.assertEquals(1L, subject.listUsers().size());

        subject.deleteUserById(1L);
        Assertions.assertEquals(0L, subject.listUsers().size());
    }

    @Test
    void givenAUser_whenGetUserById_thenShouldReturnCorrespondingUser() {
        User user1 = new User(1L, "test", "test");
        userMap.put(user1.getId(), user1);
        Assertions.assertEquals(user1, subject.getUserById(1L));
    }

    @Test
    void givenAUser_whenCheckingIfExistsById_thenShouldReturnTrue() {
        User user1 = new User(1L, "test", "test");
        userMap.put(user1.getId(), user1);
        Assertions.assertTrue(subject.userExistsById(1L));
    }

    @Test
    void givenNoUser_whenCheckingIfExistsById_thenShouldReturnFalse() {
        Assertions.assertFalse(subject.userExistsById(1L));
    }
}