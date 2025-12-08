package by.tms.service;


import by.tms.exception.UserNotFoundException;
import by.tms.model.User;
import by.tms.model.UserCreateDto;
import by.tms.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.getAllUsers();
    }

    public Optional<User> getUserById(int id) {
        return userRepository.getUserById(id);
    }

    public boolean addUser(UserCreateDto user) {
        return userRepository.addUser(user).getId() != null;
    }

    public boolean deleteUserById(int id) {
        if (getUserById(id).isEmpty()) {
            throw new UserNotFoundException(id);
        }
        userRepository.deleteUserById(id);
        Optional<User> user = getUserById(id);
        return user.isEmpty();
    }

    public Optional<User> updateUser(User user) {
        Optional<User> userOptional = getUserById(user.getId());
        if (userOptional.isPresent()) {
            return userRepository.updateUser(user);
        } else {
            throw new UserNotFoundException(user);
        }
    }
}
