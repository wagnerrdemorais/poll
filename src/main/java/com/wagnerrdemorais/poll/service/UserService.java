package com.wagnerrdemorais.poll.service;

import com.wagnerrdemorais.poll.controller.form.NewUserForm;
import com.wagnerrdemorais.poll.controller.form.UpdateUserForm;
import com.wagnerrdemorais.poll.model.User;
import com.wagnerrdemorais.poll.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User addUser(NewUserForm newUserForm) {
        User newUser = new User();
        newUser.setUsername(newUserForm.getUsername());
        newUser.setPassword(new BCryptPasswordEncoder()
                .encode(newUserForm.getPassword()));
        return this.saveUser(newUser);
    }

    public User saveUser(User user) {
        return this.userRepository.save(user);
    }

    public List<User> listUsers() {
        return this.userRepository.findAll();
    }

    public User updateUser(UpdateUserForm updateUserForm) {
        User updatedUser = this.userRepository.getById(updateUserForm.getId());

        updatedUser.setUsername(updateUserForm.getUsername());
        updatedUser.setPassword(new BCryptPasswordEncoder()
                .encode(updateUserForm.getPassword()));

        return this.saveUser(updatedUser);
    }

    public void deleteUserById(Long userId) {
        this.userRepository.deleteById(userId);
    }

    public boolean userExistsById(Long userId) {
        return this.userRepository.existsById(userId);
    }

    public User getUserById(Long userId) {
        return this.userRepository.getById(userId);
    }
}
