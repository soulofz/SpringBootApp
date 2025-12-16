package by.tms.controllers;

import by.tms.exception.ForbiddenException;
import by.tms.exception.UserNotFoundException;
import by.tms.model.User;
import by.tms.model.UserCreateDto;
import by.tms.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> allUsers = userService.getAllUsers();
        if (allUsers.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(allUsers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") int id) throws ForbiddenException {
        Optional<User> user = userService.getUserById(id);
        if (user.isPresent()) {
            return new ResponseEntity<>(user.get(), HttpStatus.OK);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<HttpStatusCode> addUser(@RequestBody UserCreateDto user) {
        User userForSave = userService.addUser(user);
        if (userForSave != null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatusCode> deleteUser(@PathVariable("id") int id) throws ForbiddenException {
        if (userService.deleteUserById(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    @PutMapping
    public ResponseEntity<User> updateUser(@RequestBody User user) throws ForbiddenException {
        Optional<User> userOptional = userService.updateUser(user);
        if (userOptional.isPresent()) {
            return new ResponseEntity<>(userOptional.get(), HttpStatus.OK);
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    @GetMapping("/sort/{field}")
    public ResponseEntity<List<User>> getSortedUsersByField(@PathVariable("field") String field, @RequestParam("order") String order) {
        List<User> users = userService.getSortedUsersByField(field, order);
        if (users.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(users);
    }

    @GetMapping("/pagination/{page}/{size}")
    public ResponseEntity<Page<User>> getAllUsersWithPagination(@PathVariable("page") int page, @PathVariable("size") int size) {
        Page<User> users = userService.getAllUsersWithPagination(page, size);
        if (users.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(users);
    }

    @GetMapping("/myself")
    public ResponseEntity<User> getMyself() {
        Optional<User> user = userService.getInfoAboutMyself();
        if (user.isEmpty()) {
            throw new UserNotFoundException(-1);
        }
        return ResponseEntity.ok(user.get());
    }
}