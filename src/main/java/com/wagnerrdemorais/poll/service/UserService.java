package com.wagnerrdemorais.poll.service;

import com.wagnerrdemorais.poll.controller.form.NewUserForm;
import com.wagnerrdemorais.poll.controller.form.UpdateUserForm;
import com.wagnerrdemorais.poll.model.User;
import com.wagnerrdemorais.poll.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service for User operations
 */
@Service
public class UserService {

    private final UserRepository userRepository;

    /**
     * All args constructor
     * @param userRepository UserRepository
     */
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Add given user to database
     * @param newUserForm NewUserForm
     * @return User
     */
    public User addUser(NewUserForm newUserForm) {
        User newUser = new User();
        newUser.setUsername(newUserForm.getUsername());
        newUser.setPassword(new BCryptPasswordEncoder()
                .encode(newUserForm.getPassword()));
        return this.saveUser(newUser);
    }

    /**
     * Saves given User
     * @param user User
     * @return User
     */
    public User saveUser(User user) {
        return this.userRepository.save(user);
    }

    /**
     * Get all users
     * @return List<User>
     */
    public List<User> listUsers() {
        return this.userRepository.findAll();
    }

    /**
     * Update given user
     * @param updateUserForm UpdateUserForm
     * @return User
     */
    public User updateUser(UpdateUserForm updateUserForm) {
        User updatedUser = this.userRepository.getById(updateUserForm.getId());

        updatedUser.setUsername(updateUserForm.getUsername());
        updatedUser.setPassword(new BCryptPasswordEncoder()
                .encode(updateUserForm.getPassword()));

        return this.saveUser(updatedUser);
    }

    /**
     * Deletes user by id
     * @param userId Long
     */
    public void deleteUserById(Long userId) {
        this.userRepository.deleteById(userId);
    }

    /**
     * Check if user exists by its id
     * @param userId Long
     * @return boolean
     */
    public boolean userExistsById(Long userId) {
        return this.userRepository.existsById(userId);
    }

    /**
     * Return user By given id if found
     * @param userId Long
     * @return User
     */
    public User getUserById(Long userId) {
        return this.userRepository.getById(userId);
    }
}
