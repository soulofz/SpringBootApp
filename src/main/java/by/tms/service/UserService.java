package by.tms.service;


import by.tms.model.User;
import by.tms.model.UserRegistrationDto;
import by.tms.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
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

    public boolean addUser(UserRegistrationDto user) {
        return userRepository.addUser(user);
    }

    public boolean deleteUserById(int id) {
        Optional<User> user = getUserById(id);
        if (user.isPresent() && userRepository.deleteUserById(id)) {
            user = getUserById(id);
            return user.isEmpty();
        }
        return false;
    }
}
