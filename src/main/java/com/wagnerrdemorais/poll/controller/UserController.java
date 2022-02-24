package com.wagnerrdemorais.poll.controller;

import com.wagnerrdemorais.poll.controller.form.NewUserForm;
import com.wagnerrdemorais.poll.controller.form.UpdateUserForm;
import com.wagnerrdemorais.poll.dto.UserDto;
import com.wagnerrdemorais.poll.model.User;
import com.wagnerrdemorais.poll.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Rest controller for Users
 */
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Returns all users
     * @return ResponseEntity<List<UserDto>>
     */
    @GetMapping("/list")
    public ResponseEntity<List<UserDto>> listUsers() {
        List<User> users = this.userService.listUsers();
        List<UserDto> dtoList = getUserDtoList(users);
        return ResponseEntity.ok(dtoList);
    }

    /**
     * Returns user if found by id
     * @param userId Long
     * @return ResponseEntity<UserDto>
     */
    @GetMapping("/get")
    public ResponseEntity<UserDto> getUser(Long userId) {
        if (userId == null || !userService.userExistsById(userId)) {
            return ResponseEntity.badRequest().build();
        }
        User user = this.userService.getUserById(userId);
        return ResponseEntity.ok(userToDto(user));
    }

    /**
     * Given a NewUserForm, creates a User and returns UserDto
     * @param newUserForm NewUserForm
     * @return ResponseEntity<UserDto>
     */
    @PostMapping("/new")
    public ResponseEntity<UserDto> newUser(@RequestBody NewUserForm newUserForm) {
        User user = userService.addUser(newUserForm);
        return ResponseEntity.ok(userToDto(user));
    }

    /**
     * Given a UserForm, updates the User and return the UserDto object with updated info
     * @param updateUserForm UpdateUserForm
     * @return ResponseEntity<UserDto>
     */
    @PutMapping("/update")
    public ResponseEntity<UserDto> updateUser(@RequestBody UpdateUserForm updateUserForm) {
        User user = userService.updateUser(updateUserForm);
        return ResponseEntity.ok(userToDto(user));
    }

    /**
     * Delete user for given id if it exists
     * @param id Long
     * @return ResponseEntity<String>
     */
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteUserById(Long id) {
        if (id == null || !userService.userExistsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        userService.deleteUserById(id);
        return ResponseEntity.ok("User deleted");
    }

    /**
     * Transform User to UserDto
     * @param user User
     * @return UserDto
     */
    private UserDto userToDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setUsername(user.getUsername());
        return userDto;
    }

    /**
     * Transform a List of User into a List of UserDto
     * @param users List<User>
     * @return List<UserDto>
     */
    private List<UserDto> getUserDtoList(List<User> users) {
        return users.stream()
                .map(this::userToDto).collect(Collectors.toList());
    }
}
